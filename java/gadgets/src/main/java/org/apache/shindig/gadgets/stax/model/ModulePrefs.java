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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.spec.GadgetSpec;
import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.variables.Substitutions;

public class ModulePrefs extends SpecElement {
  public static final String ELEMENT_NAME = "ModulePrefs";

  public static final String ATTR_TITLE = "title";
  public static final String ATTR_TITLE_URL = "title_url";
  public static final String ATTR_DESCRIPTION = "description";
  public static final String ATTR_AUTHOR = "author";
  public static final String ATTR_AUTHOR_EMAIL = "author_email";
  public static final String ATTR_SCREENSHOT = "screenshot";
  public static final String ATTR_THUMBNAIL = "thumbnail";
  public static final String ATTR_DIRECTORY_TITLE = "directory_title";
  public static final String ATTR_AUTHOR_AFFILIATION = "author_affiliation";
  public static final String ATTR_AUTHOR_LOCATION = "author_location";
  public static final String ATTR_AUTHOR_PHOTO = "author_photo";
  public static final String ATTR_AUTHOR_ABOUTME = "author_aboutme";
  public static final String ATTR_AUTHOR_QUOTE = "author_quote";
  public static final String ATTR_AUTHOR_LINK = "author_link";
  public static final String ATTR_SHOW_STATS = "show_stats";
  public static final String ATTR_SHOW_IN_DIRECTORY = "show_in_directory";
  public static final String ATTR_SINGLETON = "singleton";
  public static final String ATTR_SCALING = "scaling";
  public static final String ATTR_SCROLLING = "scrolling";
  public static final String ATTR_WIDTH = "width";
  public static final String ATTR_HEIGHT = "height";
  public static final String ATTR_CATEGORY = "category";
  public static final String ATTR_CATEGORY2 = "category2";
  public static final String ATTR_RENDER_INLINE = "render_inline";

  private final Map<String, Feature> features = new HashMap<String, Feature>();
  private final Map<Locale, LocaleSpec> locales = new HashMap<Locale, LocaleSpec>();
  private final List<Preload> preloads = new ArrayList<Preload>();
  private final List<Icon> icons = new ArrayList<Icon>();
  private final Map<String, LinkSpec> links = new HashMap<String, LinkSpec>();

  private OAuthSpec oauth = null;

  public ModulePrefs(final QName name, final Map<String, QName> attrNames,
      final Uri base) {
    super(name, attrNames, base);
  }

  /**
   * Produces a new, substituted ModulePrefs
   */
  protected ModulePrefs(final ModulePrefs prefs, final Substitutions substituter) {
    super(prefs, substituter);

    for (Feature feature : prefs.getFeatures().values()) {
      addFeature(feature);
    }

    for (LocaleSpec locale : prefs.getLocales().values()) {
      addLocale(locale);
    }

    for (Preload preload : prefs.getPreloads()) {
      addPreload(preload.substitute(substituter));
    }

    for (Icon icon : prefs.getIcons()) {
      addIcon(icon.substitute(substituter));
    }

    for (LinkSpec link : prefs.getLinks().values()) {
      addLink(link.substitute(substituter));
    }

    this.oauth = prefs.getOAuth();

    // That is actually gross...
    for (Map.Entry<String, String> entry: prefs.attributes().entrySet()) {
      setAttr(entry.getKey(), substituter.substituteString(entry.getValue()));
    }
  }

  /**
   * Produces a new ModulePrefs by substituting hangman variables from
   * substituter. See comments on individual fields to see what actually has
   * substitutions performed.
   *
   * @param substituter
   */
  @Override
  public ModulePrefs substitute(final Substitutions substituter) {
    return new ModulePrefs(this, substituter);
  }

  public List<Preload> getPreloads() {
    return Collections.unmodifiableList(preloads);
  }

  public Map<String, Feature> getFeatures() {
    return Collections.unmodifiableMap(features);
  }

  public List<Icon> getIcons() {
    return Collections.unmodifiableList(icons);
  }

  public Map<Locale, LocaleSpec> getLocales() {
    return Collections.unmodifiableMap(locales);
  }

  public Map<String, LinkSpec> getLinks() {
    return Collections.unmodifiableMap(links);
  }

  public OAuthSpec getOAuth() {
    return oauth;
  }

  public String getTitle() {
    return attrDefault(ATTR_TITLE);
  }

  public Uri getTitleUrl() {
      return attrUriBase(ATTR_TITLE_URL);
  }

  public String getDescription() {
    return attrDefault(ATTR_DESCRIPTION);
  }

  public String getAuthor() {
    return attrDefault(ATTR_AUTHOR);
  }

  public String getAuthorEmail() {
    return attrDefault(ATTR_AUTHOR_EMAIL);
  }

  public Uri getScreenshot() {
    return attrUriBase(ATTR_SCREENSHOT);
  }

  public Uri getThumbnail() {
    return attrUriBase(ATTR_THUMBNAIL);
  }

  public String getDirectoryTitle() {
    return attrDefault(ATTR_DIRECTORY_TITLE);
  }

  public String getAuthorAffiliation() {
    return attrDefault(ATTR_AUTHOR_AFFILIATION);
  }

  public String getAuthorLocation() {
    return attrDefault(ATTR_AUTHOR_LOCATION);
  }

  public Uri getAuthorPhoto() {
    return attrUriBase(ATTR_AUTHOR_PHOTO);
  }

  public String getAuthorAboutme() {
    return attrDefault(ATTR_AUTHOR_ABOUTME);
  }

  public String getAuthorQuote() {
    return attrDefault(ATTR_AUTHOR_QUOTE);
  }

  public Uri getAuthorLink() {
    return attrUriBase(ATTR_AUTHOR_LINK);
  }

  public boolean isShowStats() {
    return attrBool(ATTR_SHOW_STATS);
  }

  public boolean isShowInDirectory() {
    return attrBool(ATTR_SHOW_IN_DIRECTORY);
  }

  public boolean isSingleton() {
    return attrBool(ATTR_SINGLETON);
  }

  public boolean isScaling() {
    return attrBool(ATTR_SCALING);
  }

  public boolean isScrolling() {
    return attrBool(ATTR_SCROLLING);
  }

  public int getWidth() {
    return attrInt(ATTR_WIDTH);
  }

  public int getHeight() {
    return attrInt(ATTR_HEIGHT);
  }

  public boolean isRenderInline() {
    return attrBool(ATTR_RENDER_INLINE);
  }

  public List<String> getCategories() {
    List<String> categories = new ArrayList<String>(2);
    if (attr(ATTR_CATEGORY) != null) {
      categories.add(attr(ATTR_CATEGORY));
    }
    if (attr(ATTR_CATEGORY2) != null) {
      categories.add(attr(ATTR_CATEGORY2));
    }
    return Collections.unmodifiableList(categories);
  }

  public LocaleSpec getLocale(final Locale locale) {
    if (locales.isEmpty()) {
      return null;
    }

    LocaleSpec localeSpec = locales.get(locale);
    if (localeSpec == null) {
      Locale allLocale = new Locale(locale.getLanguage(), "ALL");
      localeSpec = locales.get(allLocale);
      if (allLocale == null) {
        localeSpec = locales.get(GadgetSpec.DEFAULT_LOCALE);
      }
    }
    return localeSpec;
  }

  private void addPreload(final Preload preload) {
    preloads.add(preload);
  }

  private void addFeature(final Feature feature) {
    features.put(feature.getFeature(), feature);
  }

  private void addIcon(final Icon icon) {
    icons.add(icon);
  }

  private void addLocale(final LocaleSpec locale) {
    locales.put(new Locale(locale.getLanguage(), locale.getCountry()), locale);
  }

  private void addLink(final LinkSpec link) {
    links.put(link.getRel().toString(), link);
  }

  private void setOAuth(final OAuthSpec oauth) {
    this.oauth = oauth;
  }

  @Override
  protected void writeChildren(final XMLStreamWriter writer)
      throws XMLStreamException {
    for (Preload preload : preloads) {
      preload.toXml(writer);
    }
    for (Feature feature : features.values()) {
      feature.toXml(writer);
    }
    for (Icon icon : icons) {
      icon.toXml(writer);
    }
    for (LocaleSpec locale : locales.values()) {
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
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (attr(ATTR_TITLE) != null) {
      writer.writeAttribute(namespaceURI, ATTR_TITLE, getTitle());
    }

    if (attr(ATTR_TITLE_URL) != null) {
      writer.writeAttribute(namespaceURI, ATTR_TITLE_URL, getTitleUrl()
          .toString());
    }

    if (attr(ATTR_DESCRIPTION) != null) {
      writer.writeAttribute(namespaceURI, ATTR_DESCRIPTION, getDescription());
    }

    if (attr(ATTR_AUTHOR) != null) {
      writer.writeAttribute(namespaceURI, ATTR_AUTHOR, getAuthor());
    }

    if (attr(ATTR_AUTHOR_EMAIL) != null) {
      writer.writeAttribute(namespaceURI, ATTR_AUTHOR_EMAIL, getAuthorEmail());
    }

    if (attr(ATTR_SCREENSHOT) != null) {
      writer.writeAttribute(namespaceURI, ATTR_SCREENSHOT, getScreenshot()
          .toString());
    }

    if (attr(ATTR_THUMBNAIL) != null) {
      writer.writeAttribute(namespaceURI, ATTR_THUMBNAIL, getThumbnail()
          .toString());
    }

    if (attr(ATTR_DIRECTORY_TITLE) != null) {
      writer.writeAttribute(namespaceURI, ATTR_DIRECTORY_TITLE,
          getDirectoryTitle());
    }

    if (attr(ATTR_AUTHOR_AFFILIATION) != null) {
      writer.writeAttribute(namespaceURI, ATTR_AUTHOR_AFFILIATION,
          getAuthorAffiliation());
    }

    if (attr(ATTR_AUTHOR_LOCATION) != null) {
      writer.writeAttribute(namespaceURI, ATTR_AUTHOR_LOCATION,
          getAuthorLocation());
    }

    if (attr(ATTR_AUTHOR_PHOTO) != null) {
      writer.writeAttribute(namespaceURI, ATTR_AUTHOR_PHOTO, getAuthorPhoto()
          .toString());
    }

    if (attr(ATTR_AUTHOR_ABOUTME) != null) {
      writer.writeAttribute(namespaceURI, ATTR_AUTHOR_ABOUTME,
          getAuthorAboutme());
    }

    if (attr(ATTR_AUTHOR_QUOTE) != null) {
      writer.writeAttribute(namespaceURI, ATTR_AUTHOR_QUOTE, getAuthorQuote());
    }

    if (attr(ATTR_AUTHOR_LINK) != null) {
      writer.writeAttribute(namespaceURI, ATTR_AUTHOR_LINK, getAuthorLink()
          .toString());
    }

    if (attr(ATTR_SHOW_STATS) != null) {
      writer.writeAttribute(namespaceURI, ATTR_SHOW_STATS, String
          .valueOf(isShowStats()));
    }

    if (attr(ATTR_SHOW_IN_DIRECTORY) != null) {
      writer.writeAttribute(namespaceURI, ATTR_SHOW_IN_DIRECTORY, String
          .valueOf(isShowInDirectory()));
    }

    if (attr(ATTR_SINGLETON) != null) {
      writer.writeAttribute(namespaceURI, ATTR_SINGLETON, String
          .valueOf(isSingleton()));
    }

    if (attr(ATTR_SCALING) != null) {
      writer.writeAttribute(namespaceURI, ATTR_SCALING, String
          .valueOf(isScaling()));
    }

    if (attr(ATTR_SCROLLING) != null) {
      writer.writeAttribute(namespaceURI, ATTR_SCROLLING, String
          .valueOf(isScrolling()));
    }

    if (attr(ATTR_WIDTH) != null) {
      writer.writeAttribute(namespaceURI, ATTR_WIDTH, String
          .valueOf(getWidth()));
    }

    if (attr(ATTR_HEIGHT) != null) {
      writer.writeAttribute(namespaceURI, ATTR_HEIGHT, String
          .valueOf(getHeight()));
    }

    if (attr(ATTR_RENDER_INLINE) != null) {
      writer.writeAttribute(namespaceURI, ATTR_RENDER_INLINE, String
          .valueOf(isRenderInline()));
    }

    final List<String> categories = getCategories();
    if (categories.size() > 0) {
      writer.writeAttribute(namespaceURI, ATTR_CATEGORY, categories.get(0));
      if (categories.size() > 1) {
        writer.writeAttribute(namespaceURI, ATTR_CATEGORY2, categories.get(1));
      }
    }
  }

  @Override
  public void validate() throws GadgetException {
    if (attr(ATTR_TITLE) == null) {
      throw new SpecParserException("ModulePrefs@title must be set!");
    }
    if (!attrIsValidUri(ATTR_TITLE_URL)) {
      throw new SpecParserException("Messages URI '" + attr(ATTR_TITLE_URL) + "' is invalid!");
    }
    if (!attrIsValidUri(ATTR_SCREENSHOT)) {
      throw new SpecParserException("Messages URI '" + attr(ATTR_SCREENSHOT) + "' is invalid!");
    }
    if (!attrIsValidUri(ATTR_THUMBNAIL)) {
      throw new SpecParserException("Messages URI '" + attr(ATTR_THUMBNAIL) + "' is invalid!");
    }
    if (!attrIsValidUri(ATTR_AUTHOR_PHOTO)) {
      throw new SpecParserException("Messages URI '" + attr(ATTR_AUTHOR_PHOTO) + "' is invalid!");
    }
    if (!attrIsValidUri(ATTR_AUTHOR_LINK)) {
      throw new SpecParserException("Messages URI '" + attr(ATTR_AUTHOR_LINK) + "' is invalid!");
    }
  }

  public static class Parser extends SpecElement.Parser<ModulePrefs> {

    public Parser(final Uri base) {
      this(new QName(ELEMENT_NAME), base);
    }

    public Parser(final QName name, final Uri base) {
      super(name, base);

      register(new Require.Parser(base));
      register(new Optional.Parser(base));
      register(new Preload.Parser(base));
      register(new Icon.Parser(base));
      register(new LocaleSpec.Parser(base));
      register(new LinkSpec.Parser(base));
      register(new OAuthSpec.Parser(base));

      register(ATTR_TITLE, ATTR_TITLE_URL, ATTR_DESCRIPTION, ATTR_AUTHOR,
          ATTR_AUTHOR_EMAIL, ATTR_SCREENSHOT, ATTR_THUMBNAIL,
          ATTR_DIRECTORY_TITLE, ATTR_AUTHOR_AFFILIATION, ATTR_AUTHOR_LOCATION,
          ATTR_AUTHOR_PHOTO, ATTR_AUTHOR_ABOUTME, ATTR_AUTHOR_QUOTE,
          ATTR_AUTHOR_LINK, ATTR_SHOW_STATS, ATTR_SHOW_IN_DIRECTORY,
          ATTR_SINGLETON, ATTR_SCALING, ATTR_SCROLLING, ATTR_WIDTH,
          ATTR_HEIGHT, ATTR_RENDER_INLINE, ATTR_CATEGORY, ATTR_CATEGORY2);
    }

    @Override
    protected ModulePrefs newElement() {
      return new ModulePrefs(name(), getAttrNames(), getBase());
    }

    @Override
    protected void addChild(final XMLStreamReader reader,
        final ModulePrefs prefs, final SpecElement child)
        throws GadgetException {
      if (child instanceof Feature) {
        prefs.addFeature((Feature) child);
      } else if (child instanceof Preload) {
        prefs.addPreload((Preload) child);
      } else if (child instanceof Icon) {
        prefs.addIcon((Icon) child);
      } else if (child instanceof LocaleSpec) {
        prefs.addLocale((LocaleSpec) child);
      } else if (child instanceof LinkSpec) {
        prefs.addLink((LinkSpec) child);
      } else if (child instanceof OAuthSpec) {
        prefs.setOAuth((OAuthSpec) child);
      } else {
        super.addChild(reader, prefs, child);
      }
    }
  }
}
