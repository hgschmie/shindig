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

import javax.xml.stream.XMLStreamException;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.StaxTestUtils;
import org.apache.shindig.gadgets.variables.Substitutions;
import org.apache.shindig.gadgets.variables.Substitutions.Type;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GadgetSpecTest {
  private static final Uri SPEC_URL = Uri.parse("http://example.org/g.xml");

  @Test
  public void testBasic() throws Exception {
    String xml = "<Module>" + "<ModulePrefs title=\"title\"/>"
        + "<UserPref name=\"foo\" datatype=\"string\"/>"
        + "<Content type=\"html\">Hello!</Content>" + "</Module>";
    GadgetSpec spec = StaxTestUtils.parseSpec(xml, SPEC_URL);
    assertEquals("title", spec.getModulePrefs().getTitle());
    assertEquals(UserPref.DataType.STRING, spec.getUserPrefs().get(0)
        .getDataType());
    assertEquals("Hello!", spec.getView(GadgetSpec.DEFAULT_VIEW).getContent());
  }

  @Test
  public void testMultipleContentSections() throws Exception {
    String xml = "<Module>" + "<ModulePrefs title=\"title\"/>"
        + "<Content type=\"html\" view=\"hello\">hello </Content>"
        + "<Content type=\"html\" view=\"world\">world</Content>"
        + "<Content type=\"html\" view=\"hello, test\">test</Content>"
        + "</Module>";
    GadgetSpec spec = StaxTestUtils.parseSpec(xml, SPEC_URL);
    assertEquals("hello test", spec.getView("hello").getContent());
    assertEquals("world", spec.getView("world").getContent());
    assertEquals("test", spec.getView("test").getContent());
  }

  @Test(expected = SpecParserException.class)
  public void testMissingModulePrefs() throws Exception {
    String xml = "<Module>" + "<Content type=\"html\"/>" + "</Module>";
    StaxTestUtils.parseSpec(xml, SPEC_URL);
  }

  @Test(expected = SpecParserException.class)
  public void testEnforceOneModulePrefs() throws Exception {
    String xml = "<Module>" + "<ModulePrefs title=\"hello\"/>"
        + "<ModulePrefs title=\"world\"/>" + "<Content type=\"html\"/>"
        + "</Module>";
    StaxTestUtils.parseSpec(xml, SPEC_URL);
  }

  @Test(expected = XMLStreamException.class)
  public void testMalformedXml() throws Exception {
    String xml = "<Module><ModulePrefs title=\"foo\"/>";
    StaxTestUtils.parseSpec(xml, SPEC_URL);
  }

  @Test
  public void testSubstitutions() throws Exception {
    Substitutions substituter = new Substitutions();
    String title = "Hello, World!";
    String content = "Goodbye, world :(";
    String xml = "<Module>" + "<ModulePrefs title=\"__UP_title__\"/>"
        + "<Content type=\"html\">__MSG_content__</Content>" + "</Module>";
    substituter.addSubstitution(Type.USER_PREF, "title", title);
    substituter.addSubstitution(Type.MESSAGE, "content", content);

    GadgetSpec spec = StaxTestUtils.parseSpec(xml, SPEC_URL).substitute(
        substituter);
    assertEquals(title, spec.getModulePrefs().getTitle());
    assertEquals(content, spec.getView(GadgetSpec.DEFAULT_VIEW).getContent());
  }
}
