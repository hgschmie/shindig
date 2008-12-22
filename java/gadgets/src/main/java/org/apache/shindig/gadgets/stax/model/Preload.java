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

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.AuthType;
import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.StaxUtils;

import com.google.common.collect.ImmutableSet;

public class Preload extends SpecElement {

  public static final String ELEMENT_NAME = "Preload";

  private static final String ATTR_HREF = "href";
  private static final String ATTR_AUTHZ = "authz";
  private static final String ATTR_SIGN_OWNER = "sign_owner";
  private static final String ATTR_SIGN_VIEWER = "sign_viewer";
  private static final String ATTR_VIEWS = "views";
  private static final String ATTR_OAUTH_SERVICE_NAME = "oauth_service_name";
  private static final String ATTR_OAUTH_TOKEN_NAME = "oauth_token_name";
  private static final String ATTR_OAUTH_REQUEST_TOKEN = "oauth_request_token";
  private static final String ATTR_OAUTH_REQUEST_TOKEN_SECRET = "oauth_request_token_secret";

  public Preload(final QName name) {
    super(name);
  }

  private String href;
  private String authType;
  private String signOwner = "true"; // Default is true, according to spec
  private String signViewer = "true"; // Default is true, according to spec
  private String views;
  private String oAuthServiceName;
  private String oAuthTokenName;
  private String oAuthRequestToken;
  private String oAuthRequestTokenSecret;

  public Uri getHref() {
    return StaxUtils.toUri(href);
  }

  public AuthType getAuthType() {
    return AuthType.parse(authType);
  }

  public boolean isSignOwner() {
    return BooleanUtils.toBoolean(signOwner);
  }

  public boolean isSignViewer() {
    return BooleanUtils.toBoolean(signViewer);
  }

  public Set<String> getViews() {
    return ImmutableSet.of(StringUtils.stripAll(StringUtils.split(StringUtils
        .defaultString(views), ',')));
  }

  public String getOAuthServiceName() {
    return StringUtils.defaultString(oAuthServiceName);
  }

  public String getOAuthTokenName() {
    return StringUtils.defaultString(oAuthTokenName);
  }

  public String getOAuthRequestToken() {
    return StringUtils.defaultString(oAuthRequestToken);
  }

  public String getOAuthRequestTokenSecret() {
    return StringUtils.defaultString(oAuthRequestTokenSecret);
  }

  private void setHref(final String href) {
    this.href = href;
  }

  private void setAuthType(final String authType) {
    this.authType = authType;
  }

  private void setSignOwner(final String signOwner) {
    this.signOwner = signOwner;
  }

  private void setSignViewer(final String signViewer) {
    this.signViewer = signViewer;
  }

  private void setViews(final String views) {
    this.views = views;
  }

  private void setOAuthServiceName(final String oAuthServiceName) {
    this.oAuthServiceName = oAuthServiceName;
  }

  private void setOAuthTokenName(final String oAuthTokenName) {
    this.oAuthTokenName = oAuthTokenName;
  }

  private void setOAuthRequestToken(final String oAuthRequestToken) {
    this.oAuthRequestToken = oAuthRequestToken;
  }

  private void setOAuthRequestTokenSecret(final String oAuthRequestTokenSecret) {
    this.oAuthRequestTokenSecret = oAuthRequestTokenSecret;
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();
    if (href != null) {
      writer.writeAttribute(namespaceURI, ATTR_HREF, getHref().toString());
    }
    if (authType != null) {
      writer.writeAttribute(namespaceURI, ATTR_AUTHZ, getAuthType().toString());
    }

    if (signOwner != null) {
      writer.writeAttribute(namespaceURI, ATTR_SIGN_OWNER, String
          .valueOf(isSignOwner()));
    }

    if (signViewer != null) {
      writer.writeAttribute(namespaceURI, ATTR_SIGN_VIEWER, String
          .valueOf(isSignViewer()));
    }

    if (views != null) {
      writer.writeAttribute(namespaceURI, ATTR_VIEWS, StringUtils.join(
          getViews(), ','));
    }

    if (oAuthServiceName != null) {
      writer.writeAttribute(namespaceURI, ATTR_OAUTH_SERVICE_NAME,
          getOAuthServiceName());
    }

    if (oAuthTokenName != null) {
      writer.writeAttribute(namespaceURI, ATTR_OAUTH_TOKEN_NAME,
          getOAuthTokenName());
    }

    if (oAuthRequestToken != null) {
      writer.writeAttribute(namespaceURI, ATTR_OAUTH_REQUEST_TOKEN,
          getOAuthRequestToken());
    }

    if (oAuthRequestTokenSecret != null) {
      writer.writeAttribute(namespaceURI, ATTR_OAUTH_REQUEST_TOKEN_SECRET,
          getOAuthRequestTokenSecret());
    }

  }

  @Override
  public void validate() throws SpecParserException {
    if (href == null) {
      throw new SpecParserException(name().getLocalPart()
          + "@href must be set!");
    }
  }

  public static class Parser extends SpecElement.Parser<Preload> {
    private final QName attrHref;
    private final QName attrAuthz;
    private final QName attrSignOwner;
    private final QName attrSignViewer;
    private final QName attrViews;
    private final QName attrOAuthServiceName;
    private final QName attrOAuthTokenName;
    private final QName attrOAuthRequestToken;
    private final QName attrOAuthRequestTokenSecret;

    public Parser() {
      this(new QName(ELEMENT_NAME));
    }

    public Parser(final QName name) {
      super(name);
      this.attrHref = buildQName(name, ATTR_HREF);
      this.attrAuthz = buildQName(name, ATTR_AUTHZ);
      this.attrSignOwner = buildQName(name, ATTR_SIGN_OWNER);
      this.attrSignViewer = buildQName(name, ATTR_SIGN_VIEWER);
      this.attrViews = buildQName(name, ATTR_VIEWS);
      this.attrOAuthServiceName = buildQName(name, ATTR_OAUTH_SERVICE_NAME);
      this.attrOAuthTokenName = buildQName(name, ATTR_OAUTH_TOKEN_NAME);
      this.attrOAuthRequestToken = buildQName(name, ATTR_OAUTH_REQUEST_TOKEN);
      this.attrOAuthRequestTokenSecret = buildQName(name,
          ATTR_OAUTH_REQUEST_TOKEN_SECRET);
    }

    @Override
    protected Preload newElement() {
      return new Preload(getName());
    }

    @Override
    protected void setAttribute(final Preload preload, final QName name,
        final String value) {
      if (name.equals(attrHref)) {
        preload.setHref(value);
      } else if (name.equals(attrAuthz)) {
        preload.setAuthType(value);
      } else if (name.equals(attrSignOwner)) {
        preload.setSignOwner(value);
      } else if (name.equals(attrSignViewer)) {
        preload.setSignViewer(value);
      } else if (name.equals(attrViews)) {
        preload.setViews(value);
      } else if (name.equals(attrOAuthServiceName)) {
        preload.setOAuthServiceName(value);
      } else if (name.equals(attrOAuthTokenName)) {
        preload.setOAuthTokenName(value);
      } else if (name.equals(attrOAuthRequestToken)) {
        preload.setOAuthRequestToken(value);
      } else if (name.equals(attrOAuthRequestTokenSecret)) {
        preload.setOAuthRequestTokenSecret(value);
      } else {
        super.setAttribute(preload, name, value);
      }
    }
  }
}
