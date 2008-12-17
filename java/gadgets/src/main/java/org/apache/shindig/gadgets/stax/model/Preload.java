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

import org.apache.commons.lang.StringUtils;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.AuthType;
import org.apache.shindig.gadgets.spec.SpecParserException;

import com.google.common.collect.ImmutableSet;

public class Preload extends SpecElement {

  public Preload(final QName name) {
    super(name);
  }

  /**
   * Preload@href
   */
  private Uri href;

  public Uri getHref() {
    return href;
  }

  /**
   * Preload@auth
   */
  private AuthType auth;

  public AuthType getAuthType() {
    return auth;
  }

  /**
   * Preload/@sign_owner
   */
  private boolean signOwner;

  public boolean isSignOwner() {
    return signOwner;
  }

  /**
   * Preload/@sign_viewer
   */
  private boolean signViewer;

  public boolean isSignViewer() {
    return signViewer;
  }

  /**
   * Prelaod@views
   */
  private Set<String> views;

  public Set<String> getViews() {
    return views;
  }

  private void setHref(Uri href) {
    this.href = href;
  }

  private void setAuth(AuthType auth) {
    this.auth = auth;
  }

  private void setSignOwner(boolean signOwner) {
    this.signOwner = signOwner;
  }

  private void setSignViewer(boolean signViewer) {
    this.signViewer = signViewer;
  }

  private void setViews(final Set<String> views) {
    this.views = views;
  }

  @Override
  public void validate() throws SpecParserException {
    if (getHref() == null) {
      throw new SpecParserException("Preload/@href is missing or invalid.");
    }
  }

  public static class Parser extends SpecElement.Parser<Preload> {
    private final QName attrViews;
    private final QName attrHref;
    private final QName attrAuthz;
    private final QName attrSignOwner;
    private final QName attrSignViewer;

    public Parser() {
      this(new QName("Preload"));
    }

    public Parser(final QName name) {
      super(name);
      this.attrViews = buildQName(name, "views");
      this.attrHref = buildQName(name, "href");
      this.attrAuthz = buildQName(name, "authz");
      this.attrSignOwner = buildQName(name, "sign_owner");
      this.attrSignViewer = buildQName(name, "sign_viewer");
    }

    @Override
    protected Preload newElement() {
      return new Preload(getName());
    }

    @Override
    protected void setAttribute(final Preload preload, final QName name,
        final String value) {
      if (name.equals(attrViews)) {
        preload.setViews(ImmutableSet.of(StringUtils.stripAll(StringUtils.split(StringUtils.defaultString(value), ','))));
      } else if (name.equals(attrHref)) {
        if (StringUtils.isNotEmpty(value)) {
          preload.setHref(Uri.parse(value));
        }
      } else if (name.equals(attrAuthz)) {
        preload.setAuth(AuthType.parse(value));
      } else if (name.equals(attrSignOwner)) {
        preload.setSignOwner(true);
      } else if (name.equals(attrSignViewer)) {
        preload.setSignViewer(true);
      } else {
        super.setAttribute(preload, name, value);
      }
    }

  }
}
