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
import org.apache.shindig.common.util.HashUtil;
import org.apache.shindig.gadgets.http.HttpFetcher;
import org.apache.shindig.gadgets.spec.GadgetSpec;
import org.apache.shindig.gadgets.spec.ShindigGadgetSpec;
import org.apache.shindig.gadgets.spec.SpecParserException;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Create GadgetSpec objects using a StAX parser, thus allowing storage and recreation of namespaces on the Gadget spec
 * object
 */
public class ShindigGadgetSpecFactory extends AbstractGadgetSpecFactory implements GadgetSpecFactory {

  private final StaxSupport staxSupport;

  @Inject
  public ShindigGadgetSpecFactory(final HttpFetcher fetcher, final CacheProvider cacheProvider,
                                  final StaxSupport staxSupport,
                                  final @Named("shindig.cache.xml.refreshInterval") long refresh) {
    super(fetcher, cacheProvider, refresh);
    this.staxSupport = staxSupport;
  }

  @Override
  protected GadgetSpec buildGadgetSpec(final Uri uri, final String xml) throws GadgetException {

    ShindigGadgetSpec gadgetSpec = null;

    try {
      final XMLStreamReader reader = staxSupport.getReader(xml);
      final String checksum = HashUtil.checksum(xml.getBytes());
      final ShindigGadgetSpec.Parser<ShindigGadgetSpec> parser = new ShindigGadgetSpec.Parser<ShindigGadgetSpec>(uri,
          checksum);

      loop: while (true) {
        final int event = reader.next();
        switch (event) {
          case XMLStreamConstants.END_DOCUMENT:
            reader.close();
            break loop;
          case XMLStreamConstants.START_ELEMENT:
            // This is the root element. Open a gadget spec parser and let it
            // loose...
            gadgetSpec = parser.parse(reader); // TODO, that parser must be
            // injectable.
            // This might not be good enough; should we take message bundle
            // changes
            // into account?
            break;
          default:
            break;
        }
      }
    } catch (XMLStreamException xse) {
      throw new SpecParserException("Could not parse GadgetSpec: ", xse);
    }
    return gadgetSpec;
  }
}
