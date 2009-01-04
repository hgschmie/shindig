package org.apache.shindig.gadgets.spec;

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

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.variables.Substitutions;

import java.util.Map;

import javax.xml.namespace.QName;

public class Require extends Feature {

  public static final QName ELEMENT_NAME = new QName(SpecElement.OPENSOCIAL_NAMESPACE_URI, "Require");

  protected Require(final QName name, final Map<String, QName> attrNames, final Uri base) {
    super(name, attrNames, base, true);
  }

  protected Require(final Require require, final Substitutions substituter) {
    super(require, substituter);
  }

  @Override
  public Require substitute(final Substitutions substituter) {
    return new Require(this, substituter);
  }

  public static class Parser extends Feature.Parser {
    public Parser(final Uri base) {
      this(ELEMENT_NAME, base);
    }

    public Parser(final QName name, final Uri base) {
      super(name, base);
    }

    @Override
    protected Require newElement() {
      return new Require(name(), getAttrNames(), getBase());
    }
  }
}
