package org.apache.shindig.gadgets.stax.model;

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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.shindig.gadgets.spec.SpecParserException;

public abstract class SpecElement {

  private final Logger LOG = Logger.getLogger(getClass().getName());

  private final Map<QName, String> attributes = new HashMap<QName, String>();

  private final Map<String, String> namespaces = new HashMap<String, String>();

  private final List<SpecElement> children = new ArrayList<SpecElement>();

  private final QName name;

  private boolean cdataFlag = false;

  protected SpecElement(final QName name) {
    this.name = name;
  }

  // ======================================================================================================================================

  protected void setAttribute(final QName name, final String value) {
    attributes.put(name, value);
  }

  protected void addChild(final SpecElement child) {
    children.add(child);
  }

  private void addNamespace(final String prefix, final String uri) {
    namespaces.put(prefix, uri);
  }

  private void setCDATAFlag() {
    this.cdataFlag = true;
  }

  private boolean isCDATA() {
    return cdataFlag;
  }

  // ======================================================================================================================================

  public QName name() {
    return name;
  }

  protected String attribute(final String key) {
    return attributes.get(new QName(name.getNamespaceURI(), key));
  }

  public void validate() throws SpecParserException {
    // Nothing to validate.
  }

  protected String getText() {
    return null;
  }

  // ======================================================================================================================================

  public void toXml(final XMLStreamWriter writer) throws XMLStreamException {
    for (Map.Entry<String, String> namespace : namespaces.entrySet()) {
      writer.setPrefix(namespace.getKey(), namespace.getValue());
    }
    writer.writeStartElement(name.getNamespaceURI(), name.getLocalPart());

    writeAttributes(writer);

    for (Map.Entry<QName, String> attribute : attributes.entrySet()) {
      writeAttribute(writer, attribute.getKey(), attribute.getValue());
    }

    writeChildren(writer);

    for (SpecElement child : children) {
      child.toXml(writer);
    }

    writeText(writer);

    writer.writeEndElement();
  }

  protected void writeAttribute(final XMLStreamWriter writer, final QName name,
      final String value) throws XMLStreamException {
    writer.writeAttribute(name.getNamespaceURI(), name.getLocalPart(), value);
  }

  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
  }

  protected void writeChildren(final XMLStreamWriter writer)
      throws XMLStreamException {
  }

  protected void writeText(final XMLStreamWriter writer)
    throws XMLStreamException {

    final String text = getText();

    if (StringUtils.isNotEmpty(text)) {
      if (isCDATA()) {
        writer.writeCData(text);
      } else {
        writer.writeCharacters(text);
      }
    }
  }

  // ======================================================================================================================================

  @Override
  public String toString() {
    final StringWriter sw = new StringWriter();
    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    factory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, Boolean.TRUE);
    XMLStreamWriter writer = null;
    try {
      writer = factory.createXMLStreamWriter(sw);
      toXml(writer);
      writer.flush();
    } catch (XMLStreamException xse) {
      LOG.warning("While writing output stream: " + xse.getMessage());
    } finally {
      closeQuietly(writer);
    }
    sw.flush();
    return sw.toString();
  }

  private static void closeQuietly(final XMLStreamWriter writer) {
    if (writer != null) {
      try {
        writer.close();
      } catch (XMLStreamException xse) {
      }
    }
  }

  public static abstract class Parser<T extends SpecElement> {

    private static final Logger LOG = Logger.getLogger(SpecElement.class
        .getName());

    private final Map<QName, Parser<? extends SpecElement>> children = new HashMap<QName, Parser<? extends SpecElement>>();

    private final QName name;

    protected Parser(final QName name) {
      this.name = name;
    }

    public QName getName() {
      return name;
    }

    protected void register(Parser<? extends SpecElement> parseElement) {
      children.put(parseElement.getName(), parseElement);
    }

    public T parse(final XMLStreamReader reader) throws SpecParserException, XMLStreamException {

      // This assumes, that parse it at the right element.
      T element = newElement();
      addAttributes(reader, element);
      addNamespaces(reader, element);

      while (true) {
        int event = reader.next();

        switch (event) {
        case XMLStreamConstants.ATTRIBUTE:
          addAttributes(reader, element);
          break;
        case XMLStreamConstants.END_ELEMENT:
        case XMLStreamConstants.END_DOCUMENT:
          element.validate();
          return element;

        case XMLStreamConstants.START_ELEMENT:
          final QName elementName = reader.getName();
          Parser<? extends SpecElement> parser = children.get(elementName);
          if (parser == null) {
            LOG.fine("No idea what to do with " + elementName + ", ignoring!");
            parser = new GenericElement.Parser(elementName);
          }

          SpecElement child = parser.parse(reader);
          addChild(reader, element, child);
          break;
        case XMLStreamConstants.CDATA:
          element.setCDATAFlag();
          /* FALLTHROUGH */
        case XMLStreamConstants.CHARACTERS:
          addText(reader, element);
          break;
        default:
          break; // TODO: Do we need to parse more things?
        }
      }
    }

    private void addAttributes(final XMLStreamReader reader, final T element) {
      for (int i = 0; i < reader.getAttributeCount(); i++) {
        setAttribute(element, reader.getAttributeName(i), reader
            .getAttributeValue(i));
      }
    }

    private void addNamespaces(final XMLStreamReader reader, final T element) {
      for (int i = 0; i < reader.getNamespaceCount(); i++) {
        element.addNamespace(reader.getNamespacePrefix(i), reader
            .getNamespaceURI(i));
      }
    }

    protected abstract T newElement();

    protected void setAttribute(final T element, final QName attributeName,
        final String value) {
      element.setAttribute(name, value);
    }

    protected void addText(final XMLStreamReader reader, final T element) throws SpecParserException {
      if (!reader.isWhiteSpace()) {
        throw new IllegalStateException("The element" + element.name()
            + " does not accept any nested text");
      }
    }

    protected void addChild(final XMLStreamReader reader, final T element,
        final SpecElement child) throws SpecParserException {
      throw new IllegalStateException("The element" + element.name()
          + " does not accept any nested elements, saw " + child.name());
    }

  }

  protected static QName buildQName(final QName name, final String localName) {
    return new QName(name.getNamespaceURI(), localName);
  }
}
