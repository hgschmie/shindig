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
package org.apache.shindig.gadgets.stax.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.spec.GadgetSpec;
import org.apache.shindig.gadgets.stax.View;
import org.apache.shindig.gadgets.variables.Substitutions;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;


public class ShindigGadgetSpec extends StaxGadgetSpec implements GadgetSpec {

  private final String checksum;

    private final Uri url;

    private Multimap<String, Content> viewMap = new HashMultimap<String, Content>();

    private ConcurrentHashMap <String, View> views = new ConcurrentHashMap<String, View>();

  /**
   * A map of attributes associated with the instance of the spec Used by handler classes to use specs to carry context. Not defined
   * by the specification
   */
  private final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

    public ShindigGadgetSpec(final QName name, final Uri url, final String checksum) {
    super(name);
    this.checksum = checksum;
    this.url = url;
  }

  public String getChecksum() {
    return checksum;
  }

  @Override
  public Uri getUrl() {
      return url;
  }

    // ========================================================================

  public Object getAttribute(final String key) {
    return attributes.get(key);
  }

  public void setAttribute(final String key, final Object o) {
    attributes.put(key, o);
  }

    // ========================================================================

  @Override
  protected void addContent(final Content content) {
      super.addContent(content);
      for(String viewName: content.getViews()) {
          viewMap.put(viewName, content);
          views.put(viewName, new View(viewName, viewMap.get(viewName)));
        }
  }


  @Override
  public View getView(final String name) {
    View view = views.get(name);
  }

    // ========================================================================

  @Override
  public Map<String, View> getViews() {
    return Collections.unmodifiableMap(views);
  }

  @Override
  public GadgetSpec substitute(Substitutions substituter) {
    // TODO Auto-generated method stub
    return null;
  }

  public static class Parser extends StaxGadgetSpec.Parser<ShindigGadgetSpec> {
    private final Uri url;
    private final String checksum;

    public Parser(final Uri url, final String checksum) {
      this.url = url;
      this.checksum = checksum;
    }

    @Override
    protected ShindigGadgetSpec newElement() {
      return new ShindigGadgetSpec(getName(), url, checksum);
    }

    @Override
    public ShindigGadgetSpec parse(final XMLStreamReader reader) {
      return (ShindigGadgetSpec) super.parse(reader);
    }

    @Override
    protected void addChild(final XMLStreamReader reader,
        final StaxGadgetSpec spec, final SpecElement child) {
      if (child instanceof Content) {
        spec.addContent((Content) child);
      } else {
        super.addChild(reader, spec, child);
      }
    }
  }
}
