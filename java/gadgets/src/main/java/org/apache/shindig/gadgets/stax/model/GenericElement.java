package org.apache.shindig.gadgets.stax.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class GenericElement extends AbstractSpecElement {

    private List<SpecElement> children = new ArrayList<SpecElement>();

    public GenericElement(final QName name) {
        super(name);
    }

    public void addChild(final SpecElement child)
    {
        if (!isSealed()) {
            this.children.add(child);
        }
    }

    public List<SpecElement> getChildren() {
        return children;
    }

    @Override
    protected void addXml(final XMLStreamWriter writer) throws XMLStreamException
    {
        for (SpecElement child : children) {
            child.toXml(writer);
        }
    }
}
