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

import org.apache.commons.lang.StringUtils;
import org.apache.shindig.gadgets.spec.SpecParserException;

public abstract class Feature extends SpecElement {

  private static final String ATTR_FEATURE = "feature";

  private final boolean required;

  private String feature = null;

  private Map<String, FeatureParam> params = new HashMap<String, FeatureParam>();

  protected Feature(final QName name, final boolean required) {
    super(name);
    this.required = required;
  }

  public boolean isRequired() {
    return required;
  }

  public String getFeature() {
    return StringUtils.defaultString(feature);
  }

  public Map<String, FeatureParam> getParams() {
    return Collections.unmodifiableMap(params);
  }

  private void setFeature(final String feature) {
    this.feature = feature;
  }

  private void addParam(final FeatureParam param) {
    this.params.put(param.getName(), param);
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();
    if (feature != null) {
      writer.writeAttribute(namespaceURI, ATTR_FEATURE, getFeature());
    }
  }

  @Override
  protected void writeChildren(final XMLStreamWriter writer)
      throws XMLStreamException {
    for (final FeatureParam param : params.values()) {
      param.toXml(writer);
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (feature == null) {
      throw new SpecParserException(name().getLocalPart()
          + "@feature must be set!");
    }
  }

  public abstract static class Parser extends SpecElement.Parser<Feature> {
    private final QName attrFeature;

    public Parser(final QName name) {
      super(name);
      register(new FeatureParam.Parser());
      attrFeature = buildQName(name, ATTR_FEATURE);
    }

    @Override
    protected abstract Feature newElement();

    @Override
    protected void setAttribute(final Feature feature, final QName name,
        final String value) {
      if (name.equals(attrFeature)) {
        feature.setFeature(value);
      } else {
        super.setAttribute(feature, name, value);
      }
    }

    @Override
    protected void addChild(final XMLStreamReader reader,
        final Feature feature, final SpecElement child) throws SpecParserException {
      if (child instanceof FeatureParam) {
        feature.addParam((FeatureParam) child);
      } else {
        super.addChild(reader, feature, child);
      }
    }
  }
}
