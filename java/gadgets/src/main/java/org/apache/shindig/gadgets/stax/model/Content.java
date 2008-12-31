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
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.variables.Substitutions;

import com.google.common.collect.ImmutableSet;

public class Content extends SpecElement {

  public static final String ELEMENT_NAME = "Content";

  public static final String ATTR_TYPE = "type";
  public static final String ATTR_HREF = "href";
  public static final String ATTR_VIEW = "view";
  public static final String ATTR_PREFERRED_HEIGHT = "preferred_height";
  public static final String ATTR_PREFERRED_WIDTH = "preferred_width";

  private StringBuilder text = new StringBuilder();

  public Content(final QName name, final Map<String, QName> attrNames,
      final Uri base) {
    super(name, attrNames, base);
  }

  protected Content(final Content content, final Substitutions substituter) {
    super(content, substituter);
    this.addText(substituter.substituteString(content.getText()));
  }

  @Override
  public Content substitute(final Substitutions substitutions) {
    return new Content(this, substitutions);
  }

  public Type getType() {
    return Type.parse(attr(ATTR_TYPE));
  }

  public String getRawType() {
    return attr(ATTR_TYPE);
  }

  public Uri getHref() {
    return attrUriNull(ATTR_HREF);
  }

  public int getPreferredHeight() {
    return attrInt(ATTR_PREFERRED_HEIGHT, -1);
  }

  public int getPreferredWidth() {
    return attrInt(ATTR_PREFERRED_WIDTH, -1);
  }

  @Override
  public String getText() {
    return text.toString();
  }

  public Set<String> getViews() {
    return ImmutableSet.of(StringUtils.stripAll(StringUtils.split(StringUtils
        .defaultString(attr(ATTR_VIEW), "default"), ',')));
  }

  private void addText(final String text) {
    this.text.append(text);
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (attr(ATTR_TYPE) != null) {
      writer.writeAttribute(namespaceURI, ATTR_TYPE, getType().toString());
    }
    if (getHref() != null) {
      writer.writeAttribute(namespaceURI, ATTR_HREF, getHref().toString());
    }
    if (attr(ATTR_VIEW) != null) {
      writer.writeAttribute(namespaceURI, ATTR_VIEW, StringUtils.join(
          getViews(), ','));
    }
    if (attr(ATTR_PREFERRED_HEIGHT) != null) {
      writer.writeAttribute(namespaceURI, ATTR_PREFERRED_HEIGHT, String
          .valueOf(getPreferredHeight()));
    }
    if (attr(ATTR_PREFERRED_WIDTH) != null) {
      writer.writeAttribute(namespaceURI, ATTR_PREFERRED_WIDTH, String
          .valueOf(getPreferredWidth()));
    }
  }

  @Override
  public void validate() throws SpecParserException {
    switch (getType()) {
    case HTML:
      if (text == null) {
        throw new SpecParserException(name().getLocalPart()
            + " body required for type='html'!");
      }
      break;
    case URL:
      if (getHref() == null) {
        throw new SpecParserException(name().getLocalPart()
            + "@href required for type='url'!");
      }
      break;
    default:
      throw new SpecParserException(
          "Unknown type for Content@type encountered: " + getType());
    }
  }

  @Override
  public boolean equals(final Object other) {
    if (other instanceof Content == false) {
      return false;
    }
    if (this == other) {
      return true;
    }
    Content rhs = (Content) other;
    return new EqualsBuilder()
                  .append(getType(), rhs.getType())
                  .append(getHref(), rhs.getHref())
                  .append(getViews(), rhs.getViews())
                  .append(getText(), rhs.getText())
                  .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
    .append(getType())
    .append(getHref())
    .append(getViews())
    .append(getText())
    .toHashCode();
  }


  /**
   * Possible values for Content@type
   */
  public static enum Type {
    HTML, URL;

    @Override
    public String toString() {
      return name().toLowerCase();
    }

    /**
     * Parses a data type from the input string.
     *
     * @param value
     * @return The data type of the given value.
     */
    public static Type parse(String value) {
      for (Type type : Type.values()) {
        if (StringUtils.equalsIgnoreCase(type.name(), value)) {
          return type;
        }
      }
      return HTML;
    }
  }

  public static class Parser extends SpecElement.Parser<Content> {

    public Parser(final Uri base) {
      this(new QName(ELEMENT_NAME), base);
    }

    public Parser(final QName name, final Uri base) {
      super(name, base);
      register(ATTR_TYPE, ATTR_HREF, ATTR_VIEW, ATTR_PREFERRED_HEIGHT,
          ATTR_PREFERRED_WIDTH);
    }

    @Override
    protected Content newElement() {
      return new Content(name(), getAttrNames(), getBase());
    }

    @Override
    protected void addText(final XMLStreamReader reader, final Content content) {
      if (!reader.isWhiteSpace()) {
        content.addText(reader.getText());
      }
    }
  }
}
