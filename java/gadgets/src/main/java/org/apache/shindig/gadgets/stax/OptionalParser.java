package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.Optional;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class OptionalParser extends AbstractSpecElementParser<Optional>
{
    public OptionalParser() {
        this(new QName("Optional"));
    }

    public OptionalParser(final QName name) {
        super(name);
    }

    @Override
    protected Optional newElement()
    {
        return new Optional(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, Optional prefs, SpecElement child)
    {
    }

    @Override
    public void validate(Optional element) throws SpecParserException
    {
    }
}
