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

public abstract class OAuthElement extends SpecElement {

  private static final String ATTR_URL = "url";
  private static final String ATTR_METHOD = "method";
  private static final String ATTR_PARAM_LOCATION = "param_location";

  private boolean request = false;

  private String url;
  private String method;
  private String paramLocation;

  protected OAuthElement(final QName name, boolean request) {
    super(name);
    this.request = request;
  }

  public boolean isRequest() {
    return request;
  }

  public Uri getUrl() {
    return StaxUtils.toUri(url);
  }

  public Method getMethod() {
    return Method.parse(method);
  }

  public Location getParamLocation() {
    return Location.parse(paramLocation);
  }

  private void setUrl(final String url) {
    this.url = url;
  }

  private void setMethod(final String method) {
    this.method = method;
  }

  private void setParamLocation(final String paramLocation) {
    this.paramLocation = paramLocation;
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();
    if (url != null) {
      writer.writeAttribute(namespaceURI, ATTR_URL, getUrl().toString());
    }
    if (method != null) {
      writer.writeAttribute(namespaceURI, ATTR_METHOD, getMethod().toString());
    }
    if (paramLocation != null) {
      writer.writeAttribute(namespaceURI, ATTR_PARAM_LOCATION,
          getParamLocation().toString());
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (url == null) {
      throw new SpecParserException(name().getLocalPart() + "@url must be set!");
    }
    if ((getMethod() == Method.GET) && (getParamLocation() == Location.BODY)) {
        throw new SpecParserException(name().getLocalPart() + "@method is GET but parameter location is body!");
    }
  }

  public static enum Method {
    GET, POST;

    public static Method parse(String value) {
      for (Method method : Method.values()) {
        if (method.toString().compareToIgnoreCase(value) == 0) {
          return method;
        }
      }
      return GET;
    }
  }

  public static enum Location {
    HEADER, URL, BODY;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static Location parse(String value) {
      for (Location location : Location.values()) {
        if (location.toString().compareToIgnoreCase(value) == 0) {
          return location;
        }
      }
      return HEADER;
    }
  }

  public static abstract class Parser extends SpecElement.Parser<OAuthElement> {
    private final QName attrUrl;
    private final QName attrMethod;
    private final QName attrParamLocation;

    public Parser(final QName name) {
      super(name);
      attrUrl = buildQName(name, ATTR_URL);
      attrMethod = buildQName(name, ATTR_METHOD);
      attrParamLocation = buildQName(name, ATTR_PARAM_LOCATION);
    }

    @Override
    protected abstract OAuthElement newElement();

    @Override
    protected void setAttribute(final OAuthElement oAuthElement,
        final QName name, final String value) {
      if (name.equals(attrUrl)) {
        oAuthElement.setUrl(value);
      } else if (name.equals(attrMethod)) {
        oAuthElement.setMethod(value);
      } else if (name.equals(attrParamLocation)) {
        oAuthElement.setParamLocation(value);
      } else {
        super.setAttribute(oAuthElement, name, value);
      }
    }
  }
}
