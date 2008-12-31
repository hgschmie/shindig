/*
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
 */

package org.apache.shindig.gadgets.spec;

import java.util.Map;

import junit.framework.TestCase;

import org.apache.shindig.gadgets.stax.StaxTestUtils;

public class FeatureTest extends TestCase {
  public void testRequire() throws Exception {
    String xml = "<Require feature=\"foo\"/>";
    Feature feature = StaxTestUtils.parseElement(xml, new Require.Parser(null));
    assertEquals("foo", feature.getFeature());
    assertEquals(true, feature.isRequired());
  }

  public void testOptional() throws Exception {
    String xml = "<Optional feature=\"foo\"/>";
    Feature feature = StaxTestUtils.parseElement(xml, new Optional.Parser(null));
    assertEquals("foo", feature.getFeature());
    assertEquals(false, feature.isRequired());
  }

  public void testParams() throws Exception {
    String key = "bar";
    String value = "Hello, World!";
    String xml = "<Require feature=\"foo\">" +
                 "  <Param name=\"" + key + "\">" + value + "</Param>" +
                 "</Require>";
    Feature feature = StaxTestUtils.parseElement(xml, new Require.Parser(null));
    Map<String, FeatureParam> params = feature.getParams();
    assertEquals(1, params.size());
    assertEquals(value, params.get(key).getText());
  }

  public void testDoesNotLikeUnnamedFeatures() throws Exception {
    String xml = "<Require/>";
    try {
      StaxTestUtils.parseElement(xml, new Require.Parser(null));
      fail("No exception thrown when an unnamed feature is passed.");
    } catch (SpecParserException e) {
      // Ok
    }
  }

  public void testEnforceParamNames() throws Exception {
    String xml = "<Require feature=\"foo\"><Param>Test</Param></Require>";
    try {
      StaxTestUtils.parseElement(xml, new Require.Parser(null));
      fail("No exception thrown when an unnamed parameter is passed.");
    } catch (SpecParserException e) {
      // OK.
    }
  }
}
