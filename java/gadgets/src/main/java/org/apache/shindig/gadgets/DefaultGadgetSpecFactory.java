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

import org.apache.shindig.common.cache.CacheProvider;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.http.HttpFetcher;
import org.apache.shindig.gadgets.spec.DefaultGadgetSpec;
import org.apache.shindig.gadgets.spec.GadgetSpec;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Default implementation of a gadget spec factory.
 */
@Singleton
public class DefaultGadgetSpecFactory extends AbstractGadgetSpecFactory implements GadgetSpecFactory {

    @Inject
    public DefaultGadgetSpecFactory(final HttpFetcher fetcher,
            final CacheProvider cacheProvider,
            final@Named("shindig.cache.xml.refreshInterval") long refresh) {
        super (fetcher, cacheProvider, refresh);
    }

    @Override
    protected GadgetSpec buildGadgetSpec(final Uri uri, final String xml) throws GadgetException {
        return new DefaultGadgetSpec(uri, xml);
    }
}
