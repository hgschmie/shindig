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
import java.util.Collections;
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

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.Pair;
import org.apache.shindig.gadgets.variables.Substitutions;

public abstract class SpecElement {

  private static final XMLOutputFactory factory;

  static {
    factory = XMLOutputFactory.newInstance();
    factory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, Boolean.TRUE);
  }

  private final Logger LOG = Logger.getLogger(getClass().getName());

  private final QName qName;

  private final Uri base;

  private final Map<String, QName> attrNames;

  private final Map<String, String> attrs = new HashMap<String, String>();

  private final Map<String, Pair<QName, String>> otherAttrs;

  private final Map<String, String> namespaces;

  private final Map<QName, String> nsAttrs;

  private final List<SpecElement> children = new ArrayList<SpecElement>();

  private boolean cdataFlag = false;

  protected SpecElement(final QName qName, final Map<String, QName> attrNames,
      final Uri base) {
    this.qName = qName;
    this.attrNames = attrNames;
    this.base = base;

    nsAttrs = new HashMap<QName, String>();
    namespaces = new HashMap<String, String>();

    otherAttrs = new HashMap<String, Pair<QName, String>>();
  }

  protected SpecElement(final SpecElement specElement) {
    this.qName = specElement.name();
    this.attrNames = specElement.attrNames();
    this.base = specElement.getBase();

    this.nsAttrs = specElement.nsAttrs();
    this.namespaces = specElement.namespaces();

    this.otherAttrs = specElement.otherAttrs();

    this.cdataFlag = specElement.isCDATA();

    // Copy known keys over.
    for (String key : attrNames.keySet()) {
      setAttr(key, specElement.attr(key));
    }
  }

  protected Uri getBase() {
    return base;
  }

  // TODO HPS - implement everywhere and use it.
  protected SpecElement substitute(final Substitutions subs) {
    return this;
  }

  // ======================================================================================================================================

  protected void setAttr(final QName key, final String value) {
    if (StringUtils.equalsIgnoreCase(key.getNamespaceURI(), qName
        .getNamespaceURI())) {
      final String keyName = key.getLocalPart().toLowerCase();
      if (attrNames.containsKey(keyName)) {
        setAttr(keyName, value);
      } else {
        otherAttrs.put(keyName, new Pair<QName, String>(key, value));
      }
    } else {
      nsAttrs.put(key, value);
    }
  }

  protected void setAttr(final String key, final String value) {
    attrs.put(key, value);
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
    return qName;
  }

  public String getOtherAttr(final String key) {
    return otherAttrs.get(key.toLowerCase()).getValue();
  }

  public Map<String, String> getOtherAttrs() {
    Map<String, String> localAttributes = new HashMap<String, String>();

    for (Pair<QName, String> pairs : otherAttrs.values()) {
      localAttributes.put(pairs.getKey().getLocalPart(), pairs.getValue());
    }

    return Collections.unmodifiableMap(localAttributes);
  }

  // ======================================================================================================================================

  protected String attr(final String key) {
    return attr(key.toLowerCase());
  }

  protected String attrDefault(final String key) {
    return StringUtils.defaultString(attr(key));
  }

  protected String attrDefault(final String key, final String defaultValue) {
    return StringUtils.defaultString(attr(key), defaultValue);
  }

  protected boolean attrBool(final String key) {
    return BooleanUtils.toBoolean(attr(key));
  }

  protected boolean attrBool(final String key, final boolean defaultValue) {
    final String value = attr(key);
    return (value == null) ? defaultValue : BooleanUtils.toBoolean(value);
  }

  protected double attrDouble(final String key) {
    return NumberUtils.toDouble(attr(key));
  }

  protected int attrInt(final String key) {
    return NumberUtils.toInt(attr(key));
  }

  protected int attrInt(final String key, final int defaultValue) {
    return NumberUtils.toInt(attr(key), defaultValue);
  }

  protected Uri attrUri(final String key) {
    return Uri.toUri(attr(key), Uri.EMPTY_URI);
  }

  protected Uri attrUriNull(final String key) {
    return Uri.toUri(attr(key), null);
  }

  // ======================================================================================================================================

  private Map<String, QName> attrNames() {
    return Collections.unmodifiableMap(attrNames);
  }

  private Map<QName, String> nsAttrs() {
    return Collections.unmodifiableMap(nsAttrs);
  }

  private Map<String, Pair<QName, String>> otherAttrs() {
    return Collections.unmodifiableMap(otherAttrs);
  }

  private Map<String, String> namespaces() {
    return Collections.unmodifiableMap(namespaces);
  }

  public void validate() throws SpecParserException {
    // Nothing to validate.
  }

  protected String getText() {
    return null;
  }

  // ======================================================================================================================================

  public void toXml(final XMLStreamWriter writer) throws XMLStreamException {

    writer.setDefaultNamespace(name().getPrefix());
    for (Map.Entry<String, String> namespace : namespaces.entrySet()) {
      writer.setPrefix(namespace.getKey(), namespace.getValue());
    }
    writer.writeStartElement(qName.getNamespaceURI(), qName.getLocalPart());

    writeAttributes(writer);

    for (Pair<QName, String> attribute : otherAttrs.values()) {
      writeAttribute(writer, attribute.getKey(), attribute.getValue());
    }

    for (Map.Entry<QName, String> attribute : nsAttrs.entrySet()) {
      writeAttribute(writer, attribute.getKey(), attribute.getValue());
    }

    writeChildren(writer);

    for (SpecElement child : children) {
      child.toXml(writer);
    }

    writeText(writer);

    writer.writeEndElement();
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

  protected static void writeAttribute(final XMLStreamWriter writer,
      final QName name, final String value) throws XMLStreamException {
    writer.writeAttribute(name.getNamespaceURI(), name.getLocalPart(), value);
  }

  // ======================================================================================================================================

  @Override
  public String toString() {
    final StringWriter sw = new StringWriter();
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

    private final Map<String, QName> attrNames = new HashMap<String, QName>();

    private final QName qName;

    private final Uri base;

    protected Parser(final QName qName, final Uri base) {
      this.qName = qName;
      this.base = base;
    }

    public QName name() {
      return qName;
    }

    protected Uri getBase() {
      return base;
    }

    protected Map<String, QName> getAttrNames() {
      return attrNames;
    }

    protected void register(final Parser<? extends SpecElement> parser) {
      children.put(parser.name(), parser);
    }

    protected void register(final String... attrNames) {
      for (final String attrName : attrNames) {
        this.attrNames.put(attrName.toLowerCase(), new QName(qName
            .getNamespaceURI(), attrName));
      }
    }

    protected abstract T newElement();

    public T parse(final XMLStreamReader reader) throws SpecParserException,
        XMLStreamException {

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
            parser = new GenericElement.Parser(elementName, getBase());
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
        element
            .setAttr(reader.getAttributeName(i), reader.getAttributeValue(i));
      }
    }

    private void addNamespaces(final XMLStreamReader reader, final T element) {
      for (int i = 0; i < reader.getNamespaceCount(); i++) {
        element.addNamespace(reader.getNamespacePrefix(i), reader
            .getNamespaceURI(i));
      }
    }

    protected void addText(final XMLStreamReader reader, final T element)
        throws SpecParserException {
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
}
