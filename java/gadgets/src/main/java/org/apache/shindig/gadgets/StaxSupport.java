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

package org.apache.shindig.gadgets;

import java.io.OutputStream;
import java.io.StringReader;
import java.io.Writer;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.gadgets.spec.SpecElement;

import com.ctc.wstx.api.WstxOutputProperties;


public class StaxSupport {

  private final XMLInputFactory inputFactory;
  private final XMLOutputFactory outputFactory;

    public StaxSupport() {
        this.inputFactory = XMLInputFactory.newInstance();
        this.inputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        this.inputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.FALSE);

        this.outputFactory = XMLOutputFactory.newInstance();
        this.outputFactory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, Boolean.TRUE);
        this.outputFactory.setProperty(WstxOutputProperties.P_OUTPUT_ESCAPE_CR, Boolean.FALSE);
    }

    public XMLStreamReader getReader(final String xml) throws XMLStreamException {
        return inputFactory.createXMLStreamReader(new StringReader(xml));
    }

    public XMLStreamWriter getWriter(final Writer writer, final SpecElement rootElement) throws XMLStreamException {
        final XMLStreamWriter xmlWriter = outputFactory.createXMLStreamWriter(writer);
        rootElement.prepareWriter(xmlWriter);
        return xmlWriter;
    }

    public XMLStreamWriter getWriter(final OutputStream stream, final SpecElement rootElement) throws XMLStreamException {
      final XMLStreamWriter xmlWriter = outputFactory.createXMLStreamWriter(stream);
      rootElement.prepareWriter(xmlWriter);
      return xmlWriter;
  }

    public static final void closeQuietly(final XMLStreamWriter writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (XMLStreamException xse) {
                // Not much we can do here. Just ignore it.
            }
        }
    }
}


