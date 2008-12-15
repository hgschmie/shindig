package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.Content;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class ContentParser extends AbstractSpecElementParser<Content>
{
    public ContentParser() {
        this(new QName("Content"));
    }

    public ContentParser(final QName name) {
        super(name);
    }

    @Override
    protected void addAttributes(XMLStreamReader reader, Content element)
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void addChild(XMLStreamReader reader, Content content, SpecElement child)
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected Content newElement()
    {
        return new Content(getName());
    }

    @Override
    public void validate(Content element) throws SpecParserException
    {
        // TODO add validation
    }

}
