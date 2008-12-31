/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.shindig.gadgets.spec;

import static org.junit.Assert.assertEquals;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.stax.StaxTestUtils;
import org.apache.shindig.gadgets.stax.model.OAuthElement;
import org.apache.shindig.gadgets.stax.model.OAuthSpec;
import org.junit.Test;

/**
 * Tests for OAuthSpec
 */
public class OAuthSpecTest {
  private static final Uri SPEC_URL = Uri.parse("http://example.org/g.xml");

  @Test
  public void testOAuthSpec() throws Exception {
    String xml = "<OAuth><Service>" +
      "<Request url='http://www.example.com/request'/>" +
      "<Access url='http://www.example.com/access'/>" +
      "<Authorization url='http://www.example.com/authorize'/>" +
      "</Service></OAuth>";
    OAuthSpec oauth = StaxTestUtils.parseElement(xml, new OAuthSpec.Parser(SPEC_URL));
    assertEquals(1, oauth.getServices().size());
  }

  @Test
  public void testOAuthSpec_noservice() throws Exception {
    String xml = "<OAuth/>";
    OAuthSpec oauth = StaxTestUtils.parseElement(xml, new OAuthSpec.Parser(SPEC_URL));
    assertEquals(0, oauth.getServices().size());
  }

  @Test
  public void testOAuthSpec_threeservice() throws Exception {
    String xml = "<OAuth>" +
        "<Service name='one'>" +
        " <Request url='http://req.example.com' param_location='uri-query' method='POST'/>" +
        " <Access url='http://acc.example.com' param_location='uri-query' method='POST'/>" +
        " <Authorization url='http://azn.example.com'/>" +
        "</Service>" +
        "<Service name='two'>" +
        " <Request url='http://two.example.com/req'/>" +
        " <Access url='http://two.example.com'/>" +
        " <Authorization url='http://two.example.com/authorize'/>" +
        "</Service>" +
    	"<Service name='three'>" +
        " <Request url='http://three.example.com' param_location='uri-query' method='POST'/>" +
        " <Access url='http://three.example.com/acc' param_location='uri-query' method='POST'/>" +
        " <Authorization url='http://three.example.com/authorize'/>" +
        "</Service>" +
    	"</OAuth>";
    OAuthSpec oauth = StaxTestUtils.parseElement(xml, new OAuthSpec.Parser(SPEC_URL));
    assertEquals(Uri.parse("http://req.example.com"),
        oauth.getServices().get("one").getRequest().getUrl());
    assertEquals(OAuthElement.Location.URL,
        oauth.getServices().get("one").getRequest().getParamLocation());
    assertEquals(Uri.parse("http://two.example.com"),
        oauth.getServices().get("two").getAccess().getUrl());
    assertEquals(OAuthElement.Method.POST,
        oauth.getServices().get("three").getRequest().getMethod());
    assertEquals(Uri.parse("http://three.example.com/acc"),
        oauth.getServices().get("three").getAccess().getUrl());
    assertEquals(Uri.parse("http://three.example.com/authorize"),
        oauth.getServices().get("three").getAuthorization().getUrl());
  }
}
