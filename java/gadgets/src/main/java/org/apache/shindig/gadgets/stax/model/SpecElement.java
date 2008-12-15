package org.apache.shindig.gadgets.stax.model;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public interface SpecElement {

    void seal();

    void setAttribute(final QName name, final String value);

    void addNamespace(final String prefix, final String uri);

    void addChild(final SpecElement element);

    void toXml(final XMLStreamWriter writer) throws XMLStreamException;
}
