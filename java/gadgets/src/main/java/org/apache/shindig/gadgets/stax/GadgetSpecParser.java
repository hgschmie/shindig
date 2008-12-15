package org.apache.shindig.gadgets.stax;
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
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.Content;
import org.apache.shindig.gadgets.stax.model.GadgetSpec;
import org.apache.shindig.gadgets.stax.model.ModulePrefs;
import org.apache.shindig.gadgets.stax.model.SpecElement;
import org.apache.shindig.gadgets.stax.model.UserPref;



public class GadgetSpecParser extends AbstractSpecElementParser<GadgetSpec>
{
    public GadgetSpecParser() {
        super(new QName("Module"));
        register(new ModulePrefsParser());
        register(new UserPrefParser());
        register(new ContentParser());
    }

    @Override
    protected GadgetSpec newElement()
    {
        return new GadgetSpec(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, GadgetSpec spec, SpecElement child)
    {
        if (child instanceof ModulePrefs) {
            spec.setModulePrefs((ModulePrefs) child);
        } else if (child instanceof UserPref) {
            spec.addUserPref((UserPref) child);
        } else if (child instanceof Content) {
            spec.setContent((Content) child);
        } else {
            throw new IllegalArgumentException("Can not add " + child.getClass().getName() + " to " + spec.getClass().getName());
        }
    }

    @Override
    public void validate(final GadgetSpec element) throws SpecParserException
    {
        if (element.getModulePrefs() == null) {
            throw new SpecParserException("No <ModulePrefs> section found!");
        }
        if (element.getContent() == null) {
            throw new SpecParserException("No <Content> section found!");
        }
    }
}
