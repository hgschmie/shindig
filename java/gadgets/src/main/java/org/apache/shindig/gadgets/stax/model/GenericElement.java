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

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.variables.Substitutions;

public class GenericElement extends SpecElement {

  private StringBuilder text = new StringBuilder();

  public GenericElement(final QName name, final Map<String, QName> attrNames,
      final Uri base) {
    super(name, attrNames, base);
  }

  protected GenericElement(final GenericElement genericElement, final Substitutions substituter) {
      super(genericElement);
  }

  @Override
  public GenericElement substitute(final Substitutions substituter) {
    return new GenericElement(this, substituter);
  }

  @Override
  public String getText() {
    return text.toString();
  }

  private void addText(final String text) {
    this.text.append(text);
  }

  public static class Parser extends SpecElement.Parser<GenericElement> {
    public Parser(final QName name, final Uri base) {
      super(name, base);
    }

    @Override
    protected GenericElement newElement() {
      return new GenericElement(name(), getAttrNames(), getBase());
    }

    @Override
    protected void addText(final XMLStreamReader reader,
        final GenericElement element) {
      if (!reader.isWhiteSpace()) {
        element.addText(reader.getText());
      }
    }

    @Override
    protected void addChild(XMLStreamReader reader,
        final GenericElement element, SpecElement child) {
      element.addChild(child);
    }
  }
}
