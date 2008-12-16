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

public class GenericElement extends AbstractSpecElement {

  private List<AbstractSpecElement> children = new ArrayList<AbstractSpecElement>();

  public GenericElement(final QName name) {
    super(name);
  }

  protected void addChild(final AbstractSpecElement child) {
    this.children.add(child);
  }

  public List<AbstractSpecElement> getChildren() {
    return children;
  }

  @Override
  protected void addXml(final XMLStreamWriter writer) throws XMLStreamException {
    for (AbstractSpecElement child : children) {
      child.toXml(writer);
    }
  }

  public static class Parser extends AbstractSpecElement.Parser<GenericElement> {
    public Parser(final QName name) {
      super(name);
    }

    @Override
    protected GenericElement newElement() {
      return new GenericElement(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader,
        final GenericElement element, AbstractSpecElement child) {
      element.addChild(child);
    }

    @Override
    public void validate(GenericElement element) throws SpecParserException {
    }
  }
}
