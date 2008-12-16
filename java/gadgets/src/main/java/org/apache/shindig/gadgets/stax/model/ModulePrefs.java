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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.gadgets.spec.LinkSpec;
import org.apache.shindig.gadgets.spec.LocaleSpec;
import org.apache.shindig.gadgets.spec.OAuthSpec;
import org.apache.shindig.gadgets.spec.SpecParserException;

public class ModulePrefs extends SpecElement {

  private final List<Preload> preloads = new ArrayList<Preload>();
  private final Map<String, Feature> features = new HashMap<String, Feature>();
  private final List<Icon> icons = new ArrayList<Icon>();
  private final Map<Locale, LocaleSpec> locales = new HashMap<Locale, LocaleSpec>();
  private final Map<String, LinkSpec> links = new HashMap<String, LinkSpec>();
  private OAuthSpec oauthSpec = null;

  public ModulePrefs(final QName name) {
    super(name);
  }

  public List<Preload> getPreloads() {
    return preloads;
  }

  public Map<String, Feature> getFeatures() {
    return features;
  }

  public List<Icon> getIcons() {
    return icons;
  }

  public Map<Locale, LocaleSpec> getLocales() {
    return locales;
  }

  public Map<String, LinkSpec> getLinks() {
    return links;
  }

  public OAuthSpec getOauthSpec() {
    return oauthSpec;
  }

  private void addPreload(final Preload preload) {
    preloads.add(preload);
  }

  private void addFeature(final Feature feature) {
    features.put(feature.getName(), feature);
  }

  private void addIcon(final Icon icon) {
    icons.add(icon);
  }

  private void addLocale(final Locale locale) {
  }

  private void addLink(final Link link) {
  }

  private void setOAuth(final OAuth oauth) {
  }

  @Override
  protected void addXml(XMLStreamWriter writer) throws XMLStreamException {
    for (Preload preload : preloads) {
      preload.toXml(writer);
    }
    for (Feature feature : features.values()) {
      feature.toXml(writer);
    }
    for (Icon icon : icons) {
      icon.toXml(writer);
    }
    /*
     * for (Locale locale : locales.values()) { locale.toXml(writer); } for
     * (Link link : links) { links.toXml(writer); } if (oauth != null) {
     * oauth.toXml(writer); }
     */
  }

  public static class Parser extends SpecElement.Parser<ModulePrefs> {
    public Parser() {
      this(new QName("ModulePrefs"));
    }

    public Parser(final QName name) {
      super(name);
      register(new Require.Parser());
      register(new Optional.Parser());
      register(new Preload.Parser());
      register(new Icon.Parser());
      register(new Locale.Parser());
      register(new Link.Parser());
      register(new OAuth.Parser());
    }

    @Override
    protected ModulePrefs newElement() {
      return new ModulePrefs(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, final ModulePrefs prefs,
        final SpecElement child) {
      if (child instanceof Feature) {
        prefs.addFeature((Feature) child);
      } else if (child instanceof Preload) {
        prefs.addPreload((Preload) child);
      } else if (child instanceof Icon) {
        prefs.addIcon((Icon) child);
      } else if (child instanceof Locale) {
        prefs.addLocale((Locale) child);
      } else if (child instanceof Link) {
        prefs.addLink((Link) child);
      } else if (child instanceof OAuth) {
        prefs.setOAuth((OAuth) child);
      } else {
        super.addChild(reader, prefs, child);
      }
    }

    @Override
    public void validate(ModulePrefs element) throws SpecParserException {
      // TODO - add validation
    }

  }
}
