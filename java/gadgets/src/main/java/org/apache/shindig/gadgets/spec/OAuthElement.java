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

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.variables.Substitutions;

public abstract class OAuthElement extends SpecElement {

  public static final String ATTR_URL = "url";
  public static final String ATTR_METHOD = "method";
  public static final String ATTR_PARAM_LOCATION = "param_location";

  private boolean request = false;

  protected OAuthElement(final QName name, final Map<String, QName> attrNames,
      final Uri base, boolean request) {
    super(name, attrNames, base);
    this.request = request;
  }

  protected OAuthElement(final OAuthElement oAuthElement, final Substitutions substituter) {
      super(oAuthElement, substituter);
      this.request = oAuthElement.isRequest();
  }

  public boolean isRequest() {
    return request;
  }

  public Uri getUrl() {
    return attrUriNull(ATTR_URL);
  }

  public Method getMethod() {
    return Method.parse(attr(ATTR_METHOD, true));
  }

  public Location getParamLocation() {
    return Location.parse(attr(ATTR_PARAM_LOCATION, true));
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {

    if (getUrl() != null) {
      writeAttribute(writer, ATTR_URL, getUrl().toString());
    }
    if (attr(ATTR_METHOD) != null) {
      writeAttribute(writer, ATTR_METHOD, getMethod().toString());
    }
    if (attr(ATTR_PARAM_LOCATION) != null) {
      writeAttribute(writer, ATTR_PARAM_LOCATION,
          getParamLocation().toString());
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (getUrl() == null) {
      throw new SpecParserException(this, "@url must be set!");
    }
    if(!getUrl().isHttpUri()) {
      throw new SpecParserException(this, "@url value '" + attr(ATTR_URL) + "' is not a valid URL!");
    }

    if (Method.parse(attr(ATTR_METHOD)) == null) {
      throw new SpecParserException(this, "@method attribute value '" + attr(ATTR_METHOD) + "' is invalid!");
    }
    if (Location.parse(attr(ATTR_PARAM_LOCATION)) == null) {
      throw new SpecParserException(this, "@param_location attribute value '" + attr(ATTR_PARAM_LOCATION) + "' is invalid!");
    }
    if ((getMethod() == Method.GET) && (getParamLocation() == Location.BODY)) {
      throw new SpecParserException(this, "@method is GET but parameter location is body!");
    }

  }

  public static enum Method {
    GET, POST;

    public static Method parse(String value) {
      if (value == null) {
        return GET;
      }
      for (Method method : Method.values()) {
        if (StringUtils.equalsIgnoreCase(method.toString(), StringUtils.trimToEmpty(value))) {
          return method;
        }
      }
      return null; // If set and invalid, return null. Catched in validate()
    }
  }

  public static enum Location {
    HEADER("auth-header"), URL("uri-query"), BODY("post-body");

    private final String value;

    private Location(final String value) {
      this.value = value.toLowerCase();
    }

    @Override
    public String toString() {
      return value;
    }

    public static Location parse(String value) {
      if (value == null) {
        return HEADER;
      }
      for (Location location : Location.values()) {
        if (StringUtils.equalsIgnoreCase(location.toString(), StringUtils.trimToEmpty(value))) {
          return location;
          // Work around spec bug. The parser should actually accept not just "auth-header", "uri-query" and "post-body"
          // but also "header", "url" and "body".
        } else if (StringUtils.equalsIgnoreCase(location.name(), StringUtils.trimToEmpty(value))) {
          return location;
        }
      }
      return null;
    }
  }

  public static abstract class Parser extends SpecElement.Parser<OAuthElement> {

    public Parser(final QName name, final Uri base) {
      super(name, base);
      register(ATTR_URL, ATTR_METHOD, ATTR_PARAM_LOCATION);
    }

    @Override
    protected abstract OAuthElement newElement();
  }
}
