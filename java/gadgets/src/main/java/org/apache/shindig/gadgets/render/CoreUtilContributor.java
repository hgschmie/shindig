package org.apache.shindig.gadgets.render;
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


import org.apache.shindig.gadgets.Gadget;
import org.apache.shindig.gadgets.GadgetFeatureRegistry;
import org.apache.shindig.gadgets.stax.StaxUtils;
import org.apache.shindig.gadgets.stax.model.Feature;
import org.apache.shindig.gadgets.stax.model.ModulePrefs;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.inject.Inject;

public class CoreUtilContributor implements ConfigContributor {

    private final GadgetFeatureRegistry registry;

    @Inject
    public CoreUtilContributor(final GadgetFeatureRegistry registry) {
        this.registry = registry;
    }

    /**
     * Add gadgets.util support. This is calculated dynamically based on request inputs.
     */
    public void contribute(final JSONObject config, final Gadget gadget) {

        final ModulePrefs prefs = gadget.getSpec().getModulePrefs();
        final JSONObject featureMap = new JSONObject();

        try {
            for (final Feature feature : prefs.getFeatures().values()) {
                if (registry.hasFeature(feature.getFeature())) {
                    featureMap.put(feature.getFeature(), StaxUtils.params(feature));
                }
            }
            config.put("core.util", featureMap);
        } catch (JSONException e) {
            // Shouldn't be possible.
            throw new RuntimeException(e);
        }
    }
}
