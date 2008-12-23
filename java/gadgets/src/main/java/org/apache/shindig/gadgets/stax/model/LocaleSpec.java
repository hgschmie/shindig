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
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.StaxUtils;

public class LocaleSpec extends SpecElement {

  public static final String ELEMENT_NAME = "Locale";

  private static final String ATTR_LANG = "lang";
  private static final String ATTR_COUNTRY = "country";
  private static final String ATTR_LANGUAGE_DIRECTION = "language_direction";
  private static final String ATTR_MESSAGES = "messages";

  public LocaleSpec(final QName name) {
    super(name);
  }

  private Set<LocaleMsg> localeMsgs = new HashSet<LocaleMsg>();

  private String language = null;
  private String country = null;
  private String languageDirection = null;
  private String messages = null;

  public String getLanguage() {
    return StringUtils.defaultString(language, "all");
  }

  public String getCountry() {
    return StringUtils.defaultString(country, "ALL");
  }

  public Direction getLanguageDirection() {
    return Direction.parse(languageDirection);
  }

  public Uri getMessages() {
    return StaxUtils.toUri(messages);
  }

  public Set<LocaleMsg> getLocalMsgs() {
    return Collections.unmodifiableSet(localeMsgs);
  }

  private void addLocaleMsg(final LocaleMsg localeMsg) {
    localeMsgs.add(localeMsg);
  }

  private void setLanguage(String language) {
    this.language = language;
  }

  private void setCountry(String country) {
    this.country = country;
  }

  private void setLanguageDirection(String languageDirection) {
    this.languageDirection = languageDirection;
  }

  private void setMessages(String messages) {
    this.messages = messages;
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (language != null) {
      writer.writeAttribute(namespaceURI, ATTR_LANG, getLanguage());
    }
    if (country != null) {
      writer.writeAttribute(namespaceURI, ATTR_COUNTRY, getCountry());
    }
    if (languageDirection != null) {
      writer.writeAttribute(namespaceURI, ATTR_LANGUAGE_DIRECTION,
          getLanguageDirection().toString());
    }
    if (messages != null) {
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
    if (messages == null && localeMsgs.size() == 0) {
      throw new SpecParserException(name().getLocalPart()
          + " must either contain a @messages attribute or <msg> elements!");
    }
  }

  public static enum Direction {
    LTR, RTL;

    public static Direction parse(String value) {
      for (Direction direction : Direction.values()) {
        if (direction.toString().compareToIgnoreCase(value) == 0) {
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
    private final QName attrLang;
    private final QName attrCountry;
    private final QName attrLanguageDirection;
    private final QName attrMessages;

    public Parser() {
      this(new QName(ELEMENT_NAME));
    }

    public Parser(final QName name) {
      super(name);
      register(new LocaleMsg.Parser());
      attrLang = buildQName(name, ATTR_LANG);
      attrCountry = buildQName(name, ATTR_COUNTRY);
      attrLanguageDirection = buildQName(name, ATTR_LANGUAGE_DIRECTION);
      attrMessages = buildQName(name, ATTR_MESSAGES);
    }

    @Override
    protected LocaleSpec newElement() {
      return new LocaleSpec(getName());
    }

    @Override
    protected void setAttribute(final LocaleSpec locale, final QName name,
        final String value) {
      if (name.equals(attrLang)) {
        locale.setLanguage(value);
      } else if (name.equals(attrCountry)) {
        locale.setCountry(value);
      } else if (name.equals(attrLanguageDirection)) {
        locale.setLanguageDirection(value);
      } else if (name.equals(attrMessages)) {
        locale.setMessages(value);
      } else {
        super.setAttribute(locale, name, value);
      }
    }

    @Override
    protected void addChild(XMLStreamReader reader, final LocaleSpec locale,
        final SpecElement child) throws SpecParserException  {
      if (child instanceof LocaleMsg) {
        locale.addLocaleMsg((LocaleMsg) child);
      } else {
        super.addChild(reader, locale, child);
      }
    }
  }
}
