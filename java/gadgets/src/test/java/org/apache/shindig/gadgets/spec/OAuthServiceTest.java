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

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.common.xml.StaxTestUtils;
import org.junit.Before;
import org.junit.Test;

public class OAuthServiceTest {
  private static final Uri SPEC_URL = Uri.parse("http://example.org/g.xml");

  @Before
  public void setUp() {
  }

  @Test
  public void testParseAuthorizeUrl() throws Exception {
    String xml = "<Authorization url='http://azn.example.com'/>";
    OAuthAuthorization authorization = StaxTestUtils.parseElement(xml, new OAuthAuthorization.Parser(SPEC_URL));
    assertEquals(Uri.parse("http://azn.example.com"), authorization.getUrl());
  }

  @Test(expected=SpecParserException.class)
  public void testParseAuthorizeUrl_nourl() throws Exception {
    String xml = "<Authorization/>";
    StaxTestUtils.parseElement(xml, new OAuthAuthorization.Parser(SPEC_URL));
  }

  @Test
  public void testParseAuthorizeUrl_extraAttr() throws Exception {
    String xml = "<Authorization url='http://www.example.com' foo='bar'/>";
    OAuthAuthorization authorization = StaxTestUtils.parseElement(xml, new OAuthAuthorization.Parser(SPEC_URL));
    assertEquals(Uri.parse("http://www.example.com"), authorization.getUrl());
  }

  @Test(expected=SpecParserException.class)
  public void testParseAuthorizeUrl_notHttp() throws Exception {
    String xml = "<Authorization url='ftp://www.example.com'/>";
    StaxTestUtils.parseElement(xml, new OAuthAuthorization.Parser(SPEC_URL));
  }

  @Test
  public void testParseEndPoint() throws Exception {
    String xml = "<Request url='http://www.example.com'/>";
    OAuthElement request = StaxTestUtils.parseElement(xml, new OAuthRequest.Parser(SPEC_URL));
    assertEquals(Uri.parse("http://www.example.com"), request.getUrl());
    assertEquals(OAuthElement.Location.HEADER, request.getParamLocation());
    assertEquals(OAuthElement.Method.GET, request.getMethod());
  }

  @Test(expected=SpecParserException.class)
  public void testParseEndPoint_badlocation() throws Exception {
      String xml = "<Request url='http://www.example.com' method='GET' param_location='body'/>";
      StaxTestUtils.parseElement(xml, new OAuthRequest.Parser(SPEC_URL));
  }

  @Test
  public void testParseEndPoint_nodefaults() throws Exception {
    String xml = "<Request url='http://www.example.com' method='POST' param_location='post-body'/>";
    OAuthElement request = StaxTestUtils.parseElement(xml, new OAuthRequest.Parser(SPEC_URL));
    assertEquals(Uri.parse("http://www.example.com"), request.getUrl());
    assertEquals(OAuthElement.Location.BODY, request.getParamLocation());
    assertEquals(OAuthElement.Method.POST, request.getMethod());
  }

  @Test(expected=SpecParserException.class)
  public void testParseEndPoint_nourl() throws Exception {
    String xml = "<Request method='GET' param_location='post-body'/>";
    StaxTestUtils.parseElement(xml, new OAuthRequest.Parser(SPEC_URL));
  }

  @Test(expected=SpecParserException.class)
  public void testParseEndPoint_badurl() throws Exception {
    String xml = "<Request url='www.example.com' />";
    StaxTestUtils.parseElement(xml, new OAuthRequest.Parser(SPEC_URL));
  }

  @Test
  public void testParseService() throws Exception {
    String xml = "" +
        "<Service name='thename'>" +
    	"   <Request url='http://request.example.com/foo'/>" +
    	"   <Access url='http://access.example.com/bar'/>" +
    	"   <Authorization url='http://azn.example.com/quux'/>" +
    	"</Service>";
    OAuthService s = StaxTestUtils.parseElement(xml, new OAuthService.Parser(SPEC_URL));
    assertEquals("thename", s.getName());
    assertEquals(OAuthElement.Location.HEADER, s.getAccess().getParamLocation());
    assertEquals(Uri.parse("http://azn.example.com/quux"), s.getAuthorization().getUrl());
  }

  @Test
  public void testParseService_noname() throws Exception {
    String xml = "" +
        "<Service>" +
        "   <Request url='http://request.example.com/foo'/>" +
        "   <Access url='http://access.example.com/bar'/>" +
        "   <Authorization url='http://azn.example.com/quux'/>" +
        "</Service>";
    OAuthService s = StaxTestUtils.parseElement(xml, new OAuthService.Parser(SPEC_URL));
    assertEquals("", s.getName());
    assertEquals(OAuthElement.Location.HEADER, s.getAccess().getParamLocation());
    assertEquals(Uri.parse("http://azn.example.com/quux"), s.getAuthorization().getUrl());
  }

  @Test(expected=SpecParserException.class)
  public void testParseService_nodata() throws Exception {
    String xml = "<Service/>";
    StaxTestUtils.parseElement(xml, new OAuthService.Parser(SPEC_URL));
  }

  @Test(expected=SpecParserException.class)
  public void testParseService_reqonly() throws Exception {
    String xml = "<Service>" +
        "<Request url='http://www.example.com/request'/>" +
        "</Service>";
    StaxTestUtils.parseElement(xml, new OAuthService.Parser(SPEC_URL));
  }
}
