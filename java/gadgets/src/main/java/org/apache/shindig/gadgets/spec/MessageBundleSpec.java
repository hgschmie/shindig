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
package org.apache.shindig.gadgets.spec;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.stax.MessageBundle;
import org.apache.shindig.gadgets.stax.MessageBundle.Direction;
import org.apache.shindig.gadgets.variables.Substitutions;

public class MessageBundleSpec extends SpecElement implements MessageBundle.MessageBundleSource {

  public static final String ELEMENT_NAME = "messageBundle";

  /** Non-0.8 Attribute! */
  public static final String ATTR_LANGUAGE_DIRECTION = "language_direction";

  private Set<LocaleMsg> localeMsgs = new LinkedHashSet<LocaleMsg>();

  private StringBuilder text = new StringBuilder();

  public MessageBundleSpec(final QName name,
      final Map<String, QName> attrNames, final Uri base) {
    super(name, attrNames, base);
  }

  protected MessageBundleSpec(final MessageBundleSpec messageBundleSpec, final Substitutions substituter) {
      super(messageBundleSpec, substituter);
  }

  @Override
  public MessageBundleSpec substitute(final Substitutions substituter) {
    return new MessageBundleSpec(this, substituter);
  }

  public Direction getLanguageDirection() {
    return Direction.parse(attr(ATTR_LANGUAGE_DIRECTION));
  }

  @Override
  public String getText() {
    return text.toString();
  }

  public Set<LocaleMsg> getLocaleMsgs() {
    return Collections.unmodifiableSet(localeMsgs);
  }

  private void addLocaleMsg(final LocaleMsg localeMsg) {
    localeMsgs.add(localeMsg);
  }

  private void addText(final String text) {
    this.text.append(text);
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (attr(ATTR_LANGUAGE_DIRECTION) != null) {
      writer.writeAttribute(namespaceURI, ATTR_LANGUAGE_DIRECTION,
          getLanguageDirection().toString());
    }
  }

  @Override
  protected void writeChildren(final XMLStreamWriter writer) throws XMLStreamException {
    for (LocaleMsg localeMsg: localeMsgs) {
      localeMsg.toXml(writer);
    }
  }

  public static class Parser extends SpecElement.Parser<MessageBundleSpec> {

    public Parser(final Uri base) {
      this(new QName(ELEMENT_NAME), base);
    }

    public Parser(final QName name, final Uri base) {
      super(name, base);
      register(new LocaleMsg.Parser(base));
      register(ATTR_LANGUAGE_DIRECTION);
    }

    @Override
    protected MessageBundleSpec newElement() {
      return new MessageBundleSpec(name(), getAttrNames(), getBase());
    }

    @Override
    protected void addText(final XMLStreamReader reader,
        final MessageBundleSpec messageBundle) {
      if (!reader.isWhiteSpace()) {
        messageBundle.addText(reader.getText());
      }
    }

    @Override
    protected void addChild(XMLStreamReader reader,
        final MessageBundleSpec messageBundle, final SpecElement child)
        throws GadgetException {
      if (child instanceof LocaleMsg) {
        messageBundle.addLocaleMsg((LocaleMsg) child);
      } else {
        super.addChild(reader, messageBundle, child);
      }
    }
  }
}
