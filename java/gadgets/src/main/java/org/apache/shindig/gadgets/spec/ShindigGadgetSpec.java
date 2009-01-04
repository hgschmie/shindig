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
package org.apache.shindig.gadgets.spec;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.variables.Substitutions;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class ShindigGadgetSpec extends SpecElement implements GadgetSpec {

  public static final QName ELEMENT_NAME = new QName(SpecElement.OPENSOCIAL_NAMESPACE_URI, "Module");

  private final String checksum;

  private Multimap<String, Content> viewMap = new LinkedListMultimap<String, Content>();

  private ConcurrentHashMap<String, View> views = new ConcurrentHashMap<String, View>();

  /**
   * A map of attributes associated with the instance of the spec Used by handler classes to use specs to carry context.
   * Not defined by the specification
   */
  private final Map<String, Object> specAttributes = new ConcurrentHashMap<String, Object>();

  private ModulePrefs modulePrefs = null;

  private List<UserPref> userPrefs = new ArrayList<UserPref>();

  private List<Content> contents = new ArrayList<Content>();

  public ShindigGadgetSpec(final QName name, final Uri base, final String checksum) {
    super(name, Collections.<String, QName> emptyMap(), base);
    this.checksum = checksum;
  }

  protected ShindigGadgetSpec(final ShindigGadgetSpec gadgetSpec, final Substitutions substituter)
      throws GadgetException {
    super(gadgetSpec, substituter);

    setModulePrefs(gadgetSpec.getModulePrefs().substitute(substituter));

    for (final UserPref pref : gadgetSpec.getUserPrefs()) {
      addUserPref(pref.substitute(substituter));
    }

    for (final Content content : gadgetSpec.getContents()) {
      addContent(content.substitute(substituter));
    }

    this.checksum = gadgetSpec.getChecksum();
  }

  @Override
  public ShindigGadgetSpec substitute(final Substitutions substituter) throws GadgetException {
    return new ShindigGadgetSpec(this, substituter);
  }

  public String getChecksum() {
    return checksum;
  }

  public Uri getUrl() {
    return getBase();
  }

  // ========================================================================

  public Object getSpecAttribute(final String key) {
    return specAttributes.get(key);
  }

  public void setSpecAttribute(final String key, final Object o) {
    specAttributes.put(key, o);
  }

  // ========================================================================

  public ModulePrefs getModulePrefs() {
    return modulePrefs;
  }

  public List<UserPref> getUserPrefs() {
    return Collections.unmodifiableList(userPrefs);
  }

  public List<Content> getContents() {
    return Collections.unmodifiableList(contents);
  }

  private void setModulePrefs(final ModulePrefs modulePrefs) throws GadgetException {
    if (this.modulePrefs != null) {
      throw new SpecParserException("Multiple <ModulePrefs> elements encountered!");
    }
    this.modulePrefs = modulePrefs;
  }

  private void addUserPref(final UserPref userPref) {
    this.userPrefs.add(userPref);
  }

  private void addContent(final Content content) throws SpecParserException {
    contents.add(content);

    for (String viewName : content.getViews()) {
      viewMap.put(viewName, content);
      views.put(viewName, new View(viewName, viewMap.get(viewName), Uri.EMPTY_URI));
    }
  }

  public View getView(final String name) {
    return views.get(name);
  }

  public Map<String, View> getViews() {
    return Collections.unmodifiableMap(views);
  }

  // ========================================================================

  @Override
  protected void writeChildren(final XMLStreamWriter writer) throws XMLStreamException {
    if (modulePrefs != null) {
      modulePrefs.toXml(writer);
    }
    for (UserPref pref : userPrefs) {
      pref.toXml(writer);
    }
    for (Content content : contents) {
      content.toXml(writer);
    }
  }

  @Override
  public void validate() throws SpecParserException {
    // TODO - according to the spec, this is actually wrong.
    if (modulePrefs == null) {
      throw new SpecParserException(name().getLocalPart() + " needs a ModulePrefs section!");
    }
    if (contents.size() == 0) {
      throw new SpecParserException(name().getLocalPart() + " needs a Content section!");
    }
  }

  // ========================================================================

  public static class Parser<T extends ShindigGadgetSpec> extends SpecElement.Parser<ShindigGadgetSpec> {
    private final String checksum;

    public Parser(final Uri base, final String checksum) {
      this(ELEMENT_NAME, base, checksum);
    }

    public Parser(final QName name, final Uri base, final String checksum) {
      super(name, base);
      register(new ModulePrefs.Parser(base));
      register(new UserPref.Parser(base));
      register(new Content.Parser(base));

      this.checksum = checksum;
    }

    @Override
    protected ShindigGadgetSpec newElement() {
      return new ShindigGadgetSpec(name(), getBase(), checksum);
    }

    @Override
    public ShindigGadgetSpec parse(final XMLStreamReader reader) throws XMLStreamException, GadgetException {
      return (ShindigGadgetSpec) super.parse(reader);
    }

    @Override
    protected void addChild(final XMLStreamReader reader, final ShindigGadgetSpec spec, final SpecElement child)
        throws GadgetException {
      if (child instanceof ModulePrefs) {
        spec.setModulePrefs((ModulePrefs) child);
      } else if (child instanceof UserPref) {
        spec.addUserPref((UserPref) child);
      } else if (child instanceof Content) {
        spec.addContent((Content) child);
      } else {
        super.addChild(reader, spec, child);
      }
    }
  }
}
