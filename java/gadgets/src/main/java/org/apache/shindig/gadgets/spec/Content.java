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

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.AuthType;
import org.apache.shindig.gadgets.variables.Substitutions;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class Content extends SpecElement {

  public static final QName ELEMENT_NAME = new QName(SpecElement.GADGET_SPEC_NAMESPACE_URI, "Content");

  public static final String ATTR_TYPE = "type";
  public static final String ATTR_HREF = "href";
  public static final String ATTR_VIEW = "view";
  public static final String ATTR_PREFERRED_HEIGHT = "preferred_height";
  public static final String ATTR_PREFERRED_WIDTH = "preferred_width";

  // non 0.8 attributes!

  public static final String ATTR_AUTHZ = "authz";
  public static final String ATTR_QUIRKS = "quirks";
  public static final String ATTR_SIGN_OWNER = "sign_owner";
  public static final String ATTR_SIGN_VIEWER = "sign_viewer";

  private StringBuilder text = new StringBuilder();

  protected Content(final QName name, final Map<String, QName> attrNames, final Uri base) {
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

  public AuthType getAuthType() {
    return AuthType.parse(attr(ATTR_AUTHZ));
  }

  public boolean isQuirks() {
    return attrBool(ATTR_QUIRKS);
  }

  public boolean isSignOwner() {
    return attrBool(ATTR_SIGN_OWNER, true);
  }

  public boolean isSignViewer() {
    return attrBool(ATTR_SIGN_VIEWER, true);
  }

  @Override
  public String getText() {
    return text.toString();
  }

  public Set<String> getViews() {
    return ImmutableSet.of(StringUtils.stripAll(StringUtils.split(
        StringUtils.defaultString(attr(ATTR_VIEW), "default"), ',')));
  }

  private void addText(final String text) {
    this.text.append(text);
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer) throws XMLStreamException {
    if (attr(ATTR_TYPE) != null) {
      writeAttribute(writer, ATTR_TYPE, getRawType());
    }
    if (getHref() != null) {
      writeAttribute(writer, ATTR_HREF, getHref().toString());
    }
    if (attr(ATTR_VIEW) != null) {
      writeAttribute(writer, ATTR_VIEW, StringUtils.join(getViews(), ','));
    }
    if (attr(ATTR_PREFERRED_HEIGHT) != null) {
      writeAttribute(writer, ATTR_PREFERRED_HEIGHT, String.valueOf(getPreferredHeight()));
    }
    if (attr(ATTR_PREFERRED_WIDTH) != null) {
      writeAttribute(writer, ATTR_PREFERRED_WIDTH, String.valueOf(getPreferredWidth()));
    }

    if (attr(ATTR_AUTHZ) != null) {
      writeAttribute(writer, ATTR_AUTHZ, getAuthType().toString());
    }

    if (attr(ATTR_QUIRKS) != null) {
      writeAttribute(writer, ATTR_QUIRKS, String.valueOf(isQuirks()));
    }

    if (attr(ATTR_SIGN_OWNER) != null) {
      writeAttribute(writer, ATTR_SIGN_OWNER, String.valueOf(isSignOwner()));
    }

    if (attr(ATTR_SIGN_VIEWER) != null) {
      writeAttribute(writer, ATTR_SIGN_VIEWER, String.valueOf(isSignViewer()));
    }
  }

  @Override
  public void validate() throws SpecParserException {

    if (attr(ATTR_TYPE) != null && getType() == null) {
      throw new SpecParserException(this, "@type value '" + attr(ATTR_TYPE) + "' is unknown!");
    }

    if (attr(ATTR_AUTHZ) != null && getAuthType() == null) {
      throw new SpecParserException(this, "@authz value '" + attr(ATTR_AUTHZ) + "' is unknown!");
    }

    switch (getType()) {
      case HTML:
        if (text == null) {
          throw new SpecParserException(this, " body required for type='html'!");
        }
        break;
      case URL:
        if (getHref() == null) {
          throw new SpecParserException(this, "@href required for type='url'!");
        }
        break;
      default:
        throw new SpecParserException(this, "Unknown type for Content@type encountered: " + getType());
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
    return new EqualsBuilder().append(getType(), rhs.getType()).append(getHref(), rhs.getHref()).append(getViews(),
        rhs.getViews()).append(getText(), rhs.getText()).append(getAuthType(), rhs.getAuthType()).append(isQuirks(),
        rhs.isQuirks()).append(isSignOwner(), rhs.isSignOwner()).append(isSignViewer(), rhs.isSignViewer()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(getType()).append(getHref()).append(getViews()).append(getText()).append(
        getAuthType()).append(isQuirks()).append(isSignOwner()).append(isSignViewer()).toHashCode();
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

      if (value != null) {
        for (Type type : Type.values()) {
          if (StringUtils.equalsIgnoreCase(type.name(), StringUtils.trimToEmpty(value))) {
            return type;
          }
        }
      }
      return HTML; // Default type for unknown content types.
    }
  }

  public static class Parser extends SpecElement.Parser<Content> {

    public Parser(final Uri base) {
      this(ELEMENT_NAME, base);
    }

    public Parser(final QName parent, final QName child, final Uri base) {
      this(buildChildName(parent, child, ELEMENT_NAME), base);
    }

    public Parser(final QName name, final Uri base) {
      super(name, base);
      register(ATTR_TYPE, ATTR_HREF, ATTR_VIEW, ATTR_PREFERRED_HEIGHT, ATTR_PREFERRED_WIDTH, ATTR_AUTHZ, ATTR_QUIRKS,
          ATTR_SIGN_OWNER, ATTR_SIGN_VIEWER);
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
