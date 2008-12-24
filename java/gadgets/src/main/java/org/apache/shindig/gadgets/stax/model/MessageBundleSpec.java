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
package org.apache.shindig.gadgets.stax.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.LocaleSpec.Direction;

public class MessageBundleSpec extends SpecElement {

  public static final String ELEMENT_NAME = "messageBundle";

  /** Non-0.8 Attribute! */
  private static final String ATTR_LANGUAGE_DIRECTION = "language_direction";

  private Set<LocaleMsg> localeMsgs = new HashSet<LocaleMsg>();

  private String languageDirection = null;

  private StringBuilder text = new StringBuilder();

  public MessageBundleSpec(final QName name) {
    super(name);
  }

  public Direction getLanguageDirection() {
    return Direction.parse(languageDirection);
  }

  @Override
  public String getText() {
    return text.toString();
  }

  public Set<LocaleMsg> getLocalMsgs() {
    return Collections.unmodifiableSet(localeMsgs);
  }

  private void addLocaleMsg(final LocaleMsg localeMsg) {
    localeMsgs.add(localeMsg);
  }

  private void setLanguageDirection(String languageDirection) {
    this.languageDirection = languageDirection;
  }

  private void addText(final String text) {
    this.text.append(text);
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (languageDirection != null) {
      writer.writeAttribute(namespaceURI, ATTR_LANGUAGE_DIRECTION,
          getLanguageDirection().toString());
    }
  }

  public static class Parser extends SpecElement.Parser<MessageBundleSpec> {

    private final QName attrLanguageDirection;

    public Parser() {
      this(new QName(ELEMENT_NAME));
    }

    public Parser(final QName name) {
      super(name);
      register(new LocaleMsg.Parser());
      this.attrLanguageDirection = buildQName(name, ATTR_LANGUAGE_DIRECTION);
    }

    @Override
    protected MessageBundleSpec newElement() {
      return new MessageBundleSpec(getName());
    }

    @Override
    protected void setAttribute(final MessageBundleSpec messageBundle, final QName name,
        final String value) {
      if (name.equals(attrLanguageDirection)) {
        messageBundle.setLanguageDirection(value);
      }else {
        super.setAttribute(messageBundle, name, value);
      }
    }

    @Override
    protected void addText(final XMLStreamReader reader, final MessageBundleSpec messageBundle) {
      if (!reader.isWhiteSpace()) {
        messageBundle.addText(reader.getText());
      }
    }

    @Override
    protected void addChild(XMLStreamReader reader, final MessageBundleSpec messageBundle,
        final SpecElement child) throws SpecParserException  {
      if (child instanceof LocaleMsg) {
        messageBundle.addLocaleMsg((LocaleMsg) child);
      } else {
        super.addChild(reader, messageBundle, child);
      }
    }
  }
}
