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
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.variables.Substitutions;

public class StaxGadgetSpec extends SpecElement {

  public static final String ELEMENT_NAME = "Module";

  private ModulePrefs modulePrefs = null;

  private List<UserPref> userPrefs = new ArrayList<UserPref>();

  private List<Content> contents = new ArrayList<Content>();

    public StaxGadgetSpec(final QName name, final Uri base) {
    super(name, Collections.<String, QName> emptyMap(), base);
  }

  protected StaxGadgetSpec(final StaxGadgetSpec gadgetSpec, final Substitutions substituter) {
    super(gadgetSpec);

    setModulePrefs(gadgetSpec.getModulePrefs().substitute(substituter));
    for (final UserPref pref : gadgetSpec.getUserPrefs()) {
        addUserPref(pref.substitute(substituter));
    }
  }

  /**
   * Performs substitutions on the spec. See individual elements for
   * details on what gets substituted.
   *
   * @param substituter
   * @return The substituted spec.
   */
  public StaxGadgetSpec substitute(final Substitutions substituter) {
      return new StaxGadgetSpec(this, substituter);
  }

  public ModulePrefs getModulePrefs() {
    return modulePrefs;
  }

  public List<UserPref> getUserPrefs() {
    return Collections.unmodifiableList(userPrefs);
  }

  public List<Content> getContents() {
    return Collections.unmodifiableList(contents);
  }

  private void setModulePrefs(final ModulePrefs modulePrefs) {
    this.modulePrefs = modulePrefs;
  }

  private void addUserPref(final UserPref userPref) {
    this.userPrefs.add(userPref);
  }

  protected void addContent(final Content content) throws SpecParserException {
    contents.add(content);
  }

  @Override
  protected void writeChildren(final XMLStreamWriter writer)
      throws XMLStreamException {
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
      throw new SpecParserException(name().getLocalPart()
          + " needs a ModulePrefs section!");
    }
    if (contents.size() == 0) {
      throw new SpecParserException(name().getLocalPart()
          + " needs a Content section!");
    }
  }

  public static class Parser<T extends StaxGadgetSpec> extends
      SpecElement.Parser<StaxGadgetSpec> {

    public Parser(final Uri url) {
      this(new QName(ELEMENT_NAME), url);
    }

    public Parser(final QName name, final Uri base) {
      super(name, base);
      register(new ModulePrefs.Parser(base));
      register(new UserPref.Parser(base));
      register(new Content.Parser(base));
    }

    @Override
    protected StaxGadgetSpec newElement() {
      return new StaxGadgetSpec(name(), getBase());
    }

    @Override
    protected void addChild(final XMLStreamReader reader,
        final StaxGadgetSpec spec, final SpecElement child)
        throws SpecParserException {
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
