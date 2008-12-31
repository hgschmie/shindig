package org.apache.shindig.gadgets.stax;
/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.common.cache.Cache;
import org.apache.shindig.common.cache.CacheProvider;
import org.apache.shindig.common.cache.SoftExpiringCache;
import org.apache.shindig.common.cache.SoftExpiringCache.CachedObject;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.MessageBundleFactory;
import org.apache.shindig.gadgets.http.HttpFetcher;
import org.apache.shindig.gadgets.http.HttpRequest;
import org.apache.shindig.gadgets.http.HttpResponse;
import org.apache.shindig.gadgets.spec.GadgetSpec;
import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.LocaleMsg;
import org.apache.shindig.gadgets.stax.model.LocaleSpec;
import org.apache.shindig.gadgets.stax.model.MessageBundleSpec;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ShindigMessageBundleFactory implements MessageBundleFactory {

  private static final Logger LOG = Logger
  .getLogger(ShindigMessageBundleFactory.class.getName());

  public static final Locale LOCALE_ALL_ALL = new Locale("all", "ALL");

  private final XMLInputFactory factory;

  private final HttpFetcher fetcher;
  private final SoftExpiringCache<String, MessageBundle> cache;
  private final long refresh;

  @Inject
  public ShindigMessageBundleFactory(final HttpFetcher fetcher,
      final CacheProvider cacheProvider,
      @Named("shindig.cache.xml.refreshInterval")
      final long refresh) {

    final Cache<String, MessageBundle> baseCache = cacheProvider
    .createCache(MessageBundleFactory.CACHE_NAME);

    this.fetcher = fetcher;
    this.cache = new SoftExpiringCache<String, MessageBundle>(baseCache);
    this.refresh = refresh;

    this.factory = XMLInputFactory.newInstance();
    this.factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
  }

  @Override
  public MessageBundle getBundle(final GadgetSpec spec, final Locale locale,
      final boolean ignoreCache) throws GadgetException {

    if (locale == null) {
      return MessageBundle.EMPTY;
    }

    final String key = spec.getUrl().toString() + '.' + locale.toString();
    CachedObject<MessageBundle> cached = null;

    if (!ignoreCache) {
      cached = cache.getElement(key);
      if (cached != null && !cached.isExpired) {
        return cached.obj;
      }
    }

    MessageBundle bundle = null;

    try {
      bundle = loadBundle(spec, locale, ignoreCache);
    } catch (GadgetException e) {
      if (cached != null) {
        bundle = cached.obj;
        LOG.warning("Could not refresh MessageBundle, keeping stale entry.");
      } else {
        LOG.warning("Could not load MessageBundle, creating empty dummy.");
        bundle = MessageBundle.EMPTY;
      }
    } finally {
      if (!ignoreCache) {
        cache.addElement(key, bundle, refresh);
      }
    }
    return bundle;
  }

  private MessageBundle loadBundle(final GadgetSpec spec, final Locale locale,
      final boolean ignoreCache) throws GadgetException {

    Locale parentLocale = null;

    // Look if there a parent bundle to load. We try
    // en_US -> en_ALL -> all_ALL.
    if (!locale.getLanguage().equalsIgnoreCase("all")) {
      parentLocale = locale.getCountry().equalsIgnoreCase("ALL") ? LOCALE_ALL_ALL
          : new Locale(locale.getLanguage(), "ALL");
    }

    final MessageBundle parentBundle = getBundle(spec, parentLocale, ignoreCache);
    final LocaleSpec localeSpec = spec.getModulePrefs().getLocale(locale);

    if (localeSpec == null) {
      return parentBundle; // This either exists or is MessageBundle.EMPTY;
    }


    final Uri messageUrl = localeSpec.getMessages();
    Map<String, String> messages = null;
    if (messageUrl != null) {
      final MessageBundleSpec messageBundleSpec = fetchMessageBundle(messageUrl, ignoreCache);
      messages = addMessages(messages, messageBundleSpec.getLocaleMsgs());
    }

    messages = addMessages(messages, localeSpec.getLocaleMsgs());

    return new MessageBundle(parentBundle.getMessages(), messages, localeSpec.getLanguageDirection());
  }

  private  static Map<String, String> addMessages(
      final Map<String, String> messages, final Set<LocaleMsg> msgs) {
    final Map<String, String> targetMap = (messages != null) ? messages
        : new HashMap<String, String>((msgs != null) ? msgs.size() : 10);
    if (msgs != null) {
      for (LocaleMsg msg : msgs) {
        targetMap.put(msg.getName(), msg.getText());
      }
    }
    return targetMap;
  }

  protected MessageBundleSpec fetchMessageBundle(final Uri uri,
      final boolean ignoreCache) throws GadgetException {

    HttpRequest request = new HttpRequest(uri).setIgnoreCache(ignoreCache);

    // Since we don't allow any variance in cache time, we should just force the
    // cache time
    // globally. This ensures propagation to shared caches when this is set.
    request.setCacheTtl((int) (refresh / 1000));

    final HttpResponse response = fetcher.fetch(request);

    if (response == null) {
      throw new GadgetException(
          GadgetException.Code.FAILED_TO_RETRIEVE_CONTENT, String.format(
              "Unable to retrieve message bundle xml from '%s', got null!",
              uri.toString()));
    }

    if (response.getHttpStatusCode() != HttpResponse.SC_OK) {
      throw new GadgetException(
          GadgetException.Code.FAILED_TO_RETRIEVE_CONTENT, String.format(
              "Unable to retrieve message bundle xml from '%s'. HTTP error %d",
              uri.toString(), response.getHttpStatusCode()));
    }

    try {
      XMLStreamReader reader = factory.createXMLStreamReader(response
          .getResponse());
      final MessageBundleSpec.Parser parser = new MessageBundleSpec.Parser(uri);
      MessageBundleSpec messageBundleSpec = null;

      loop: while (true) {
        final int event = reader.next();
        switch (event) {
        case XMLStreamConstants.END_DOCUMENT:
          reader.close();
          break loop;
        case XMLStreamConstants.START_ELEMENT:
          messageBundleSpec = parser.parse(reader);
          break;
        default:
          break;
        }
      }
      return messageBundleSpec;

    } catch (XMLStreamException xse) {
      throw new SpecParserException(String.format(
          "Could not parse Message bundle from '%s': ", uri.toString()), xse);
    }
  }

  // Code for unit tests.
  public SoftExpiringCache<String, MessageBundle> getCache()
  {
    return cache;
  }
}
