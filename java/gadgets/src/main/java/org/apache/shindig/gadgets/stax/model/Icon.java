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
import org.apache.shindig.gadgets.spec.SpecParserException;

public class Icon extends SpecElement {

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

  public String getType() {
    return StringUtils.defaultString(type);
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
  protected void writeAttributes(final XMLStreamWriter writer) throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (mode != null) {
      writer.writeAttribute(namespaceURI, ATTR_MODE, getMode());
    }
    if (type != null) {
      writer.writeAttribute(namespaceURI, ATTR_TYPE, getType());
    }
  }

  @Override
  protected void writeChildren(final XMLStreamWriter writer) throws XMLStreamException {
    if (StringUtils.isNotEmpty(text)) {
      writer.writeCharacters(text);
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (mode != null && !mode.equals("base64")) {
      throw new SpecParserException("All msg elements must have a name attribute.");
    }
  }

  public static class Parser extends SpecElement.Parser<Icon> {

    private final QName attrMode;
    private final QName attrType;

    public Parser() {
      this(new QName("icon"));
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
    protected void setAttribute(final Icon icon, final QName name, final String value) {
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
