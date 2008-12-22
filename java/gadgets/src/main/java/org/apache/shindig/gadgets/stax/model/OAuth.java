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

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.shindig.gadgets.spec.SpecParserException;

public class OAuth extends SpecElement {

  public static final String ELEMENT_NAME = "OAuth";

  private static final String ATTR_NAME = "name";

  private String name = null;

  private List<OAuthService> oAuthServices = new ArrayList<OAuthService>();

  public OAuth(final QName name) {
    super(name);
  }

  public String getName() {
    return StringUtils.defaultString(name);
  }

  public List<OAuthService> getOAuthServices() {
    return oAuthServices;
  }

  private void setName(final String name) {
    this.name = name;
  }

  private void addOAuthService(final OAuthService oAuthService) {
    this.oAuthServices.add(oAuthService);
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (name != null) {
      writer.writeAttribute(namespaceURI, ATTR_NAME, getName());
    }
  }

  @Override
  protected void writeChildren(final XMLStreamWriter writer)
      throws XMLStreamException {
    for (OAuthService oAuthService : oAuthServices) {
      oAuthService.toXml(writer);
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (oAuthServices.size() == 0) {
      throw new SpecParserException(name().getLocalPart()
          + " must contain at least one 'service' element!");
    }
  }

  public static class Parser extends SpecElement.Parser<OAuth> {

    private final QName attrName;

    public Parser() {
      this(new QName(ELEMENT_NAME));
    }

    public Parser(final QName name) {
      super(name);
      register(new OAuthService.Parser());
      this.attrName = buildQName(name, ATTR_NAME);
    }

    @Override
    protected OAuth newElement() {
      return new OAuth(getName());
    }

    @Override
    protected void setAttribute(final OAuth oAuth, final QName name,
        final String value) {
      if (name.equals(attrName)) {
        oAuth.setName(value);
      } else {
        super.setAttribute(oAuth, name, value);
      }
    }

    @Override
    protected void addChild(XMLStreamReader reader, final OAuth oAuth,
        final SpecElement child) {
      if (child instanceof OAuthService) {
        oAuth.addOAuthService((OAuthService) child);
      } else {
        super.addChild(reader, oAuth, child);
      }
    }
  }
}
