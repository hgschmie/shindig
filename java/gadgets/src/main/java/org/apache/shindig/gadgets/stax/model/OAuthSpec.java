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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.gadgets.spec.SpecParserException;

public class OAuthSpec extends SpecElement {

  public static final String ELEMENT_NAME = "OAuth";

  private Map<String, OAuthService> services = new HashMap<String, OAuthService>();

  public OAuthSpec(final QName name, final Map<String, QName> attrNames) {
    super(name, attrNames);
  }

  public Map<String, OAuthService> getServices() {
    return Collections.unmodifiableMap(services);
  }

  private void addService(final OAuthService oAuthService) {
    this.services.put(oAuthService.getName(), oAuthService);
  }

  @Override
  protected void writeChildren(final XMLStreamWriter writer)
      throws XMLStreamException {
    for (OAuthService oAuthService : services.values()) {
      oAuthService.toXml(writer);
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (services.size() == 0) {
      throw new SpecParserException(name().getLocalPart()
          + " must contain at least one 'service' element!");
    }
  }

  public static class Parser extends SpecElement.Parser<OAuthSpec> {

    public Parser() {
      this(new QName(ELEMENT_NAME));
    }

    public Parser(final QName name) {
      super(name);
      register(new OAuthService.Parser());
    }

    @Override
    protected OAuthSpec newElement() {
      return new OAuthSpec(name(), getAttrNames());
    }

    @Override
    protected void addChild(XMLStreamReader reader, final OAuthSpec oAuth,
        final SpecElement child) throws SpecParserException {
      if (child instanceof OAuthService) {
        oAuth.addService((OAuthService) child);
      } else {
        super.addChild(reader, oAuth, child);
      }
    }
  }
}
