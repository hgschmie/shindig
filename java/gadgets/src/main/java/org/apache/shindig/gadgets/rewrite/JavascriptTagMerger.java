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
package org.apache.shindig.gadgets.rewrite;

import com.google.caja.lexer.HtmlTokenType;
import com.google.caja.lexer.Token;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Transform a contiguous block of script tags that refer to external scripts
 * and rewrite them to a single script tag that uses a concatenating proxy
 * to concatenate these scripts in order and also potentially perform other
 * optimizations on the generated unified script such as minification
 */
public class JavascriptTagMerger implements HtmlTagTransformer {

  @SuppressWarnings("unchecked")
  // Scripts has to hold both URIs and tokens.
  private final List scripts = new ArrayList();

  private final String concatBase;

  private final URI relativeUrlBase;

  private boolean isTagOpen = true;

  /**
   * @param concatBase Base url of the Concat servlet. Expected to be of the
   *                   form www.host.com/concat?
   * @param relativeUrlBase to resolve relative urls
   */
  public JavascriptTagMerger(String concatBase, URI relativeUrlBase) {
    this.concatBase = concatBase;
    this.relativeUrlBase = relativeUrlBase;
  }

  @SuppressWarnings("unchecked")
  public void accept(Token<HtmlTokenType> token,
      Token<HtmlTokenType> lastToken) {
    try {
      if (isTagOpen) {
        if (lastToken != null &&
            lastToken.type == HtmlTokenType.ATTRNAME &&
            lastToken.text.equalsIgnoreCase("src")) {
          scripts.add(new URI(stripQuotes(token.text)));
        } else if (token.type == HtmlTokenType.UNESCAPED) {
          scripts.add(token);
        }
      }
    } catch (URISyntaxException use) {
      throw new RuntimeException(use);
    }
  }

  public boolean acceptNextTag(Token<HtmlTokenType> tagStart) {
    if (tagStart.text.equalsIgnoreCase("<script")) {
      isTagOpen = true;
      return true;
    } else if (tagStart.text.equalsIgnoreCase("</script")) {
      isTagOpen = false;
      return true;
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  public String close() {
    List<URI> concat = new ArrayList<URI>();
    StringBuilder builder = new StringBuilder(100);
    for (Object o : scripts) {
      if (o instanceof URI) {
        concat.add((URI) o);
      } else {
        flushConcat(concat, builder);
        builder.append("<script type=\"text/javascript\">")
            .append(((Token<HtmlTokenType>) o).text).append("</script>");
      }
    }
    flushConcat(concat, builder);
    scripts.clear();
    isTagOpen = true;
    return builder.toString();
  }

  private void flushConcat(List<URI> concat, StringBuilder builder) {
    if (concat.isEmpty()) {
      return;
    }
    builder.append("<script src=\"").append(concatBase);
    for (int i = 0; i < concat.size(); i++) {
      URI srcUrl = concat.get(i);
      if (!srcUrl.isAbsolute()) {
        srcUrl = relativeUrlBase.resolve(srcUrl);
      }
      builder.append(i + 1).append("=")
          .append(URLEncoder.encode(srcUrl.toString()));
      if (i < concat.size() - 1) {
        builder.append("&");
      }
    }
    builder.append("\" type=\"text/javascript\"></script>");
    concat.clear();
  }

  private String stripQuotes(String s) {
    return s.replaceAll("\"", "").replaceAll("'","");
  }
}
