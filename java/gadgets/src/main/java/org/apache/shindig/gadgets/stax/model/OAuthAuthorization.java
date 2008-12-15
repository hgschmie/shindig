package org.apache.shindig.gadgets.stax.model;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;

public class OAuthAuthorization extends AbstractSpecElement {

    public OAuthAuthorization(final QName name) {
        super(name);
    }

    @Override
    protected void addXml(XMLStreamWriter writer)
    {
    }
}
