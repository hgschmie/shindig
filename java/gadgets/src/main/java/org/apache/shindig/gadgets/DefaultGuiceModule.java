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

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.shindig.common.util.ArrayListProvider;
import org.apache.shindig.gadgets.http.BasicHttpFetcher;
import org.apache.shindig.gadgets.http.DefaultHttpCache;
import org.apache.shindig.gadgets.http.HttpCache;
import org.apache.shindig.gadgets.http.HttpFetcher;
import org.apache.shindig.gadgets.http.HttpResponse;
import org.apache.shindig.gadgets.oauth.OAuthModule;
import org.apache.shindig.gadgets.parse.ParseModule;
import org.apache.shindig.gadgets.preload.ConcurrentPreloaderService;
import org.apache.shindig.gadgets.preload.HttpPreloader;
import org.apache.shindig.gadgets.preload.Preloader;
import org.apache.shindig.gadgets.preload.PreloaderService;
import org.apache.shindig.gadgets.render.ConfigContributor;
import org.apache.shindig.gadgets.render.CoreUtilContributor;
import org.apache.shindig.gadgets.render.RenderingContentRewriter;
import org.apache.shindig.gadgets.render.ShindigAuthContributor;
import org.apache.shindig.gadgets.rewrite.ContentRewriter;
import org.apache.shindig.gadgets.rewrite.ContentRewriterRegistry;
import org.apache.shindig.gadgets.rewrite.DefaultContentRewriterRegistry;
import org.apache.shindig.gadgets.rewrite.lexer.DefaultContentRewriter;
import org.apache.shindig.gadgets.servlet.CajaContentRewriter;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

/**
 * Creates a module to supply all of the Basic* classes
 */
public class DefaultGuiceModule extends AbstractModule {

  /** {@inheritDoc} */
  @Override
  protected void configure() {

    ExecutorService service = Executors.newCachedThreadPool();
    bind(Executor.class).toInstance(service);
    bind(ExecutorService.class).toInstance(service);

    // Bind the configurable stuff
    bind(HttpCache.class).to(DefaultHttpCache.class);
    bind(HttpFetcher.class).to(BasicHttpFetcher.class);
    bind(PreloaderService.class).to(ConcurrentPreloaderService.class);
    bind(ContentRewriterRegistry.class).to(DefaultContentRewriterRegistry.class);
    bind(GadgetBlacklist.class).to(BasicGadgetBlacklist.class);
    bind(GadgetSpecFactory.class).to(ShindigGadgetSpecFactory.class);
    // bind(GadgetSpec.class).to(DefaultGadgetSpec.class);
    bind(LockedDomainService.class).to(HashLockedDomainService.class);
    bind(MessageBundleFactory.class).to(ShindigMessageBundleFactory.class);
    bind(UrlGenerator.class).to(DefaultUrlGenerator.class);


    this.install(new ParseModule());
    this.install(new OAuthModule());

    bind(new TypeLiteral<List<ContentRewriter>>(){}).toProvider(ContentRewritersProvider.class);
    bind(new TypeLiteral<List<Preloader>>(){}).toProvider(PreloaderProvider.class);

    bind(new TypeLiteral<List<ConfigContributor>>() {})
      .toProvider(new ArrayListProvider<ConfigContributor>()
          .add(CoreUtilContributor.class)
          .add(ShindigAuthContributor.class));

    // We perform static injection on HttpResponse for cache TTLs.
    requestStaticInjection(HttpResponse.class);
  }

  private static class ContentRewritersProvider implements Provider<List<ContentRewriter>> {
    private final List<ContentRewriter> rewriters;

    @Inject
    public ContentRewritersProvider(DefaultContentRewriter optimizingRewriter,
                                    CajaContentRewriter cajaRewriter,
                                    RenderingContentRewriter renderingRewriter) {
      rewriters = Lists.newArrayList();
      rewriters.add(optimizingRewriter);
      rewriters.add(cajaRewriter);
      rewriters.add(renderingRewriter);
    }

    public List<ContentRewriter> get() {
      return rewriters;
    }
  }

  private static class PreloaderProvider implements Provider<List<Preloader>> {
    private final List<Preloader> preloaders;

    @Inject
    public PreloaderProvider(HttpPreloader httpPreloader) {
      preloaders = Lists.<Preloader>newArrayList(httpPreloader);
    }

    public List<Preloader> get() {
      return preloaders;
    }
  }
}
