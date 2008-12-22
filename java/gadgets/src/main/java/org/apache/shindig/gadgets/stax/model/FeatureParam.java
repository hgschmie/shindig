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

public class FeatureParam extends SpecElement {

  public static final String ELEMENT_NAME = "Param";

  private static final String ATTR_NAME = "name";

  private String name = null;

  private String text = "";

  public FeatureParam(final QName name) {
    super(name);
  }

  public String getName() {
    return StringUtils.defaultString(name);
  }

  public String getText() {
    return text;
  }

  private void setName(final String name) {
    this.name = name;
  }

  private void setText(final String text) {
    this.text = StringUtils.trim(text);
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (name != null) {
      writer.writeAttribute(namespaceURI, ATTR_NAME, getName());
    }
  }

  @Override
  protected void writeChildren(final XMLStreamWriter writer)
      throws XMLStreamException {
    if (StringUtils.isNotEmpty(text)) {
      writer.writeCharacters(text);
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (name == null) {
      throw new SpecParserException(name().getLocalPart()
          + "@name must be set!");
    }
  }

  public static class Parser extends SpecElement.Parser<FeatureParam> {

    private final QName attrName;

    public Parser() {
      this(new QName(ELEMENT_NAME));
    }

    public Parser(final QName name) {
      super(name);
      this.attrName = buildQName(name, ATTR_NAME);
    }

    @Override
    protected FeatureParam newElement() {
      return new FeatureParam(getName());
    }

    @Override
    protected void setAttribute(final FeatureParam msg, final QName name,
        final String value) {
      if (name.equals(attrName)) {
        msg.setName(value);
      } else {
        super.setAttribute(msg, name, value);
      }
    }

    @Override
    protected void setText(final FeatureParam msg, final String value) {
      msg.setText(value);
    }
  }
}
