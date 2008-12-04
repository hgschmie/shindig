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
package org.apache.shindig.server.endtoend;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import org.apache.shindig.auth.AnonymousAuthenticationHandler;
import org.apache.shindig.auth.AuthenticationHandler;
import org.apache.shindig.common.cache.LruCacheModule;
import org.apache.shindig.common.guice.DefaultCommonModule;
import org.apache.shindig.common.servlet.ParameterFetcher;
import org.apache.shindig.gadgets.DefaultGuiceModule;
import org.apache.shindig.social.core.config.DefaultOpenSocialObjectsModule;
import org.apache.shindig.social.core.oauth.AuthenticationHandlerProvider;
import org.apache.shindig.social.core.util.BeanJsonConverter;
import org.apache.shindig.social.core.util.BeanXStreamAtomConverter;
import org.apache.shindig.social.core.util.BeanXStreamConverter;
import org.apache.shindig.social.core.util.xstream.XStream081Configuration;
import org.apache.shindig.social.core.util.xstream.XStreamConfiguration;
import org.apache.shindig.social.opensocial.oauth.OAuthLookupService;
import org.apache.shindig.social.opensocial.service.BeanConverter;
import org.apache.shindig.social.opensocial.service.DataServiceServletFetcher;
import org.apache.shindig.social.opensocial.service.HandlerDispatcher;
import org.apache.shindig.social.opensocial.service.StandardHandlerDispatcher;
import org.apache.shindig.social.opensocial.spi.ActivityService;
import org.apache.shindig.social.opensocial.spi.AppDataService;
import org.apache.shindig.social.opensocial.spi.PersonService;
import org.apache.shindig.social.sample.oauth.SampleContainerOAuthLookupService;
import org.apache.shindig.social.sample.spi.JsonDbOpensocialService;

import java.util.List;

/**
 * Guice module for the end-to-end tests.
 */
public class EndToEndModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(HandlerDispatcher.class).to(StandardHandlerDispatcher.class);

    bind(ParameterFetcher.class).annotatedWith(Names.named("DataServiceServlet"))
        .to(DataServiceServletFetcher.class);

    bind(String.class).annotatedWith(Names.named("shindig.canonical.json.db"))
        .toInstance("sampledata/canonicaldb.json");

    bind(Boolean.class)
        .annotatedWith(Names.named(AnonymousAuthenticationHandler.ALLOW_UNAUTHENTICATED))
        .toInstance(Boolean.FALSE);

    bind(BeanConverter.class).annotatedWith(Names.named("shindig.bean.converter.xml"))
        .to(BeanXStreamConverter.class);
    bind(BeanConverter.class).annotatedWith(Names.named("shindig.bean.converter.json"))
        .to(BeanJsonConverter.class);
    bind(BeanConverter.class).annotatedWith(Names.named("shindig.bean.converter.atom"))
        .to(BeanXStreamAtomConverter.class);

    bind(new TypeLiteral<List<AuthenticationHandler>>(){}).toProvider(
        AuthenticationHandlerProvider.class);

    bind(XStreamConfiguration.class).to(XStream081Configuration.class);
    bind(OAuthLookupService.class).to(SampleContainerOAuthLookupService.class);
    bind(ActivityService.class).to(JsonDbOpensocialService.class);
    bind(AppDataService.class).to(JsonDbOpensocialService.class);
    bind(PersonService.class).to(JsonDbOpensocialService.class);

    install(new DefaultOpenSocialObjectsModule());
    install(new DefaultGuiceModule());
    install(new DefaultCommonModule());
    install(new LruCacheModule());
  }
}
