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
package org.apache.shindig.gadgets;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.oauth.GadgetTokenStoreTest;
import org.apache.shindig.gadgets.spec.GadgetSpec;

import java.net.URI;

import javax.xml.stream.XMLStreamException;

/**
 * Fakes out a gadget spec factory
 */
public class FakeGadgetSpecFactory implements GadgetSpecFactory {
  public static final String SERVICE_NAME = "testservice";
  public static final String SERVICE_NAME_NO_KEY = "nokey";

  public GadgetSpec getGadgetSpec(GadgetContext context) throws GadgetException, XMLStreamException {
    URI uri = context.getUrl();
    String gadget = uri.toString();
    String baseSpec = GadgetTokenStoreTest.GADGET_SPEC;

    if (gadget.contains("nokey")) {
      // For testing key lookup failures
      String nokeySpec = baseSpec.replace(SERVICE_NAME, SERVICE_NAME_NO_KEY);
      return StaxTestUtils.parseSpec(nokeySpec, Uri.fromJavaUri(uri));
    } else if (gadget.contains("header")) {
      // For testing oauth data in header
      String headerSpec = baseSpec.replace("uri-query", "auth-header");
      return StaxTestUtils.parseSpec(headerSpec, Uri.fromJavaUri(uri));
    } else if (gadget.contains("body")) {
      // For testing oauth data in body
      String bodySpec = baseSpec.replace("uri-query", "post-body");
      bodySpec = bodySpec.replace("'GET'", "'POST'");
      return StaxTestUtils.parseSpec(bodySpec, Uri.fromJavaUri(uri));
    } else if (gadget.contains("badoauthurl")) {
      String spec = baseSpec.replace("/request?param=foo", "/echo?mary_had_a_little_lamb");
      spec = spec.replace("/access", "/echo?add_oauth_token=with_fleece_as_white_as_snow");
      spec = spec.replace("uri-query", "auth-header");
      return StaxTestUtils.parseSpec(spec, Uri.fromJavaUri(uri));
    } else if (gadget.contains("approvalparams")) {
      String spec = baseSpec.replace("/authorize", "/authorize?oauth_callback=foo");
      return StaxTestUtils.parseSpec(spec, Uri.fromJavaUri(uri));
    } else {
      return StaxTestUtils.parseSpec(baseSpec, Uri.fromJavaUri(uri));
    }
  }

  public GadgetSpec getGadgetSpec(URI gadgetUri, boolean ignoreCache) throws GadgetException, XMLStreamException {
    Uri uri = Uri.fromJavaUri(gadgetUri);
    String gadget = uri.toString();
    String baseSpec = GadgetTokenStoreTest.GADGET_SPEC;
    if (gadget.contains("nokey")) {
      // For testing key lookup failures
      String nokeySpec = baseSpec.replace(SERVICE_NAME, SERVICE_NAME_NO_KEY);
      return StaxTestUtils.parseSpec(nokeySpec, uri);

    } else if (gadget.contains("header")) {
      // For testing oauth data in header
      String headerSpec = baseSpec.replace("uri-query", "auth-header");
      return StaxTestUtils.parseSpec(headerSpec, uri);

    } else if (gadget.contains("body")) {
      // For testing oauth data in body
      String bodySpec = baseSpec.replace("uri-query", "post-body");
      bodySpec = bodySpec.replace("'GET'", "'POST'");
      return StaxTestUtils.parseSpec(bodySpec, uri);
    } else {
      return StaxTestUtils.parseSpec(baseSpec, uri);
    }
  }
}
