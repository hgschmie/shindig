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
package org.apache.shindig.gadgets.render;

import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.gadgets.Gadget;
import org.apache.shindig.gadgets.GadgetContext;
import org.json.JSONException;
import org.json.JSONObject;

public class ShindigAuthContributor implements ConfigContributor {

  /**
   * Add authentication token config.
   */
  public void contribute(final JSONObject config, final Gadget gadget) {
    final GadgetContext context = gadget.getContext();
    final SecurityToken authToken = context.getToken();

    if (authToken != null) {
      final JSONObject authConfig = new JSONObject();
      final String updatedToken = authToken.getUpdatedToken();
      try {
        if (updatedToken != null) {
          authConfig.put("authToken", updatedToken);
        }
        final String trustedJson = authToken.getTrustedJson();
        if (trustedJson != null) {
          authConfig.put("trustedJson", trustedJson);
        }
        config.put("shindig.auth", authConfig);
      } catch (JSONException e) {
        // Shouldn't be possible.
        throw new RuntimeException(e);
      }
    }
  }
}
