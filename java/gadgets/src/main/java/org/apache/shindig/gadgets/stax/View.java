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

package org.apache.shindig.gadgets.stax;

import java.util.Collection;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.Content;
import org.apache.shindig.gadgets.stax.model.Content.Type;

/**
 * Normalized Content views.
 */
public class View {

    private final String name;

    private final Type type;

    private final Uri href;

    private final int preferredHeight;

    private final int preferredWidth;

    private final String content;

  /**
   * @param name The name of this view.
   * @param elements List of all views, in order, that make up this view.
   *     An ordered list is required per the spec, since values must
   *     overwrite one another.
   * @param base The base url to resolve href against.
   * @throws SpecParserException
   */
    public View(final String name, final Collection<Content> contents) throws SpecParserException
    {
        this.name = name;

        Type type = null;
        Uri href = null;
        int preferredHeight = -1;
        int preferredWidth = -1;
        StringBuilder text = new StringBuilder();

        for (Content content: contents) {
            switch(content.getType()) {
            case HTML:
            case HTML_INLINE:
                if (href != null) {
                    throw new SpecParserException(content.name().getLocalPart() + " contains HTML code but View '" + name + "' already uses '" + href + "' as content!");
                }
                text.append(content.getText());
                type = Content.Type.HTML;
                break;
            case URL:
                if (text.length() > 0) {
                    throw new SpecParserException(content.name().getLocalPart() + " references '" + content.getHref() + " but View '" + name + "' already has inline HTML code!");
                }
                href = content.getHref();
                type = Content.Type.URL;
                break;
            default:
                throw new SpecParserException(content.name().getLocalPart() + " references unknown content type: " + content.getType());
            }

            preferredHeight = Math.max(content.getPreferredHeight(), preferredHeight);
            preferredWidth = Math.max(content.getPreferredHeight(), preferredWidth);
        }

        this.type = type;
        this.href = href;
        this.preferredHeight = preferredHeight;
        this.preferredWidth = preferredWidth;
        this.content = text.toString();
    }

  public String getName() {
    return name;
  }

  public Type getType() {
    return type;
  }

  public Uri getHref() {
    return href;
  }

  public int getPreferredHeight() {
    return preferredHeight;
  }

  public int getPreferredWidth() {
    return preferredWidth;
  }

  public String getContent() {
    return content;
  }


}
