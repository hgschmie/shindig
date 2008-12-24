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

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.spec.SpecParserException;

public class OAuthAuthorization extends SpecElement {

  public static final String ELEMENT_NAME = "Authorization";

  public static final String ATTR_URL = "url";

  public OAuthAuthorization(final QName name, final Map<String, QName> attrNames) {
    super(name, attrNames);
  }

  public Uri getUrl() {
    return attrUriNull(ATTR_URL);
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (getUrl() != null) {
      writer.writeAttribute(namespaceURI, ATTR_URL, getUrl().toString());
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (getUrl() == null) {
      throw new SpecParserException(name().getLocalPart()
          + "@url must not be empty!");
    }
  }

  public static class Parser extends SpecElement.Parser<OAuthAuthorization> {

    public Parser() {
      this(new QName(ELEMENT_NAME));
    }

    public Parser(final QName name) {
      super(name);
      register(ATTR_URL);
    }

    @Override
    protected OAuthAuthorization newElement() {
      return new OAuthAuthorization(name(), getAttrNames());
    }
  }
}
