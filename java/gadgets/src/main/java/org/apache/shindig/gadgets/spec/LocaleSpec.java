package org.apache.shindig.gadgets.spec;

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

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.spec.MessageBundle.Direction;
import org.apache.shindig.gadgets.variables.Substitutions;

public class LocaleSpec extends SpecElement implements MessageBundle.MessageBundleSource {

  public static final QName ELEMENT_NAME = new QName(SpecElement.OPENSOCIAL_NAMESPACE_URI, "Locale");

  public static final String ATTR_LANG = "lang";
  public static final String ATTR_COUNTRY = "country";
  public static final String ATTR_LANGUAGE_DIRECTION = "language_direction";
  public static final String ATTR_MESSAGES = "messages";

  private Set<LocaleMsg> localeMsgs = new HashSet<LocaleMsg>();

  protected LocaleSpec(final QName name, final Map<String, QName> attrNames, final Uri base) {
    super(name, attrNames, base);
  }

  protected LocaleSpec(final LocaleSpec localeSpec, final Substitutions substituter) {
      super(localeSpec, substituter);
  }

  @Override
  public LocaleSpec substitute(final Substitutions substituter) {
    return new LocaleSpec(this, substituter);
  }

  public String getLanguage() {
    return attrDefault(ATTR_LANG, "all");
  }

  public String getCountry() {
    return attrDefault(ATTR_COUNTRY, "ALL");
  }

  public Direction getLanguageDirection() {
    return Direction.parse(attr(ATTR_LANGUAGE_DIRECTION));
  }

  public Uri getMessages() {
      return attrUriBase(ATTR_MESSAGES);
  }

  public Set<LocaleMsg> getLocaleMsgs() {
    return Collections.unmodifiableSet(localeMsgs);
  }

  private void addLocaleMsg(final LocaleMsg localeMsg) {
    localeMsgs.add(localeMsg);
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    if (attr(ATTR_LANG) != null) {
      writeAttribute(writer, ATTR_LANG, getLanguage());
    }
    if (attr(ATTR_COUNTRY) != null) {
      writeAttribute(writer, ATTR_COUNTRY, getCountry());
    }
    if (attr(ATTR_LANGUAGE_DIRECTION) != null) {
      writeAttribute(writer, ATTR_LANGUAGE_DIRECTION,
          getLanguageDirection().toString());
    }
    if (getMessages() != null) {
      writeAttribute(writer, ATTR_MESSAGES, getMessages()
          .toString());
    }
  }

  @Override
  protected void writeChildren(final XMLStreamWriter writer)
      throws XMLStreamException {
    for (LocaleMsg localeMsg : localeMsgs) {
      localeMsg.toXml(writer);
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (getLanguageDirection() == null) {
      throw new SpecParserException("Direction '" + attr(ATTR_LANGUAGE_DIRECTION) + "' is invalid!");
    }
    if (!attrIsValidUri(ATTR_MESSAGES)) {
      throw new SpecParserException("Messages URI '" + attr(ATTR_MESSAGES) + "' is invalid!");
    }
  }

  public static class Parser extends SpecElement.Parser<LocaleSpec> {

    public Parser(final Uri base) {
      this(ELEMENT_NAME, base);
    }

    public Parser(final QName name, final Uri base) {
      super(name, base);
      register(new LocaleMsg.Parser(base));
      register(ATTR_LANG, ATTR_COUNTRY, ATTR_LANGUAGE_DIRECTION, ATTR_MESSAGES);
    }

    @Override
    protected LocaleSpec newElement() {
      return new LocaleSpec(name(), getAttrNames(), getBase());
    }

    @Override
    protected void addChild(XMLStreamReader reader, final LocaleSpec locale,
        final SpecElement child) throws GadgetException {
      if (child instanceof LocaleMsg) {
        locale.addLocaleMsg((LocaleMsg) child);
      } else {
        super.addChild(reader, locale, child);
      }
    }
  }
}
