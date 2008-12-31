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

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.variables.Substitutions;

public class EnumValue extends SpecElement {
  public static final QName ELEMENT_NAME = new QName(SpecElement.OPENSOCIAL_NAMESPACE_URI, "EnumValue");

  public static final String ATTR_VALUE = "value";
  public static final String ATTR_DISPLAY_VALUE = "display_value";

  protected EnumValue(final QName name, final Map<String, QName> attrNames,
      final Uri base) {
    super(name, attrNames, base);
  }

  protected EnumValue(final EnumValue enumValue, final Substitutions substituter) {
    super(enumValue, substituter);
    setAttr(ATTR_VALUE, substituter.substituteString(enumValue.getValue()));
    setAttr(ATTR_DISPLAY_VALUE, substituter.substituteString(enumValue
        .getDisplayValue()));
  }

  @Override
  public EnumValue substitute(final Substitutions substituter) {
    return new EnumValue(this, substituter);
  }

  public String getValue() {
    return attr(ATTR_VALUE);
  }

  public String getDisplayValue() {
    return attrDefault(ATTR_DISPLAY_VALUE, getValue());
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    if (attr(ATTR_VALUE) != null) {
      writeAttribute(writer, ATTR_VALUE, getValue());
    }
    if (attr(ATTR_DISPLAY_VALUE) != null) {
      writeAttribute(writer, ATTR_DISPLAY_VALUE, getDisplayValue());
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (attr(ATTR_VALUE) == null) {
      throw new SpecParserException(name().getLocalPart()
          + "@value must be set!");
    }
  }

  public static class Parser extends SpecElement.Parser<EnumValue> {

    public Parser(final Uri base) {
      this(ELEMENT_NAME, base);
    }

    public Parser(final QName name, final Uri base) {
      super(name, base);
      register(ATTR_VALUE, ATTR_DISPLAY_VALUE);
    }

    @Override
    protected EnumValue newElement() {
      return new EnumValue(name(), getAttrNames(), getBase());
    }
  }
}
