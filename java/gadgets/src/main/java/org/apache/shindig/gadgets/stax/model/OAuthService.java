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
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.variables.Substitutions;

public class OAuthService extends SpecElement {

  public static final String ELEMENT_NAME = "Service";

  public static final String ATTR_NAME = "name";

  private OAuthRequest oAuthRequest = null;
  private OAuthAccess oAuthAccess = null;
  private OAuthAuthorization oAuthAuthorization = null;

  public OAuthService(final QName name, final Map<String, QName> attrNames,
      final Uri base) {
    super(name, attrNames, base);
  }

  protected OAuthService(final OAuthService oAuthService, final Substitutions substituter) {
      super(oAuthService);
  }

  @Override
  public OAuthService substitute(final Substitutions substituter) {
    return new OAuthService(this, substituter);
  }

  public String getName() {
    return attrDefault(ATTR_NAME);
  }

  public OAuthRequest getRequest() {
    return oAuthRequest;
  }

  public OAuthAccess getAccess() {
    return oAuthAccess;
  }

  public OAuthAuthorization getAuthorization() {
    return oAuthAuthorization;
  }

  private void setOAuthRequest(final OAuthRequest oAuthRequest) {
    this.oAuthRequest = oAuthRequest;
  }

  private void setOAuthAccess(final OAuthAccess oAuthAccess) {
    this.oAuthAccess = oAuthAccess;
  }

  private void setOAuthAuthorization(final OAuthAuthorization oAuthAuthorization) {
    this.oAuthAuthorization = oAuthAuthorization;
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (attr(ATTR_NAME) != null) {
      writer.writeAttribute(namespaceURI, ATTR_NAME, getName());
    }
  }

  @Override
  protected void writeChildren(final XMLStreamWriter writer)
      throws XMLStreamException {
    if (oAuthRequest != null) {
      oAuthRequest.toXml(writer);
    }
    if (oAuthAccess != null) {
      oAuthAccess.toXml(writer);
    }
    if (oAuthAuthorization != null) {
      oAuthAuthorization.toXml(writer);
    }
  }

  @Override
  public void validate() throws SpecParserException {

    if (attr(ATTR_NAME) == null) {
      throw new SpecParserException(name().getLocalPart()
          + "@name must be set!");
    }

    if (oAuthRequest == null) {
      throw new SpecParserException(name().getLocalPart()
          + " must contain a 'Request' element!");
    }
    if (oAuthAccess == null) {
      throw new SpecParserException(name().getLocalPart()
          + " must contain an 'Access' element!");
    }
    if (oAuthAuthorization == null) {
      throw new SpecParserException(name().getLocalPart()
          + " must contain an 'Authorization' element!");
    }

    if (oAuthRequest.getMethod() != oAuthAccess.getMethod()) {
      throw new SpecParserException(name().getLocalPart()
          + " access@method is not equal request@method!");
    }

    if (oAuthRequest.getParamLocation() != oAuthAccess.getParamLocation()) {
      throw new SpecParserException(name().getLocalPart()
          + " access@param_location is not equal request@param_location!");
    }
  }

  public static class Parser extends SpecElement.Parser<OAuthService> {

    public Parser(final Uri base) {
      this(new QName(ELEMENT_NAME), base);
    }

    public Parser(final QName name, final Uri base) {
      super(name, base);
      register(new OAuthRequest.Parser(base));
      register(new OAuthAccess.Parser(base));
      register(new OAuthAuthorization.Parser(base));
      register(ATTR_NAME);
    }

    @Override
    protected OAuthService newElement() {
      return new OAuthService(name(), getAttrNames(), getBase());
    }

    @Override
    protected void addChild(XMLStreamReader reader,
        final OAuthService oAuthService, final SpecElement child)
        throws GadgetException {
      if (child instanceof OAuthRequest) {
        oAuthService.setOAuthRequest((OAuthRequest) child);
      } else if (child instanceof OAuthAccess) {
        oAuthService.setOAuthAccess((OAuthAccess) child);
      } else if (child instanceof OAuthAuthorization) {
        oAuthService.setOAuthAuthorization((OAuthAuthorization) child);
      } else {
        super.addChild(reader, oAuthService, child);
      }
    }
  }
}
