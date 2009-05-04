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
package org.apache.shindig.gadgets.http;

import org.apache.shindig.common.util.Utf8UrlCoder;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.oauth.OAuthRequest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Implements HttpFetcher by delegating fetches to either plain or authenticated Http fetchers.
 *
 * TODO: Make this actually implement HttpFetcher to simplify the bindings. Currently we just
 * pretend that is implements HttpFetcher.
 *
 * TODO: Get rid of RemoteContentFetcherFactory by just injecting HttpFetcher.
 * TODO: Get rid of OAUthFetcherFactory by injecting OAuthFetcher (requires significant work.
 */
@Singleton
public class BasicContentFetcherFactory implements ContentFetcherFactory {

  private final HttpFetcher httpFetcher;
  private final HttpCache httpCache;
  private final Provider<OAuthRequest> oauthRequestProvider;

  @Inject
  public BasicContentFetcherFactory(HttpFetcher httpFetcher,
                               HttpCache httpCache,
                               Provider<OAuthRequest> oauthRequestProvider)

  {
    this.httpFetcher = httpFetcher;
    this.httpCache = httpCache;
    this.oauthRequestProvider = oauthRequestProvider;
  }

  /* (non-Javadoc)
 * @see org.apache.shindig.gadgets.http.ContentFetcherFactory#fetch(org.apache.shindig.gadgets.http.HttpRequest)
 */
public HttpResponse fetch(final HttpRequest request) throws GadgetException {
    normalizeProtocol(request);

    final HttpCacheKey cacheKey = new HttpCacheKey(request);
    HttpResponse response = null;

    if (!request.getIgnoreCache())
    {
        response = httpCache.getResponse(cacheKey, request);
        if (response != null)
        {
            return response;
        }
    }

    HttpResponse fetchedResponse = null;
    switch (request.getAuthType()) {
      case NONE:
        fetchedResponse = httpFetcher.fetch(request);
        break;
      case SIGNED:
      case OAUTH:
        fetchedResponse = oauthRequestProvider.get().fetch(request);
        break;
      default:
        return HttpResponse.error();
    }

    if (!request.getIgnoreCache() ) {
      httpCache.addResponse(cacheKey, request, fetchedResponse);
    }
    return fetchedResponse;
  }

  public void normalizeProtocol(HttpRequest request) throws GadgetException {
    // Normalize the protocol part of the URI
    if (request.getUri().getScheme()== null) {
      throw new GadgetException(GadgetException.Code.INVALID_PARAMETER,
          "Url " + request.getUri().toString() + " does not include scheme");
    } else if (!"http".equals(request.getUri().getScheme()) &&
        !"https".equals(request.getUri().getScheme())) {
      throw new GadgetException(GadgetException.Code.INVALID_PARAMETER,
          "Invalid request url scheme in url: " + Utf8UrlCoder.encode(request.getUri().toString()) +
            "; only \"http\" and \"https\" supported.");
    }
  }
}
