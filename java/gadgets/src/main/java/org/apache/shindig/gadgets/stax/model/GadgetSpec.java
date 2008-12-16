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
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.gadgets.spec.SpecParserException;

public class GadgetSpec extends AbstractSpecElement {

  private ModulePrefs modulePrefs = null;

  private List<UserPref> userPrefs = new ArrayList<UserPref>();

  private Content content = null;

  public GadgetSpec(final QName name) {
    super(name);
  }

  public ModulePrefs getModulePrefs() {
    return modulePrefs;
  }

  public List<UserPref> getUserPrefs() {
    return userPrefs;
  }

  public Content getContent() {
    return content;
  }

  private void setModulePrefs(final ModulePrefs modulePrefs) {
    this.modulePrefs = modulePrefs;
  }

  private void addUserPref(final UserPref userPref) {
    this.userPrefs.add(userPref);
  }

  private void setContent(final Content content) {
    this.content = content;
  }

  @Override
  protected void addXml(final XMLStreamWriter writer) throws XMLStreamException {
    if (modulePrefs != null) {
      modulePrefs.toXml(writer);
    }
    for (UserPref pref : userPrefs) {
      pref.toXml(writer);
    }
    if (content != null) {
      content.toXml(writer);
    }
  }

  public static class Parser extends AbstractSpecElement.Parser<GadgetSpec> {
    public Parser() {
      this(new QName("Module"));
    }

    public Parser(final QName name) {
      super(name);
      register(new ModulePrefs.Parser());
      register(new UserPref.Parser());
      register(new Content.Parser());
    }

    @Override
    protected GadgetSpec newElement() {
      return new GadgetSpec(getName());
    }

    @Override
    protected void addChild(final XMLStreamReader reader, final GadgetSpec spec,
        final AbstractSpecElement child) {
      if (child instanceof ModulePrefs) {
        spec.setModulePrefs((ModulePrefs) child);
      } else if (child instanceof UserPref) {
        spec.addUserPref((UserPref) child);
      } else if (child instanceof Content) {
        spec.setContent((Content) child);
      } else {
        super.addChild(reader, spec, child);
      }
    }

    @Override
    public void validate(final GadgetSpec element) throws SpecParserException {
      if (element.getModulePrefs() == null) {
        throw new SpecParserException("No <ModulePrefs> section found!");
      }
      if (element.getContent() == null) {
        throw new SpecParserException("No <Content> section found!");
      }
    }
  }
}
