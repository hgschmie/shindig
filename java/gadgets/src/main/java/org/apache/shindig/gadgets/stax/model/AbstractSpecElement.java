package org.apache.shindig.gadgets.stax.model;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public abstract class AbstractSpecElement implements SpecElement {

    private final Logger LOG = Logger.getLogger(getClass().getName());

    protected final Map<QName, String> attributes = new HashMap<QName, String>();

    protected final Map<String, String> namespaces = new HashMap<String, String>();

    protected final List<SpecElement> children = new ArrayList<SpecElement>();

    private boolean sealed = false;

    private final QName name;

    protected AbstractSpecElement(final QName name) {
        this.name = name;
    }

    public void setAttribute(final QName name, final String value) {
        if (!sealed) {
            attributes.put(name, value);
        }
    }

    public void addChild(final SpecElement child) {
        if (!sealed) {
            children.add(child);
        }
    }

    public void addNamespace(final String prefix, final String uri) {
        if (!sealed) {
            namespaces.put(prefix, uri);
        }
    }

    public void seal() {
        sealed = true;
    }

    protected boolean isSealed() {
        return sealed;
    }

    protected QName name() {
        return name;
    }

    protected String attribute(final String key) {
        return attributes.get(new QName(name.getNamespaceURI(), key));
    }

    // ======================================================================================================================================

    public void toXml(final XMLStreamWriter writer) throws XMLStreamException {
        for (Map.Entry<String, String> namespace : namespaces.entrySet()) {
            writer.setPrefix(namespace.getKey(), namespace.getValue());
        }
        writer.writeStartElement(name.getNamespaceURI(), name.getLocalPart());
        for (Map.Entry<QName, String> attribute: attributes.entrySet()) {
            writer.writeAttribute(attribute.getKey().getNamespaceURI(), attribute.getKey().getLocalPart(), attribute.getValue());
        }

        addXml(writer);

        for (SpecElement child: children) {
            child.toXml(writer);
        }
        writer.writeEndElement();
    }

    protected abstract void addXml(final XMLStreamWriter writer) throws XMLStreamException;

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

    private void closeQuietly(final XMLStreamWriter writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (XMLStreamException xse) {
            }
        }
    }
}
