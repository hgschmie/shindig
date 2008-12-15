package org.apache.shindig.gadgets.stax.model;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;

public class Require extends Feature {

    public Require(final QName name) {
        super(name, true);
    }

    public String getName() {
        return attribute("feature");
    }

    @Override
    protected void addXml(XMLStreamWriter writer)
    {
    }
}
