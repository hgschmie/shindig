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

package org.apache.shindig.gadgets.spec;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.common.util.Pair;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.StaxSupport;
import org.apache.shindig.gadgets.variables.Substitutions;

public abstract class SpecElement {

  public static final String OPENSOCIAL_NAMESPACE_URI = "http://ns.opensocial.org/specs/0.8/";

  public static final String MESSAGEBUNDLE_NAMESPACE_URI = "http://ns.opensocial.org/messagebundle/0.8/";

  private final Logger LOG = Logger.getLogger(getClass().getName());

  private final QName qName;

  private final Uri base;

  private final Map<String, QName> attrNames;

  private final Map<String, String> attrs = new HashMap<String, String>();

  private final Map<String, Pair<QName, String>> otherAttrs = new HashMap<String, Pair<QName, String>>();

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
  }

  protected SpecElement(final SpecElement specElement, final Substitutions substituter) {
    this.qName = specElement.name();
    this.attrNames = specElement.attrNames();
    this.base = specElement.getBase();

    this.nsAttrs = specElement.nsAttrs();
    this.namespaces = specElement.namespaces();

    this.cdataFlag = specElement.isCDATA();

    // Copy known keys over.
    for (String key : attrNames.keySet()) {
      setAttr(key, specElement.attr(key));
    }

    for (Pair<QName, String> pair : specElement.otherAttrs().values()) {
      setAttr(pair.getKey(), substituter.substituteString(pair.getValue()));
    }
  }

  protected Uri getBase() {
    return base;
  }

  public abstract SpecElement substitute(final Substitutions subs) throws GadgetException;

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

  public String getAttribute(final String key) {
    final Pair<QName, String> result = otherAttrs.get(key.toLowerCase());
    return result != null ? result.getValue() : null;
  }

  public int getIntAttribute(final String key) {
    return NumberUtils.toInt(getAttribute(key));
  }

  public Map<String, String> getAttributes() {
    Map<String, String> localAttributes = new HashMap<String, String>();

    for (Pair<QName, String> pairs : otherAttrs.values()) {
      localAttributes.put(pairs.getKey().getLocalPart(), pairs.getValue());
    }

    return Collections.unmodifiableMap(localAttributes);
  }

  // ======================================================================================================================================

  protected String attr(final String key) {
    return attrs.get(key.toLowerCase());
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

  protected Uri attrUriBase(final String key) {
    final Uri uri = attrUriNull(key);
      if (base != null && uri !=  null) {
          return base.resolve(uri);
      } else {
          return uri;
      }
  }

  protected boolean attrIsValidUri(final String key) {
    return attr(key) == null || attrUriNull(key) != null;
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

  private List<SpecElement> children() {
    return Collections.unmodifiableList(children);
  }

  protected Map<String, String> attributes() {
    return Collections.unmodifiableMap(attrs);
  }

  public void validate() throws GadgetException {
    // Nothing to validate.
  }

  protected String getText() {
    return null;
  }

  // ======================================================================================================================================

  /**
   * Should never be called directly, StaxSupport takes care of this and calls this for the root element that
   * you want to write.
   */
  public void prepareWriter(final XMLStreamWriter writer) throws XMLStreamException {
    writer.setDefaultNamespace(name().getNamespaceURI());
    for (Map.Entry<String, String> namespace : namespaces.entrySet()) {
      writer.setPrefix(namespace.getKey(), namespace.getValue());
    }
  }

  public void toXml(final XMLStreamWriter writer) throws XMLStreamException {

    writer.writeStartElement(qName.getNamespaceURI(), qName.getLocalPart());

    writeAttributes(writer);

    for (Pair<QName, String> attribute : otherAttrs.values()) {
      writeAttribute(writer, attribute.getKey().getLocalPart(), attribute.getValue());
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
    if (XMLConstants.DEFAULT_NS_PREFIX.equals(name.getPrefix())) {
      writer.writeAttribute(name.getLocalPart(), value);
    } else {
      writer.writeAttribute(name.getNamespaceURI(), name.getLocalPart(), value);
    }
  }

  protected void writeAttribute(final XMLStreamWriter writer,
      final String localName, final String value) throws XMLStreamException {
    if (XMLConstants.DEFAULT_NS_PREFIX.equals(qName.getPrefix())) {
      writer.writeAttribute(localName, value);
    } else {
      writer.writeAttribute(qName.getNamespaceURI(), localName, value);
    }
  }

  // ======================================================================================================================================

  @Override
  public String toString() {
    final StringWriter sw = new StringWriter();
    XMLStreamWriter writer = null;
    try {
      // This is bad. But then again, don't really use toString() in
      // a real world scenario, use toXml and manage the writer yourself.
      writer = new StaxSupport().getWriter(sw, this);

      toXml(writer);
      writer.flush();
    } catch (XMLStreamException xse) {
      LOG.warning("While writing output stream: " + xse.getMessage());
    } finally {
      StaxSupport.closeQuietly(writer);
    }
    sw.flush();
    return sw.toString();
  }

  @Override
  public boolean equals(final Object other) {
    if (other instanceof SpecElement == false) {
      return false;
    }
    if (this == other) {
      return true;
    }
    SpecElement rhs = (SpecElement) other;
    return new EqualsBuilder()
                  .append(name(), rhs.name())
                  .append(getBase(), rhs.getBase())
                  .append(namespaces(), rhs.namespaces())
                  .append(nsAttrs(), rhs.nsAttrs())
                  .append(otherAttrs(), rhs.otherAttrs())
                  .append(children(), rhs.children())
                  .append(isCDATA(), rhs.isCDATA())
                  .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
      .append(name())
      .append(getBase())
      .append(namespaces())
      .append(nsAttrs())
      .append(otherAttrs())
      .append(children())
      .append(isCDATA())
      .toHashCode();
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
    
    protected static QName buildChildName(final QName parent, final QName child, final QName defaultName) {
      return new QName(parent.getNamespaceURI(), child != null ? child.getLocalPart() : defaultName.getLocalPart());
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

    public T parse(final XMLStreamReader reader) throws GadgetException,
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
          element.validate();
          return element;
        case XMLStreamConstants.END_DOCUMENT:
          throw new SpecParserException("Unexpected end of document encountered!");

        case XMLStreamConstants.START_ELEMENT:
          final QName elementName = buildQName(reader.getName());
          Parser<? extends SpecElement> parser = children.get(elementName);
          if (parser == null) {
            LOG.fine("No idea what to do with " + elementName + ", ignoring!");
            parser = new GenericElement.Parser(elementName, getBase());
          }

          final SpecElement child = parser.parse(reader);
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

    private QName buildQName(final QName elementName) {
      if (XMLConstants.DEFAULT_NS_PREFIX.equals(elementName.getPrefix())) {
        return new QName(name().getNamespaceURI(), elementName.getLocalPart());
      } else {
        return elementName;
      }
    }

    private void addAttributes(final XMLStreamReader reader, final T element) {
      for (int i = 0; i < reader.getAttributeCount(); i++) {
        element
            .setAttr(buildQName(reader.getAttributeName(i)), reader.getAttributeValue(i));
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
        final SpecElement child) throws GadgetException {
      if (child instanceof GenericElement) {
        element.addChild(child);
      } else {
        throw new IllegalStateException("The element" + element.name()
            + " does not accept any nested elements, saw " + child.name());
      }
    }
  }
}
