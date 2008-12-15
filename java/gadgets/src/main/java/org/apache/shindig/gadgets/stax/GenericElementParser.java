package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.GenericElement;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class GenericElementParser extends AbstractSpecElementParser<GenericElement>
{
    public GenericElementParser(final QName name) {
        super(name);
    }

    @Override
    protected GenericElement newElement()
    {
        return new GenericElement(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, final GenericElement element, SpecElement child)
    {
        element.addChild(child);
    }

    @Override
    public void validate(GenericElement element) throws SpecParserException
    {
    }
}
