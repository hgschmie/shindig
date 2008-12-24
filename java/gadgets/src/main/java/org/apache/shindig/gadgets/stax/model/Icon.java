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

import org.apache.commons.lang.StringUtils;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.variables.Substitutions;

public class Icon extends SpecElement {

  public static final String ELEMENT_NAME = "Icon";

  public static final String ATTR_MODE = "mode";
  public static final String ATTR_TYPE = "type";

  private StringBuilder text = new StringBuilder();

  public Icon(final QName name, final Map<String, QName> attrNames,
      final Uri base) {
    super(name, attrNames, base);
  }

  protected Icon(final Icon icon, final Substitutions substituter) {
    super(icon);
    addText(substituter.substituteString(icon.getText()));
  }

  public Icon substitute(final Substitutions substituter) {
    return new Icon(this, substituter);
  }

  public String getMode() {
    return attrDefault(ATTR_MODE);
  }

  public Type getType() {
    return Type.parse(attr(ATTR_TYPE));
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

    if (attr(ATTR_MODE) != null) {
      writer.writeAttribute(namespaceURI, ATTR_MODE, getMode());
    }
    if (attr(ATTR_TYPE) != null) {
      writer.writeAttribute(namespaceURI, ATTR_TYPE, getType().toString());
    }
  }

  public static enum Type {
    BASE64;

    public static Type parse(String value) {
      for (Type type : Type.values()) {
        if (StringUtils.equalsIgnoreCase(type.toString(), value)) {
          return type;
        }
      }
      return BASE64;
    }
  }

  public static class Parser extends SpecElement.Parser<Icon> {

    public Parser(final Uri base) {
      this(new QName(ELEMENT_NAME), base);
    }

    public Parser(final QName name, final Uri base) {
      super(name, base);
      register(ATTR_MODE, ATTR_TYPE);
    }

    @Override
    protected Icon newElement() {
      return new Icon(name(), getAttrNames(), getBase());
    }

    @Override
    protected void addText(final XMLStreamReader reader, final Icon icon) {
      if (!reader.isWhiteSpace()) {
        icon.addText(reader.getText());
      }
    }
  }
}
