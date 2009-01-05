package org.apache.shindig.gadgets.spec;

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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.variables.Substitutions;

public class LocaleMsg extends SpecElement {

  public static final QName ELEMENT_NAME = new QName(SpecElement.GADGET_SPEC_NAMESPACE_URI, "msg");

  public static final String ATTR_NAME = "name";
  public static final String ATTR_DESC = "desc";

  private StringBuilder text = new StringBuilder();

  protected LocaleMsg(final QName name, final Map<String, QName> attrNames, final Uri base) {
    super(name, attrNames, base);
  }

  protected LocaleMsg(final LocaleMsg localeMsg, final Substitutions substituter) {
      super(localeMsg, substituter);
  }

  @Override
  public LocaleMsg substitute(final Substitutions substituter) {
    return new LocaleMsg(this, substituter);
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
    if (attr(ATTR_NAME) != null) {
      writeAttribute(writer, ATTR_NAME, getName());
    }

    if (attr(ATTR_DESC) != null) {
      writeAttribute(writer, ATTR_DESC, getDesc());
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (attr(ATTR_NAME) == null) {
      throw new SpecParserException(name().getLocalPart()
          + "@name must be set!");
    }
  }

  @Override
  public boolean equals(final Object other) {
    if (other instanceof LocaleMsg == false) {
      return false;
    }
    if (this == other) {
      return true;
    }
    LocaleMsg rhs = (LocaleMsg) other;
    return new EqualsBuilder()
                  .appendSuper(super.equals(other))
                  .append(getName(), rhs.getName())
                  .append(getText(), rhs.getText())
                  .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
      .appendSuper(super.hashCode())
      .append(getName())
      .append(getText())
      .toHashCode();
  }

  public static class Parser extends SpecElement.Parser<LocaleMsg> {

    public Parser(final Uri base) {
      this(ELEMENT_NAME, base);
    }
    
    public Parser(final QName parent, final QName child, final Uri base) {
      this(buildChildName(parent, child, ELEMENT_NAME), base);
    }

    public Parser(final QName name, final Uri base) {
      super(name, base);
      register(ATTR_NAME, ATTR_DESC);
    }

    @Override
    protected LocaleMsg newElement() {
      return new LocaleMsg(name(), getAttrNames(), getBase());
    }

    @Override
    protected void addText(final XMLStreamReader reader, final LocaleMsg msg) {
      if (!reader.isWhiteSpace()) {
        msg.addText(reader.getText());
      }
    }
  }
}
