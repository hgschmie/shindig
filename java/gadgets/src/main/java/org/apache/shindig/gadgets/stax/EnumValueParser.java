package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.EnumValue;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class EnumValueParser extends AbstractSpecElementParser<EnumValue>
{
    public EnumValueParser() {
        this(new QName("EnumValue"));
    }

    public EnumValueParser(final QName name) {
        super(name);
    }

    @Override
    protected EnumValue newElement()
    {
        return new EnumValue(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, EnumValue prefs, SpecElement child)
    {
    }

    @Override
    public void validate(EnumValue element) throws SpecParserException
    {
    }
}
