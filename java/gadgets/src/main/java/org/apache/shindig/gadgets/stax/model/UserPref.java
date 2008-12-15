package org.apache.shindig.gadgets.stax.model;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;

public class UserPref extends AbstractSpecElement {

    public UserPref(final QName name) {
        super(name);
    }

    @Override
    protected void addXml(XMLStreamWriter writer)
    {
    }
}
