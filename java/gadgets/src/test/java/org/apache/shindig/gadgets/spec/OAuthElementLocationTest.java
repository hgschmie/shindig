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

import org.junit.Test;

/**
 * Tests for OAuthElement.Location parser.
 */
public class OAuthElementLocationTest {

    @Test
    public void testValidValues() {
        assertEquals(OAuthElement.Location.HEADER, OAuthElement.Location.parse("header"));
        assertEquals(OAuthElement.Location.URL, OAuthElement.Location.parse("url"));
        assertEquals(OAuthElement.Location.BODY, OAuthElement.Location.parse("body"));
        assertEquals(OAuthElement.Location.HEADER, OAuthElement.Location.parse("auth-header"));
        assertEquals(OAuthElement.Location.URL, OAuthElement.Location.parse("uri-query"));
        assertEquals(OAuthElement.Location.BODY, OAuthElement.Location.parse("post-body"));
    }

    @Test
    public void testDefaultValue() {
        assertEquals(OAuthElement.Location.HEADER, OAuthElement.Location.parse(null));
    }

    @Test
    public void testInvalidValues() {
        assertEquals(null, OAuthElement.Location.parse("foobar"));
        assertEquals(null, OAuthElement.Location.parse(""));
        assertEquals(null, OAuthElement.Location.parse("           "));
    }

    @Test
    public void testWhitespace() {
        assertEquals(OAuthElement.Location.HEADER, OAuthElement.Location.parse("                  header"));
        assertEquals(OAuthElement.Location.URL, OAuthElement.Location.parse("url                   "));
        assertEquals(OAuthElement.Location.BODY, OAuthElement.Location.parse("             body          "));
    }

    @Test
    public void testUpperLowercase() {
        assertEquals(OAuthElement.Location.HEADER, OAuthElement.Location.parse("hEaDeR"));
        assertEquals(OAuthElement.Location.URL, OAuthElement.Location.parse("URL"));
        assertEquals(OAuthElement.Location.BODY, OAuthElement.Location.parse("Body"));
    }
}



