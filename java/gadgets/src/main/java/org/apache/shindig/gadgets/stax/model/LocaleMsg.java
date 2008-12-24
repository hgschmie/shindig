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

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.gadgets.spec.SpecParserException;

public class LocaleMsg extends SpecElement {

  public static final String ELEMENT_NAME = "msg";

  public static final String ATTR_NAME = "name";

  /** Non 0.8 in gadgetspec, present in message bundle */
  public static final String ATTR_DESC = "name";

  private StringBuilder text = new StringBuilder();

  public LocaleMsg(final QName name, final Map<String, QName> attrNames) {
    super(name, attrNames);
  }

  public String getName() {
    return attrDefault(ATTR_NAME);
  }

  public String getDesc() {
    return attrDefault(ATTR_DESC);
  }

  @Override
  public String getText() {
    return text.toString();
  }

  private void addText(final String text) {
    this.text.append(text);
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (attr(ATTR_NAME) != null) {
      writer.writeAttribute(namespaceURI, ATTR_NAME, getName());
    }

    if (attr(ATTR_DESC) != null) {
      writer.writeAttribute(namespaceURI, ATTR_DESC, getDesc());
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (attr(ATTR_NAME) == null) {
      throw new SpecParserException(name().getLocalPart()
          + "@name must be set!");
    }
  }

  public static class Parser extends SpecElement.Parser<LocaleMsg> {

    public Parser() {
      this(new QName(ELEMENT_NAME));
    }

    public Parser(final QName name) {
      super(name);
      register(ATTR_NAME, ATTR_DESC);
    }

    @Override
    protected LocaleMsg newElement() {
      return new LocaleMsg(name(), getAttrNames());
    }

    @Override
    protected void addText(final XMLStreamReader reader, final LocaleMsg msg) {
      if (!reader.isWhiteSpace()) {
        msg.addText(reader.getText());
      }
    }
  }
}
