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
package org.apache.shindig.common.guice;

import org.apache.shindig.auth.DefaultSecurityTokenDecoder;
import org.apache.shindig.auth.SecurityTokenDecoder;
import org.apache.shindig.common.ContainerConfig;
import org.apache.shindig.common.JsonContainerConfig;
import org.apache.shindig.common.cache.CacheProvider;
import org.apache.shindig.common.cache.LruCacheProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * Default configuration for everything that should be wired
 * up in the common module.
 */
public class DefaultCommonModule extends AbstractModule {

  /** {@inheritDoc} */
  @Override
  protected void configure() {
	  bind(SecurityTokenDecoder.class).to(DefaultSecurityTokenDecoder.class).in(Singleton.class);
	  bind(CacheProvider.class).to(LruCacheProvider.class);
	  bind(ContainerConfig.class).to(JsonContainerConfig.class);
  }
}
