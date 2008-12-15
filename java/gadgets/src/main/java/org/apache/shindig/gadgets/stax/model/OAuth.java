package org.apache.shindig.gadgets.stax.model;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;

public class OAuth extends AbstractSpecElement {

    public OAuth(final QName name) {
        super(name);
    }

    @Override
    protected void addXml(XMLStreamWriter writer)
    {
    }
}
