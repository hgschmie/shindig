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

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.StaxUtils;

public class OAuthAuthorization extends SpecElement {

  public static final String ELEMENT_NAME = "Authorization";
  private static final String ATTR_URL = "url";

  private String url = null;

  public OAuthAuthorization(final QName name) {
    super(name);
  }

  public Uri getUrl() {
    return StaxUtils.toUri(url);
  }

  private void setUrl(final String url) {
    this.url = url;
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (url != null) {
      writer.writeAttribute(namespaceURI, ATTR_URL, getUrl().toString());
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (url == null) {
      throw new SpecParserException(name().getLocalPart()
          + "@url must not be empty!");
    }
  }

  public static class Parser extends SpecElement.Parser<OAuthAuthorization> {

    private final QName attrUrl;

    public Parser() {
      this(new QName(ELEMENT_NAME));
    }

    public Parser(final QName name) {
      super(name);
      this.attrUrl = buildQName(name, ATTR_URL);
    }

    @Override
    protected OAuthAuthorization newElement() {
      return new OAuthAuthorization(getName());
    }

    @Override
    protected void setAttribute(final OAuthAuthorization oAuthAuthorization,
        final QName name, final String value) {
      if (name.equals(attrUrl)) {
        oAuthAuthorization.setUrl(value);
      } else {
        super.setAttribute(oAuthAuthorization, name, value);
      }
    }
  }
}
