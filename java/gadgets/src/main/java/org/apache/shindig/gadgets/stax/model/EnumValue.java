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

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.shindig.gadgets.spec.SpecParserException;

public class EnumValue extends SpecElement {
  public static final String ELEMENT_NAME = "EnumValue";

  private static final String ATTR_VALUE = "value";
  private static final String ATTR_DISPLAY_VALUE = "display_value";

  private String value = null;
  private String displayValue = null;

  public EnumValue(final QName name) {
    super(name);
  }

  public String getValue() {
    return StringUtils.defaultString(value);
  }

  public String getDisplayValue() {
    return StringUtils.defaultString(displayValue);
  }

  private void setValue(final String value) {
    this.value = value;
  }

  private void setDisplayValue(final String displayValue) {
    this.displayValue = displayValue;
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (value != null) {
      writer.writeAttribute(namespaceURI, ATTR_VALUE, getValue());
    }
    if (displayValue != null) {
      writer
          .writeAttribute(namespaceURI, ATTR_DISPLAY_VALUE, getDisplayValue());
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (value == null) {
      throw new SpecParserException(name().getLocalPart()
          + "@value must be set!");
    }
  }

  public static class Parser extends SpecElement.Parser<EnumValue> {

    private final QName attrValue;
    private final QName attrDisplayValue;

    public Parser() {
      this(new QName(ELEMENT_NAME));
    }

    public Parser(final QName name) {
      super(name);
      this.attrValue = buildQName(name, ATTR_VALUE);
      this.attrDisplayValue = buildQName(name, ATTR_DISPLAY_VALUE);
    }

    @Override
    protected EnumValue newElement() {
      return new EnumValue(getName());
    }

    @Override
    protected void setAttribute(final EnumValue enumValue, final QName name,
        final String value) {
      if (name.equals(attrValue)) {
        enumValue.setValue(value);
      } else if (name.equals(attrDisplayValue)) {
        enumValue.setDisplayValue(value);
      } else {
        super.setAttribute(enumValue, name, value);
      }
    }
  }
}
