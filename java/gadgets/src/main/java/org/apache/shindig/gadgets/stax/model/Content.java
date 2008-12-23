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
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.StaxUtils;

public class Content extends SpecElement {

  public static final String ELEMENT_NAME = "Content";

  private static final String ATTR_TYPE = "type";
  private static final String ATTR_HREF = "href";
  private static final String ATTR_VIEW = "view";
  private static final String ATTR_PREFERRED_HEIGHT = "preferred_height";
  private static final String ATTR_PREFERRED_WIDTH = "preferred_width";

  private String type;
  private String href;
  private String view;
  private String preferredHeight;
  private String preferredWidth;

  private StringBuilder text = new StringBuilder();

  public Content(final QName name) {
    super(name);
  }

  public Type getType() {
    return Type.parse(type);
  }

  public Uri getHref() {
    return StaxUtils.toUri(href);
  }

  public String getView() {
    return StringUtils.defaultString(view);
  }

  public int getPreferredHeight() {
    return NumberUtils.toInt(preferredHeight);
  }

  public int getPreferredWidth() {
    return NumberUtils.toInt(preferredWidth);
  }

  public String getText() {
    return text.toString();
  }

  private void setType(final String type) {
    this.type = type;
  }

  private void setHref(final String href) {
    this.href = href;
  }

  private void setView(final String view) {
    this.view = view;
  }

  private void setPreferredHeight(final String preferredHeight) {
    this.preferredHeight = preferredHeight;
  }

  private void setPreferredWidth(final String preferredWidth) {
    this.preferredWidth = preferredWidth;
  }

  private void addText(final String text) {
    this.text.append(text);
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (type != null) {
      writer.writeAttribute(namespaceURI, ATTR_TYPE, getType().toString());
    }
    if (href != null) {
      writer.writeAttribute(namespaceURI, ATTR_HREF, getHref().toString());
    }
    if (view != null) {
      writer.writeAttribute(namespaceURI, ATTR_VIEW, getView());
    }
    if (preferredHeight != null) {
      writer.writeAttribute(namespaceURI, ATTR_PREFERRED_HEIGHT, String
          .valueOf(getPreferredHeight()));
    }
    if (preferredWidth != null) {
      writer.writeAttribute(namespaceURI, ATTR_PREFERRED_WIDTH, String
          .valueOf(getPreferredWidth()));
    }
  }

  @Override
  public void validate() throws SpecParserException {
    switch (getType()) {
    case HTML:
    case HTML_INLINE:
      if (text == null) {
        throw new SpecParserException(name().getLocalPart()
            + " body required for type='html'!");
      }
      break;
    case URL:
      if (href == null) {
        throw new SpecParserException(name().getLocalPart()
            + "@href required for type='url'!");
      }
      break;
    default:
      throw new SpecParserException(
          "Unknown type for Content@type encountered: " + getType());
    }
  }

  /**
   * Possible values for Content@type
   */
  public static enum Type {
    HTML("html"), URL("url"), HTML_INLINE("html-inline");

    private final String value;

    private Type(final String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return getValue();
    }

    /**
     * Parses a data type from the input string.
     *
     * @param value
     * @return The data type of the given value.
     */
    public static Type parse(String value) {
      for (Type type : Type.values()) {
        if (type.getValue().compareToIgnoreCase(value) == 0) {
          return type;
        }
      }
      return HTML;
    }
  }

  public static class Parser extends SpecElement.Parser<Content> {
    private final QName attrType;
    private final QName attrHref;
    private final QName attrView;
    private final QName attrPreferredHeight;
    private final QName attrPreferredWidth;

    public Parser() {
      this(new QName(ELEMENT_NAME));
    }

    public Parser(final QName name) {
      super(name);
      this.attrType = buildQName(name, ATTR_TYPE);
      this.attrHref = buildQName(name, ATTR_HREF);
      this.attrView = buildQName(name, ATTR_VIEW);
      this.attrPreferredHeight = buildQName(name, ATTR_PREFERRED_HEIGHT);
      this.attrPreferredWidth = buildQName(name, ATTR_PREFERRED_WIDTH);
    }

    @Override
    protected Content newElement() {
      return new Content(getName());
    }

    @Override
    protected void setAttribute(final Content content, final QName name,
        final String value) {
      if (name.equals(attrType)) {
        content.setType(value);
      } else if (name.equals(attrHref)) {
        content.setHref(value);
      } else if (name.equals(attrView)) {
        content.setView(value);
      } else if (name.equals(attrPreferredHeight)) {
        content.setPreferredHeight(value);
      } else if (name.equals(attrPreferredWidth)) {
        content.setPreferredWidth(value);
      } else {
        super.setAttribute(content, name, value);
      }
    }

    @Override
    protected void addText(final XMLStreamReader reader, final Content content) {
      if (!reader.isWhiteSpace()) {
        content.addText(reader.getText());
      }
    }
  }
}
