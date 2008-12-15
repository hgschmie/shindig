package org.apache.shindig.gadgets.stax.model;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;

public class LocaleMsg extends AbstractSpecElement {

    public LocaleMsg(final QName name) {
        super(name);
    }

    @Override
    protected void addXml(XMLStreamWriter writer)
    {
    }
}
