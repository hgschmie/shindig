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
import javax.xml.stream.XMLStreamWriter;

public class GadgetSpec extends AbstractSpecElement {

    private ModulePrefs modulePrefs = null;

    private List<UserPref> userPrefs = new ArrayList<UserPref>();

    private Content content = null;

    public GadgetSpec(final QName name) {
        super(name);
    }

    public void setModulePrefs(final ModulePrefs modulePrefs)
    {
        if (!isSealed()) {
            this.modulePrefs = modulePrefs;
        }
    }

    public void addUserPref(final UserPref userPref)
    {
        if (!isSealed()) {
            this.userPrefs.add(userPref);
        }
    }

    public void setContent(final Content content)
    {
        if (!isSealed()) {
            this.content = content;
        }
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

    @Override
    protected void addXml(final XMLStreamWriter writer) throws XMLStreamException
    {
        if (modulePrefs != null) {
            modulePrefs.toXml(writer);
        }
        for (UserPref pref: userPrefs) {
            pref.toXml(writer);
        }
        if (content != null) {
            content.toXml(writer);
        }
    }
}
