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

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.StaxTestUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NamespaceGadgetSpecTest  {
  private static final Uri SPEC_URL = Uri.parse("http://example.org/g.xml");

  @Test
  public void testParseBasic() throws Exception {
    String xml = "<Module xmlns:conf=\"http://mynamespace\">" +
                 "<ModulePrefs title=\"title\" />" +
                 "<UserPref name=\"foo\" datatype=\"STRING\" />" +
                 "<Content type=\"html\">Hello!</Content>" +
                 "<conf:foo conf:blo=\"blu\" bar=\"baz\">content</conf:foo>" +
                 "</Module>";
    GadgetSpec spec = StaxTestUtils.parseSpec(xml, SPEC_URL);
    assertEquals("title", spec.getModulePrefs().getTitle());
    assertEquals(UserPref.DataType.STRING,
        spec.getUserPrefs().get(0).getDataType());
    assertEquals("Hello!", spec.getView(GadgetSpec.DEFAULT_VIEW).getContent());

    final String rebuilt = spec.toString();
    assertEquals(xml, rebuilt);
  }

  @Test
  public void testParseBasicNoNS() throws Exception {
    String xml = "<Module><ModulePrefs title=\"title\" />"
      + "<UserPref name=\"foo\" datatype=\"STRING\" />"
      + "<Content type=\"html\">Hello!</Content>"
      + "<conf:foo conf:blo=\"blu\" bar=\"baz\">"
      + "content</conf:foo></Module>";

    GadgetSpec spec = StaxTestUtils.parseSpec(xml, SPEC_URL);
    assertEquals("title", spec.getModulePrefs().getTitle());
    assertEquals(UserPref.DataType.STRING,
        spec.getUserPrefs().get(0).getDataType());
    assertEquals("Hello!", spec.getView(GadgetSpec.DEFAULT_VIEW).getContent());

    final String rebuilt = spec.toString();
    assertEquals(xml, rebuilt);
  }

  /*
  @Test
  public void testParseExplicitNamespace() throws Exception {
    String xml = "<osoc:Module xmlns:osoc=\"" + SpecElement.GADGET_SPEC_NAMESPACE_URI + "\" xmlns:conf=\"http://mynamespace\">" +
                 "<osoc:ModulePrefs title=\"title\" />" +
                 "<osoc:UserPref name=\"foo\" datatype=\"STRING\" />" +
                 "<osoc:Content type=\"html\">Hello!</osoc:Content>" +
                 "<conf:foo conf:blo=\"blu\" bar=\"baz\">content</conf:foo>" +
                 "</osoc:Module>";
    GadgetSpec spec = StaxTestUtils.parseSpec(xml, SPEC_URL);
    assertEquals("title", spec.getModulePrefs().getTitle());
    assertEquals(UserPref.DataType.STRING,
        spec.getUserPrefs().get(0).getDataType());
    assertEquals("Hello!", spec.getView(GadgetSpec.DEFAULT_VIEW).getContent());

    final String rebuilt = spec.toString();
    assertEquals(xml, rebuilt);
  }
  */

  @Test
  public void toStringIsSane() throws Exception {
    String xml = "<Module xmlns:conf=\"http://mynamespace\">" +
                 "<ModulePrefs title=\"title\" />" +
                 "<UserPref name=\"foo\" datatype=\"STRING\" />" +
                 "<Content type=\"html\">Hello!</Content>" +
                 "<conf:foo conf:blo=\"blu\" bar=\"baz\">content</conf:foo>" +
                 "</Module>";
    GadgetSpec spec = StaxTestUtils.parseSpec(xml, SPEC_URL);
    GadgetSpec spec2 = StaxTestUtils.parseSpec(spec.toString(), SPEC_URL);
    assertEquals("title", spec2.getModulePrefs().getTitle());
    assertEquals(UserPref.DataType.STRING,
        spec2.getUserPrefs().get(0).getDataType());
    assertEquals("Hello!", spec2.getView(GadgetSpec.DEFAULT_VIEW).getContent());

    final String rebuilt = spec.toString();
    assertEquals(xml, rebuilt);
  }
}


