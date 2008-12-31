/*
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
 */
package org.apache.shindig.gadgets;

import java.net.URI;
import java.util.logging.Logger;

import org.apache.shindig.common.cache.Cache;
import org.apache.shindig.common.cache.CacheProvider;
import org.apache.shindig.common.cache.SoftExpiringCache;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.common.util.Check;
import org.apache.shindig.gadgets.http.HttpFetcher;
import org.apache.shindig.gadgets.http.HttpRequest;
import org.apache.shindig.gadgets.http.HttpResponse;
import org.apache.shindig.gadgets.spec.GadgetSpec;


/**
 * The basic scaffolding for setting up a GadgetSpec Factory.
 */
public abstract class AbstractGadgetSpecFactory implements GadgetSpecFactory {

    public static final String ERROR_SPEC = "<Module><ModulePrefs title='Error'/><Content/></Module>";
    public static final String ERROR_KEY = "parse.exception";

    public static final String CACHE_NAME = "gadgetSpecs";

    private static final Logger LOG = Logger.getLogger(AbstractGadgetSpecFactory.class.getName());

    private final HttpFetcher fetcher;
    private final SoftExpiringCache<Uri, GadgetSpec> cache;
    private final long refresh;

    protected AbstractGadgetSpecFactory(final HttpFetcher fetcher,
            final CacheProvider cacheProvider,
            final long refresh)
    {
        // TODO This definitely sucks.
        final Cache<Uri, GadgetSpec> baseCache = cacheProvider.createCache(CACHE_NAME);

        this.fetcher = fetcher;
        this.cache = new SoftExpiringCache<Uri, GadgetSpec>(baseCache);
        this.refresh = refresh;
    }



    public GadgetSpec getGadgetSpec(final GadgetContext context) throws GadgetException {
        // DefaultGadgetSpecFactory tries to squirrel some extra functionality into
        // this method by doing
        //
        // if (rawxml != null) {
        //   return new GadgetSpec(RAW_GADGET_URI, rawxml);
        // }
        //
        // This is non-sensical and should not be here. It circumvents the cache and does strange
        // things to URLs. TODO: Find out what this should do, implement it in a better way.
        //
        return getGadgetSpec(context.getUrl(), context.getIgnoreCache());
    }

    public GadgetSpec getGadgetSpec(final URI gadgetUri, final boolean ignoreCache) throws GadgetException {

        final Uri uri = Uri.fromJavaUri(gadgetUri);
        SoftExpiringCache.CachedObject<GadgetSpec> cached = null;

        if (!ignoreCache) {
            cached = cache.getElement(uri);
            if (cached != null && !cached.isExpired) {
                final GadgetSpec spec = cached.obj;
                Check.notNull(spec);

                // If we pulled the error gadget spec from the cache, this allows us to re-throw the
                // cached exception.
                final GadgetException cachedException = (GadgetException) spec.getSpecAttribute(ERROR_KEY);
                if (cachedException != null) {
                    throw cachedException;
                }
                return spec;
            }
        }

        final HttpRequest request = new HttpRequest(uri)
                .setIgnoreCache(ignoreCache)
                // Since we don't allow any variance in cache time, we should just force the cache time
                // globally. This ensures propagation to shared caches when this is set.
                .setCacheTtl((int) (refresh / 1000));

        final HttpResponse response = fetcher.fetch(request);

        if (response.getHttpStatusCode() != HttpResponse.SC_OK) {
            throw new GadgetException(GadgetException.Code.FAILED_TO_RETRIEVE_CONTENT,
                    "Unable to retrieve gadget xml. HTTP error " + response.getHttpStatusCode());
        }

        GadgetSpec spec = null;
        try {
            spec = buildGadgetSpec(uri, response.getResponseAsString());
            return spec;
        } catch (GadgetException e) {
            // We could not parse the gadget spec for a reason. If the remote site is down, we will
            // now hammer on it until it comes back up. Same if the Gadget is simply not parsable.
            //
            // To avoid this, this adds negative caching of a minimal error gadget for the URL.

            // Did the existing object just expire?
            if (cached != null) {
                spec = cached.obj; // It might be stale, but this is better than returning an error... TODO: This should be configurable.
                LOG.warning(String.format("Could not refresh '%s', keeping stale entry.", uri));
            } else {
                spec = buildGadgetSpec(uri, ERROR_SPEC);
                // That looks pretty brittle to me...
                spec.setSpecAttribute(ERROR_KEY, e);
            }
            throw e;
        }
        finally {
            cache.addElement(uri, spec, refresh);
        }
    }

    protected abstract GadgetSpec buildGadgetSpec(final Uri uri, final String xml) throws GadgetException;
}
