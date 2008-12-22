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

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;

public class Icon extends SpecElement {

  public static final String ELEMENT_NAME = "Icon";

  private static final String ATTR_MODE = "mode";
  private static final String ATTR_TYPE = "type";

  private String mode = null;
  private String type = null;

  private String text = "";

  public Icon(final QName name) {
    super(name);
  }

  public String getMode() {
    return StringUtils.defaultString(mode);
  }

  public Type getType() {
    return Type.parse(type);
  }

  public String getText() {
    return text;
  }

  private void setMode(final String mode) {
    this.mode = mode;
  }

  private void setType(final String type) {
    this.type = type;
  }

  private void setText(final String text) {
    this.text = StringUtils.trim(text);
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (mode != null) {
      writer.writeAttribute(namespaceURI, ATTR_MODE, getMode());
    }
    if (type != null) {
      writer.writeAttribute(namespaceURI, ATTR_TYPE, getType().toString());
    }
  }

  @Override
  protected void writeChildren(final XMLStreamWriter writer)
      throws XMLStreamException {
    if (StringUtils.isNotEmpty(text)) {
      writer.writeCharacters(text);
    }
  }

  public static enum Type {
    BASE64;

    public static Type parse(String value) {
      for (Type type : Type.values()) {
        if (type.toString().compareToIgnoreCase(value) == 0) {
          return type;
        }
      }
      return BASE64;
    }
  }

  public static class Parser extends SpecElement.Parser<Icon> {

    private final QName attrMode;
    private final QName attrType;

    public Parser() {
      this(new QName(ELEMENT_NAME));
    }

    public Parser(final QName name) {
      super(name);
      this.attrMode = buildQName(name, ATTR_MODE);
      this.attrType = buildQName(name, ATTR_TYPE);
    }

    @Override
    protected Icon newElement() {
      return new Icon(getName());
    }

    @Override
    protected void setAttribute(final Icon icon, final QName name,
        final String value) {
      if (name.equals(attrMode)) {
        icon.setMode(value);
      } else if (name.equals(attrType)) {
        icon.setType(value);
      } else {
        super.setAttribute(icon, name, value);
      }
    }

    @Override
    protected void setText(final Icon icon, final String value) {
      icon.setText(value);
    }
  }
}
