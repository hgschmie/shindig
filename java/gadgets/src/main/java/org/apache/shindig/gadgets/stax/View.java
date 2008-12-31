/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.apache.shindig.gadgets.stax;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.AuthType;
import org.apache.shindig.gadgets.spec.Content;
import org.apache.shindig.gadgets.spec.RequestAuthenticationInfo;
import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.spec.Content.Type;
import org.apache.shindig.gadgets.variables.Substitutions;

/**
 * Normalized Content views.
 */
public class View implements RequestAuthenticationInfo {

  private final String name;

  private final Uri baseUri;

  private final Type type;

  private final Uri href;

  private final int preferredHeight;

  private final int preferredWidth;

  private final String content;

  private final String rawType;

  private final AuthType authType;

  private final boolean quirks;

  private final boolean signOwner;

  private final boolean signViewer;

  private final Map<String, String> attributes;

  /**
   * @param name
   *          The name of this view.
   * @param elements
   *          List of all views, in order, that make up this view. An ordered
   *          list is required per the spec, since values must overwrite one
   *          another.
   * @param base
   *          The base url to resolve href against.
   * @throws SpecParserException
   */
  public View(final String name, final Collection<Content> contents,
      final Uri baseUri) throws SpecParserException {
    this.name = name;
    this.baseUri = baseUri;

    Type type = null;
    Uri href = null;
    int preferredHeight = -1;
    int preferredWidth = -1;
    StringBuilder text = new StringBuilder();
    String rawType = null;
    AuthType authType = null;
    boolean quirks = true;
    boolean signOwner = false;
    boolean signViewer = false;
    Map<String, String> attributes = new HashMap<String, String>();

    for (Content content : contents) {
      switch (content.getType()) {
      case HTML:
        if (type == Content.Type.URL) {
          throw new SpecParserException(content.name().getLocalPart()
              + " contains HTML code but View '" + name + "' already uses '"
              + href + "' as content!");
        }
        text.append(content.getText());
        type = Content.Type.HTML;
        break;
      case URL:
        if (type == Content.Type.HTML) {
          throw new SpecParserException(content.name().getLocalPart()
              + " references '" + content.getHref() + " but View '" + name
              + "' already has inline HTML code!");
        }
        href = content.getHref();
        type = Content.Type.URL;
        break;
      default:
        throw new SpecParserException(content.name().getLocalPart()
            + " references unknown content type: " + content.getType());
      }

      if (content.getPreferredHeight() != -1) {
        preferredHeight = content.getPreferredHeight();
      }

      if (content.getPreferredWidth() != -1) {
        preferredWidth = content.getPreferredWidth();
      }

      rawType = content.getRawType();
      authType = content.getAuthType();
      quirks = content.isQuirks();
      signOwner = content.isSignOwner();
      signViewer = content.isSignViewer();
      attributes = addAttributes(attributes, content.getAttributes());
    }

    this.type = type;
    this.href = href;
    this.preferredHeight = preferredHeight;
    this.preferredWidth = preferredWidth;
    this.content = text.toString();
    this.attributes = attributes;
    this.rawType = rawType;
    this.authType = authType;
    this.quirks = quirks;
    this.signOwner = signOwner;
    this.signViewer = signViewer;
  }

  /**
   * Allows the creation of a view from an existing view so that localization
   * can be performed.
   */
  private View(final View view, final Substitutions substituter) {
    this.name = view.getName();
    this.baseUri = view.getBaseUri();

    this.type = view.getType();
    this.preferredHeight = view.getPreferredHeight();
    this.preferredWidth = view.getPreferredWidth();
    this.rawType = view.getRawType();
    this.authType = view.getAuthType();
    this.quirks = view.isQuirks();
    this.signOwner = view.isSignOwner();
    this.signViewer = view.isSignViewer();

    this.content = substituter.substituteString(view.getContent());

    this.href = baseUri.resolve(substituter.substituteUri(view.getHref()));

    final Map<String, String> attributes = new HashMap<String, String>();

    for (Map.Entry<String, String> entry : view.getAttributes().entrySet()) {
      attributes.put(entry.getKey(), substituter.substituteString(entry
          .getValue()));
    }
    this.attributes = Collections.unmodifiableMap(attributes);
  }

  public String getName() {
    return name;
  }

  public Type getType() {
    return type;
  }

  public Uri getHref() {
    return href;
  }

  public int getPreferredHeight() {
    return preferredHeight;
  }

  public int getPreferredWidth() {
    return preferredWidth;
  }

  public String getContent() {
    return content;
  }

  public Map<String, String> getAttributes() {
    return attributes;
  }

  public String getRawType() {
    return rawType;
  }

  private Uri getBaseUri() {
    return baseUri;
  }

  /**
   * Creates a new view by performing hangman substitution. See field comments
   * for details on what gets substituted.
   *
   * @param substituter
   * @return The substituted view.
   */
  public View substitute(final Substitutions substituter) {
    return new View(this, substituter);
  }

  // ========================================================================================
  // Non-0.8.1 methods

  @Override
  public AuthType getAuthType() {
    return authType;
  }

  @Override
  public Map<String, String> getOAuthAttributes() {
    return Collections.unmodifiableMap(attributes);
  }

  public boolean isQuirks() {
    return quirks;
  }

  @Override
  public boolean isSignOwner() {
    return signOwner;
  }

  @Override
  public boolean isSignViewer() {
    return signViewer;
  }

  public static final Map<String, String> addAttributes(
      final Map<String, String> existing, final Map<String, String> attributes) {
    final Map<String, String> newAttributes = (existing != null) ? existing
        : new HashMap<String, String>();

    if (attributes != null) {
      for (Map.Entry<String, String> entry : attributes.entrySet()) {
        newAttributes.put(entry.getKey(), entry.getValue());
      }
    }

    return existing;
  }

}
