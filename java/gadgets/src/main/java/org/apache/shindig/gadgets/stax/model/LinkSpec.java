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
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.StaxUtils;

public class LinkSpec extends SpecElement {

  private static final String ATTR_REL = "rel";
  private static final String ATTR_HREF = "href";

  public LinkSpec(final QName name) {
    super(name);
  }

  private String rel = null;
  private String href = null;

  public String getRel() {
    return StringUtils.defaultString(rel);
  }

  public Uri getHref() {
    return StaxUtils.toUri(href);
  }

  private void setRel(final String rel) {
    this.rel = rel;
  }

  private void setHref(final String href) {
    this.href = href;
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer) throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (rel != null) {
      writer.writeAttribute(namespaceURI, ATTR_REL, getRel());
    }
    if (href != null) {
      writer.writeAttribute(namespaceURI, ATTR_HREF, getHref().toString());
    }
  }

  @Override
  public void validate() throws SpecParserException {
  }

  public static class Parser extends SpecElement.Parser<LinkSpec> {

    private final QName attrRel;
    private final QName attrHref;

    public Parser() {
      this(new QName("Link"));
    }

    public Parser(final QName name) {
      super(name);
      attrRel = buildQName(name, ATTR_REL);
      attrHref = buildQName(name, ATTR_HREF);
    }

    @Override
    protected LinkSpec newElement() {
      return new LinkSpec(getName());
    }

    @Override
    protected void setAttribute(final LinkSpec link, final QName name, final String value) {
      if (name.equals(attrRel)) {
        link.setRel(value);
      } else if (name.equals(attrHref)) {
        link.setHref(value);
      } else {
        super.setAttribute(link, name, value);
      }
    }
  }
}
