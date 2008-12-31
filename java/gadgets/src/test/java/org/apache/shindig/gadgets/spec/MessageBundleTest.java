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

import static org.junit.Assert.assertEquals;

import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.stax.MessageBundle;
import org.apache.shindig.gadgets.stax.StaxTestUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

public class MessageBundleTest {
  private static final Uri BUNDLE_URL = Uri.parse("http://example.org/m.xml");
  private static final String LOCALE
      = "<Locale lang='en' country='US' messages='" + BUNDLE_URL + "'/>";
  private static final String PARENT_LOCALE
      = "<Locale lang='en' country='ALL' language_direction='rtl'>" +
        " <msg name='one'>VALUE</msg>" +
        " <msg name='foo'>adfdfdf</msg>" +
        "</Locale>";
  private static final Map<String, String> MESSAGES = Maps.newHashMap();
  private static final String XML;
  static {
    MESSAGES.put("hello", "world");
    MESSAGES.put("foo", "bar");
    StringBuilder buf = new StringBuilder();
    buf.append("<messagebundle>");
    for (Map.Entry<String, String> entry : MESSAGES.entrySet()) {
      buf.append("<msg name='").append(entry.getKey()).append("'>")
         .append(entry.getValue())
         .append("</msg>");
    }
    buf.append("</messagebundle>");
    XML = buf.toString();
  }

  private LocaleSpec locale;

  @Before
  public void setUp() throws Exception {
    locale = StaxTestUtils.parseElement(LOCALE, new LocaleSpec.Parser(Uri.parse("http://example.org/gadget")));
  }

  @Test
  public void normalMessageBundleParsesOk() throws Exception {
    MessageBundleSpec messageBundleSpec = StaxTestUtils.parseElement(XML, new MessageBundleSpec.Parser(null));
    MessageBundle bundle = new MessageBundle(locale, messageBundleSpec);
    assertEquals(MESSAGES, bundle.getMessages());
  }

  @Test(expected = SpecParserException.class)
  public void missingNameThrows() throws Exception {
    String xml = "<messagebundle><msg>foo</msg></messagebundle>";
    StaxTestUtils.parseElement(xml, new MessageBundleSpec.Parser(null));
  }

  @Test(expected = XMLStreamException.class)
  public void malformedXmlThrows() throws Exception {
    String xml = "</messagebundle>";
    StaxTestUtils.parseElement(xml, new MessageBundleSpec.Parser(null));
  }

  @Test
  public void extractFromElement() throws Exception {
    MessageBundleSpec element = StaxTestUtils.parseElement(XML, new MessageBundleSpec.Parser(null));
    MessageBundle bundle = new MessageBundle(element);
    assertEquals(MESSAGES, bundle.getMessages());
  }

  @Test
  public void extractFromElementWithLanguageDir() throws Exception {
    LocaleSpec element = StaxTestUtils.parseElement(PARENT_LOCALE, new LocaleSpec.Parser(null));
    MessageBundle bundle = new MessageBundle(element);
    assertEquals(MessageBundle.Direction.RTL, bundle.getLanguageDirection());
  }

  @Test(expected = SpecParserException.class)
  public void extractFromElementsWithNoName() throws Exception {
    String xml = "<messagebundle><msg>foo</msg></messagebundle>";
    new MessageBundle(StaxTestUtils.parseElement(xml, new MessageBundleSpec.Parser(null)));
  }

  @Test
  public void merge() throws Exception {
    MessageBundle parent = new MessageBundle(StaxTestUtils.parseElement(PARENT_LOCALE, new LocaleSpec.Parser(null)));
    MessageBundle child = new MessageBundle(StaxTestUtils.parseElement(XML, new MessageBundleSpec.Parser(null)));
    MessageBundle bundle = new MessageBundle(parent, child);
    assertEquals(MessageBundle.Direction.LTR, bundle.getLanguageDirection());
    assertEquals("VALUE", bundle.getMessages().get("one"));
    assertEquals("bar", bundle.getMessages().get("foo"));
  }

  @Test
  public void toStringIsSane() throws Exception {
    MessageBundleSpec b0 = StaxTestUtils.parseElement(XML, new MessageBundleSpec.Parser(null));
    MessageBundleSpec b1 = StaxTestUtils.parseElement(b0.toString(), new MessageBundleSpec.Parser(null));
    assertEquals(b0.getLocaleMsgs(), b1.getLocaleMsgs());
  }

  private static void assertJsonEquals(JSONObject left, JSONObject right) throws JSONException {
    assertEquals(left.length(), right.length());
    for (String key : JSONObject.getNames(left)) {
      assertEquals(left.get(key), right.get(key));
    }
  }

  @Test
  public void toJSONStringMatchesValues() throws Exception {
    MessageBundle simple = new MessageBundle(StaxTestUtils.parseElement(PARENT_LOCALE, new LocaleSpec.Parser(null)));

    JSONObject fromString = new JSONObject(simple.toJSONString());
    JSONObject fromMap = new JSONObject(simple.getMessages());
    assertJsonEquals(fromString, fromMap);
  }

  @Test
  public void toJSONStringMatchesValuesLocaleCtor() throws Exception {
    MessageBundle bundle = new MessageBundle(StaxTestUtils.parseElement(XML, new MessageBundleSpec.Parser(null)));

    JSONObject fromString = new JSONObject(bundle.toJSONString());
    JSONObject fromMap = new JSONObject(bundle.getMessages());
    assertJsonEquals(fromString, fromMap);
  }

  @Test
  public void toJSONStringMatchesValuesWithChild() throws Exception {
    MessageBundle parent = new MessageBundle(StaxTestUtils.parseElement(PARENT_LOCALE, new LocaleSpec.Parser(null)));
    MessageBundle child = new MessageBundle(StaxTestUtils.parseElement(XML, new MessageBundleSpec.Parser(null)));
    MessageBundle bundle = new MessageBundle(parent, child);

    JSONObject fromString = new JSONObject(bundle.toJSONString());
    JSONObject fromMap = new JSONObject(bundle.getMessages());
    assertJsonEquals(fromString, fromMap);
  }
}
