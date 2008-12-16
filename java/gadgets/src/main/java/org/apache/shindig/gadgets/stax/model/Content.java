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
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.gadgets.spec.SpecParserException;

public class Content extends SpecElement {

  public Content(final QName name) {
    super(name);
  }

  @Override
  protected void addXml(XMLStreamWriter writer) {
  }

  public static class Parser extends SpecElement.Parser<Content> {
    public Parser() {
      this(new QName("Content"));
    }

    public Parser(final QName name) {
      super(name);
    }

    @Override
    protected Content newElement() {
      return new Content(getName());
    }

    @Override
    public void validate(Content element) throws SpecParserException {
    }
  }
}
