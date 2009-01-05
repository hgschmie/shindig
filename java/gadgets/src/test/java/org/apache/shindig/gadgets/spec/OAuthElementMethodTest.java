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
 * Tests for OAuthElement.Method parser.
 */
public class OAuthElementMethodTest {

    @Test
    public void testValidValues() {
        assertEquals(OAuthElement.Method.GET, OAuthElement.Method.parse("get"));
        assertEquals(OAuthElement.Method.POST, OAuthElement.Method.parse("post"));
    }

    @Test
    public void testDefaultValue() {
        assertEquals(OAuthElement.Method.GET, OAuthElement.Method.parse(null));
    }

    @Test
    public void testInvalidValues() {
        assertEquals(null, OAuthElement.Method.parse("foobar"));
        assertEquals(null, OAuthElement.Method.parse(""));
        assertEquals(null, OAuthElement.Method.parse("           "));
    }

    @Test
    public void testWhitespace() {
        assertEquals(OAuthElement.Method.GET, OAuthElement.Method.parse("                  get"));
        assertEquals(OAuthElement.Method.POST, OAuthElement.Method.parse("post                   "));
        assertEquals(OAuthElement.Method.GET, OAuthElement.Method.parse("             get          "));
    }

    @Test
    public void testUpperLowercase() {
        assertEquals(OAuthElement.Method.GET, OAuthElement.Method.parse("gEt"));
        assertEquals(OAuthElement.Method.POST, OAuthElement.Method.parse("POST"));
        assertEquals(OAuthElement.Method.GET, OAuthElement.Method.parse("Get"));
    }
}
