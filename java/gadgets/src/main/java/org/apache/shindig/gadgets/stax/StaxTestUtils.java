package org.apache.shindig.gadgets.stax;
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


import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.spec.GadgetSpec;
import org.apache.shindig.gadgets.spec.ShindigGadgetSpec;
import org.apache.shindig.gadgets.spec.SpecElement;
import org.apache.shindig.gadgets.spec.SpecElement.Parser;

public final class StaxTestUtils {

  private static final XMLInputFactory factory;

  static {
    factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
  }

  private StaxTestUtils() {
  }

  public static final <T extends SpecElement> T parseElement(final String xml,
      final Parser<T> parser) throws GadgetException, XMLStreamException {

    XMLStreamReader reader = null;
    reader = factory.createXMLStreamReader(new StringReader(xml));
    T element = null;

    loop: while (true) {
      final int event = reader.next();
      switch (event) {
      case XMLStreamConstants.END_DOCUMENT:
        reader.close();
        break loop;
      case XMLStreamConstants.START_ELEMENT:
        element = parser.parse(reader);
        break;
      default:
        break;
      }
    }

    return element;
  }

    public static final GadgetSpec parseSpec(final String xml, final Uri base) throws GadgetException, XMLStreamException {
        return parseElement(xml, new ShindigGadgetSpec.Parser<ShindigGadgetSpec>(base, xml));
    }
}
