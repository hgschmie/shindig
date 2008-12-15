package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.Link;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class LinkParser extends AbstractSpecElementParser<Link>
{
    public LinkParser() {
        this(new QName("Link"));
    }

    public LinkParser(final QName name) {
        super(name);
    }

    @Override
    protected Link newElement()
    {
        return new Link(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, Link prefs, SpecElement child)
    {
    }

    @Override
    public void validate(Link element) throws SpecParserException
    {
    }
}
