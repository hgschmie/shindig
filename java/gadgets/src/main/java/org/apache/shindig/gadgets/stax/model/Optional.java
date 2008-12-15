package org.apache.shindig.gadgets.stax.model;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;

public class Optional extends Feature {

    public Optional(final QName name) {
        super(name, false);
    }

    public String getName() {
        return attribute("feature");
    }

    @Override
    protected void addXml(XMLStreamWriter writer)
    {
    }
}
