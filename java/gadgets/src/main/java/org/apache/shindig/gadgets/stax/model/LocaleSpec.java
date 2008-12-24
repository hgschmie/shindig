package org.apache.shindig.gadgets.stax.model;

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

import org.apache.commons.lang.StringUtils;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.spec.SpecParserException;

public class LocaleSpec extends SpecElement {

  public static final String ELEMENT_NAME = "Locale";

  public static final String ATTR_LANG = "lang";
  public static final String ATTR_COUNTRY = "country";
  public static final String ATTR_LANGUAGE_DIRECTION = "language_direction";
  public static final String ATTR_MESSAGES = "messages";

  private Set<LocaleMsg> localeMsgs = new HashSet<LocaleMsg>();

  public LocaleSpec(final QName name, final Map<String, QName> attrNames, final Uri base) {
    super(name, attrNames, base);
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
    return attrUriNull(ATTR_MESSAGES);
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
    final String namespaceURI = name().getNamespaceURI();

    if (attr(ATTR_LANG) != null) {
      writer.writeAttribute(namespaceURI, ATTR_LANG, getLanguage());
    }
    if (attr(ATTR_COUNTRY) != null) {
      writer.writeAttribute(namespaceURI, ATTR_COUNTRY, getCountry());
    }
    if (attr(ATTR_LANGUAGE_DIRECTION) != null) {
      writer.writeAttribute(namespaceURI, ATTR_LANGUAGE_DIRECTION,
          getLanguageDirection().toString());
    }
    if (getMessages() != null) {
      writer.writeAttribute(namespaceURI, ATTR_MESSAGES, getMessages()
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
    if (attr(ATTR_MESSAGES) == null && localeMsgs.size() == 0) {
      throw new SpecParserException(name().getLocalPart()
          + " must contain a @messages attribute or <msg> elements!");
    }
  }

  public static enum Direction {
    LTR, RTL;

    public static Direction parse(String value) {
      for (Direction direction : Direction.values()) {
        if (StringUtils.equalsIgnoreCase(direction.toString(), value)) {
          return direction;
        }
      }
      return LTR;
    }

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

  public static class Parser extends SpecElement.Parser<LocaleSpec> {

    public Parser(final Uri base) {
      this(new QName(ELEMENT_NAME), base);
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
        final SpecElement child) throws SpecParserException {
      if (child instanceof LocaleMsg) {
        locale.addLocaleMsg((LocaleMsg) child);
      } else {
        super.addChild(reader, locale, child);
      }
    }
  }
}
