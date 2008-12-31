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
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.variables.Substitutions;

public class LinkSpec extends SpecElement {

  public static final String ELEMENT_NAME = "Link";

  public static final String ATTR_REL = "rel";
  public static final String ATTR_HREF = "href";

  public LinkSpec(final QName name, final Map<String, QName> attrNames,
      final Uri base) {
    super(name, attrNames, base);
  }

  protected LinkSpec(final LinkSpec linkSpec, final Substitutions substituter) {
    super(linkSpec, substituter);
    setAttr(ATTR_HREF, getBase().resolve(
        substituter.substituteUri(linkSpec.getHref())).toString());
    setAttr(ATTR_REL, substituter.substituteString(linkSpec.getRel()));
  }

  @Override
  public LinkSpec substitute(final Substitutions substituter) {
    return new LinkSpec(this, substituter);
  }

  public String getRel() {
    return attrDefault(ATTR_REL);
  }

  public Uri getHref() {
    return attrUriNull(ATTR_HREF);
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (attr(ATTR_REL) != null) {
      writer.writeAttribute(namespaceURI, ATTR_REL, getRel());
    }
    if (getHref() != null) {
      writer.writeAttribute(namespaceURI, ATTR_HREF, getHref().toString());
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (attr(ATTR_REL) == null) {
      throw new SpecParserException(name().getLocalPart() + "@rel must be set!");
    }
    if (getHref() == null) {
      throw new SpecParserException(name().getLocalPart()
          + "@href must be set!");
    }
  }

  public static class Parser extends SpecElement.Parser<LinkSpec> {

    public Parser(final Uri base) {
      this(new QName(ELEMENT_NAME), base);
    }

    public Parser(final QName name, final Uri base) {
      super(name, base);
      register(ATTR_REL, ATTR_HREF);
    }

    @Override
    protected LinkSpec newElement() {
      return new LinkSpec(name(), getAttrNames(), getBase());
    }
  }
}
