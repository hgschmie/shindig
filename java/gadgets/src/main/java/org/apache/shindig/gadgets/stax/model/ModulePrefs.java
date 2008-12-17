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
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.StaxUtils;

import com.google.common.collect.ImmutableSet;

public class ModulePrefs extends SpecElement {

  private static final String ATTR_TITLE = "title";
  private static final String ATTR_TITLE_URL = "title_url";
  private static final String ATTR_DESCRIPTION = "description";
  private static final String ATTR_AUTHOR = "author";
  private static final String ATTR_AUTHOR_EMAIL = "author_email";
  private static final String ATTR_SCREENSHOT = "screenshot";
  private static final String ATTR_THUMBNAIL = "thumbnail";
  private static final String ATTR_DIRECTORY_TITLE = "directory_title";
  private static final String ATTR_AUTHOR_AFFILIATION = "author_affiliation";
  private static final String ATTR_AUTHOR_LOCATION = "author_location";
  private static final String ATTR_AUTHOR_PHOTO = "author_photo";
  private static final String ATTR_AUTHOR_ABOUTME = "author_aboutme";
  private static final String ATTR_AUTHOR_QUOTE = "author_quote";
  private static final String ATTR_AUTHOR_LINK = "author_link";
  private static final String ATTR_SHOW_STATS = "show_stats";
  private static final String ATTR_SHOW_IN_DIRECTORY = "show_in_directory";
  private static final String ATTR_SINGLETON = "singleton";
  private static final String ATTR_SCALING = "scaling";
  private static final String ATTR_SCROLLING = "scrolling";
  private static final String ATTR_WIDTH = "width";
  private static final String ATTR_HEIGHT = "height";
  private static final String ATTR_CATEGORY = "category";
  private static final String ATTR_CATEGORY2 = "category2";
  private static final String ATTR_RENDER_INLINE = "render_inline";

  private final List<Preload> preloads = new ArrayList<Preload>();
  private final Map<String, Feature> features = new HashMap<String, Feature>();
  private final List<Icon> icons = new ArrayList<Icon>();
  private final Map<Locale, ModulePrefsLocale> locales = new HashMap<Locale, ModulePrefsLocale>();
  private final Map<String, LinkSpec> links = new HashMap<String, LinkSpec>();
  private OAuth oauth = null;

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

  public Map<Locale, ModulePrefsLocale> getLocales() {
    return locales;
  }

  public Map<String, LinkSpec> getLinks() {
    return links;
  }

  public OAuth getOauth() {
    return oauth;
  }

  private Map<String, String> attributes = new HashMap<String, String>();
  private final List<String> categories = new ArrayList<String>();

  public String getTitle() {
    return StringUtils.defaultString(attributes.get(ATTR_TITLE));
  }

  public Uri getTitleUrl() {
    return StaxUtils.toUri(attributes.get(ATTR_TITLE_URL));
  }

  public String getDescription() {
    return StringUtils.defaultString(attributes.get(ATTR_DESCRIPTION));
  }

  public String getAuthor() {
    return StringUtils.defaultString(attributes.get(ATTR_AUTHOR));
  }

  public String getAuthorEmail() {
    return StringUtils.defaultString(attributes.get(ATTR_AUTHOR_EMAIL));
  }

  public Uri getScreenshot() {
    return StaxUtils.toUri(attributes.get(ATTR_SCREENSHOT));
  }

  public Uri getThumbnail() {
    return StaxUtils.toUri(attributes.get(ATTR_THUMBNAIL));
  }

  public String getDirectoryTitle() {
    return StringUtils.defaultString(attributes.get(ATTR_DIRECTORY_TITLE));
  }

  public String getAuthorAffiliation() {
    return StringUtils.defaultString(attributes.get(ATTR_AUTHOR_AFFILIATION));
  }

  public String getAuthorLocation() {
    return StringUtils.defaultString(attributes.get(ATTR_AUTHOR_LOCATION));
  }

  public Uri getAuthorPhoto() {
    return StaxUtils.toUri(attributes.get(ATTR_AUTHOR_PHOTO));
  }

  public String getAuthorAboutme() {
    return StringUtils.defaultString(attributes.get(ATTR_AUTHOR_ABOUTME));
  }

  public String getAuthorQuote() {
    return StringUtils.defaultString(attributes.get(ATTR_AUTHOR_QUOTE));
  }

  public Uri getAuthorLink() {
    return StaxUtils.toUri(attributes.get(ATTR_AUTHOR_LINK));
  }

  public boolean isShowStats() {
    return BooleanUtils.toBoolean(attributes.get(ATTR_SHOW_STATS));
  }

  public boolean isShowInDirectory() {
    return BooleanUtils.toBoolean(attributes.get(ATTR_SHOW_IN_DIRECTORY));
  }

  public boolean isSingleton() {
    return BooleanUtils.toBoolean(attributes.get(ATTR_SINGLETON));
  }

  public boolean isScaling() {
    return BooleanUtils.toBoolean(attributes.get(ATTR_SCALING));
  }

  public boolean isScrolling() {
    return BooleanUtils.toBoolean(attributes.get(ATTR_SCROLLING));
  }

  public int getWidth() {
    return NumberUtils.toInt(attributes.get(ATTR_WIDTH));
  }

  public int getHeight() {
    return NumberUtils.toInt(attributes.get(ATTR_HEIGHT));
  }

  public boolean getRenderInline() {
    return BooleanUtils.toBoolean(attributes.get(ATTR_RENDER_INLINE));
  }

  public List<String> getCategories() {
    return categories;
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

  private void addLocale(final ModulePrefsLocale locale) {
    locales.put(new Locale(locale.getLanguage(), locale.getCountry()), locale);
  }

  private void addLink(final LinkSpec link) {
    links.put(link.getRel(), link);
  }

  private void setOAuth(final OAuth oauth) {
    this.oauth = oauth;
  }

  private void setAttribute(final String key, final String value) {
    this.attributes.put(key, value);
  }

  private void addCategory(final String category) {
    categories.add(category);
  }

  @Override
  protected void writeChildren(final XMLStreamWriter writer) throws XMLStreamException {
    for (Preload preload : preloads) {
      preload.toXml(writer);
    }
    for (Feature feature : features.values()) {
      feature.toXml(writer);
    }
    for (Icon icon : icons) {
      icon.toXml(writer);
    }
    for (ModulePrefsLocale locale : locales.values()) {
      locale.toXml(writer);
    }
    for (LinkSpec link : links.values()) {
      link.toXml(writer);
    }
    if (oauth != null) {
      oauth.toXml(writer);
    }
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer) throws XMLStreamException {
    for (Map.Entry<String, String> attribute : attributes.entrySet()) {
      writer.writeAttribute(name().getNamespaceURI(), attribute.getKey(), attribute.getValue());
    }
  }

  @Override
  public void validate() throws SpecParserException {
    // TODO - add validation
  }

  public static class Parser extends SpecElement.Parser<ModulePrefs> {

    private final Set<QName> knownAttributes;
    private final Set<QName> categories;

    public Parser() {
      this(new QName("ModulePrefs"));
    }

    public Parser(final QName name) {
      super(name);

      this.knownAttributes = ImmutableSet.of(buildQName(name, ATTR_TITLE), buildQName(name, ATTR_TITLE_URL), buildQName(name, ATTR_DESCRIPTION), buildQName(
          name, ATTR_AUTHOR), buildQName(name, ATTR_AUTHOR_EMAIL), buildQName(name, ATTR_SCREENSHOT), buildQName(name, ATTR_THUMBNAIL), buildQName(name,
          ATTR_DIRECTORY_TITLE), buildQName(name, ATTR_AUTHOR_AFFILIATION), buildQName(name, ATTR_AUTHOR_LOCATION), buildQName(name, ATTR_AUTHOR_PHOTO),
          buildQName(name, ATTR_AUTHOR_ABOUTME), buildQName(name, ATTR_AUTHOR_QUOTE), buildQName(name, ATTR_AUTHOR_LINK), buildQName(name, ATTR_SHOW_STATS),
          buildQName(name, ATTR_SHOW_IN_DIRECTORY), buildQName(name, ATTR_SINGLETON), buildQName(name, ATTR_SCALING), buildQName(name, ATTR_SCROLLING),
          buildQName(name, ATTR_WIDTH), buildQName(name, ATTR_HEIGHT), buildQName(name, ATTR_RENDER_INLINE));

      this.categories = ImmutableSet.of(buildQName(name, ATTR_CATEGORY), buildQName(name, ATTR_CATEGORY2));

      register(new Require.Parser());
      register(new Optional.Parser());
      register(new Preload.Parser());
      register(new Icon.Parser());
      register(new ModulePrefsLocale.Parser());
      register(new LinkSpec.Parser());
      register(new OAuth.Parser());
    }

    @Override
    protected ModulePrefs newElement() {
      return new ModulePrefs(getName());
    }

    @Override
    protected void setAttribute(final ModulePrefs modulePrefs, final QName name, final String value) {
      if (knownAttributes.contains(name)) {
        modulePrefs.setAttribute(name.getLocalPart(), value);
      } else if (categories.contains(name)) {
        modulePrefs.addCategory(value);
      } else {
        super.setAttribute(modulePrefs, name, value);
      }
    }

    @Override
    protected void addChild(XMLStreamReader reader, final ModulePrefs prefs, final SpecElement child) {
      if (child instanceof Feature) {
        prefs.addFeature((Feature) child);
      } else if (child instanceof Preload) {
        prefs.addPreload((Preload) child);
      } else if (child instanceof Icon) {
        prefs.addIcon((Icon) child);
      } else if (child instanceof ModulePrefsLocale) {
        prefs.addLocale((ModulePrefsLocale) child);
      } else if (child instanceof LinkSpec) {
        prefs.addLink((LinkSpec) child);
      } else if (child instanceof OAuth) {
        prefs.setOAuth((OAuth) child);
      } else {
        super.addChild(reader, prefs, child);
      }
    }
  }
}
