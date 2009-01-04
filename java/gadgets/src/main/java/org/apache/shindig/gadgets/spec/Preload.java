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
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.AuthType;
import org.apache.shindig.gadgets.variables.Substitutions;

import com.google.common.collect.ImmutableSet;

public class Preload extends SpecElement implements RequestAuthenticationInfo {

  public static final QName ELEMENT_NAME = new QName(SpecElement.OPENSOCIAL_NAMESPACE_URI, "Preload");

  public static final String ATTR_HREF = "href";
  public static final String ATTR_AUTHZ = "authz";
  public static final String ATTR_SIGN_OWNER = "sign_owner";
  public static final String ATTR_SIGN_VIEWER = "sign_viewer";
  public static final String ATTR_VIEWS = "views";
  public static final String ATTR_OAUTH_SERVICE_NAME = "oauth_service_name";
  public static final String ATTR_OAUTH_TOKEN_NAME = "oauth_token_name";
  public static final String ATTR_OAUTH_REQUEST_TOKEN = "oauth_request_token";
  public static final String ATTR_OAUTH_REQUEST_TOKEN_SECRET = "oauth_request_token_secret";
  public static final String ATTR_OAUTH_USE_TOKEN = "oauth_use_token";

  protected Preload(final QName name, final Map<String, QName> attrNames, final Uri base) {
    super(name, attrNames, base);
  }

  protected Preload(final Preload preload, final Substitutions substituter) {
    super(preload, substituter);

    setAttr(ATTR_VIEWS, StringUtils.join(preload.getViews(), ','));
    setAttr(ATTR_AUTHZ, preload.getAuthType().toString());
    setAttr(ATTR_SIGN_OWNER, String.valueOf(preload.isSignOwner()));
    setAttr(ATTR_SIGN_VIEWER, String.valueOf(preload.isSignViewer()));
    setAttr(ATTR_HREF, getBase().resolve(
        substituter.substituteUri(preload.getHref())).toString());
  }

  /**
   * Produces a new Preload by substituting hangman variables from substituter.
   * See comments on individual fields to see what actually has substitutions
   * performed.
   *
   * @param substituter
   */
  @Override
  public Preload substitute(final Substitutions substituter) {
    return new Preload(this, substituter);
  }

  public Uri getHref() {
    return attrUriNull(ATTR_HREF);
  }

  public AuthType getAuthType() {
    return AuthType.parse(attr(ATTR_AUTHZ));
  }

  public boolean isSignOwner() {
    return attrBool(ATTR_SIGN_OWNER, true);
  }

  public boolean isSignViewer() {
    return attrBool(ATTR_SIGN_VIEWER, true);
  }

  public Set<String> getViews() {
    return ImmutableSet.of(StringUtils.stripAll(StringUtils.split(
        attrDefault(ATTR_VIEWS), ',')));
  }

  public String getOAuthServiceName() {
    return attrDefault(ATTR_OAUTH_SERVICE_NAME);
  }

  public String getOAuthTokenName() {
    return attrDefault(ATTR_OAUTH_TOKEN_NAME);
  }

  public String getOAuthRequestToken() {
    return attrDefault(ATTR_OAUTH_REQUEST_TOKEN);
  }

  public String getOAuthRequestTokenSecret() {
    return attrDefault(ATTR_OAUTH_REQUEST_TOKEN_SECRET);
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {

    if (getHref() != null) {
      writeAttribute(writer, ATTR_HREF, getHref().toString());
    }

    if (attr(ATTR_AUTHZ) != null) {
      writeAttribute(writer, ATTR_AUTHZ, getAuthType().toString());
    }

    if (attr(ATTR_SIGN_OWNER) != null) {
      writeAttribute(writer, ATTR_SIGN_OWNER, String
          .valueOf(isSignOwner()));
    }

    if (attr(ATTR_SIGN_VIEWER) != null) {
      writeAttribute(writer, ATTR_SIGN_VIEWER, String
          .valueOf(isSignViewer()));
    }

    if (attr(ATTR_VIEWS) != null) {
      writeAttribute(writer, ATTR_VIEWS, StringUtils.join(
          getViews(), ','));
    }

    if (attr(ATTR_OAUTH_SERVICE_NAME) != null) {
      writeAttribute(writer, ATTR_OAUTH_SERVICE_NAME,
          getOAuthServiceName());
    }

    if (attr(ATTR_OAUTH_TOKEN_NAME) != null) {
      writeAttribute(writer, ATTR_OAUTH_TOKEN_NAME,
          getOAuthTokenName());
    }

    if (attr(ATTR_OAUTH_REQUEST_TOKEN) != null) {
      writeAttribute(writer, ATTR_OAUTH_REQUEST_TOKEN,
          getOAuthRequestToken());
    }

    if (attr(ATTR_OAUTH_REQUEST_TOKEN_SECRET) != null) {
      writeAttribute(writer, ATTR_OAUTH_REQUEST_TOKEN_SECRET,
          getOAuthRequestTokenSecret());
    }

  }

  @Override
  public void validate() throws SpecParserException {
    if (getHref() == null) {
      throw new SpecParserException(name().getLocalPart()
          + "@href must be set!");
    }
  }

  @Override
  public Map<String, String> getOAuthAttributes() {
    return attributes();
  }

  public static class Parser extends SpecElement.Parser<Preload> {

    public Parser(final Uri base) {
      this(ELEMENT_NAME, base);
    }

    public Parser(final QName name, final Uri base) {
      super(name, base);

      register(ATTR_HREF, ATTR_AUTHZ, ATTR_SIGN_OWNER, ATTR_SIGN_VIEWER,
          ATTR_VIEWS, ATTR_OAUTH_SERVICE_NAME, ATTR_OAUTH_TOKEN_NAME,
          ATTR_OAUTH_REQUEST_TOKEN, ATTR_OAUTH_REQUEST_TOKEN_SECRET, ATTR_OAUTH_USE_TOKEN);
    }

    @Override
    protected Preload newElement() {
      return new Preload(name(), getAttrNames(), getBase());
    }
  }
}
