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
import org.apache.shindig.gadgets.stax.model.EnumValue;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class EnumValueParser extends AbstractSpecElementParser<EnumValue>
{
    public EnumValueParser() {
        this(new QName("EnumValue"));
    }

    public EnumValueParser(final QName name) {
        super(name);
    }

    @Override
    protected EnumValue newElement()
    {
        return new EnumValue(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, EnumValue prefs, SpecElement child)
    {
    }

    @Override
    public void validate(EnumValue element) throws SpecParserException
    {
    }
}
