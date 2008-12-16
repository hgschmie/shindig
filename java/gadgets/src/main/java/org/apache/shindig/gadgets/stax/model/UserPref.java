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


import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.gadgets.spec.SpecParserException;

public class UserPref extends AbstractSpecElement {

    public UserPref(final QName name) {
        super(name);
    }

    @Override
    protected void addXml(XMLStreamWriter writer)
    {
    }

    public static class Parser extends SpecElement.Parser<UserPref>
    {
        public Parser() {
            this(new QName("UserPref"));
            register(new EnumValue.Parser());
        }

        public Parser(final QName name) {
            super(name);
        }

        @Override
        protected UserPref newElement()
        {
            return new UserPref(getName());
        }

        @Override
        protected void addChild(XMLStreamReader reader, UserPref prefs, SpecElement child)
        {
        }

        @Override
        public void validate(UserPref element) throws SpecParserException
        {
        }
    }
}