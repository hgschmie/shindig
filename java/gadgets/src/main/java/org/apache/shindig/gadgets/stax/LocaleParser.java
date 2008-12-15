package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.Locale;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class LocaleParser extends AbstractSpecElementParser<Locale>
{
    public LocaleParser() {
        this(new QName("Locale"));
        register(new LocaleMsgParser());
    }

    public LocaleParser(final QName name) {
        super(name);
    }

    @Override
    protected Locale newElement()
    {
        return new Locale(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, Locale prefs, SpecElement child)
    {
    }

    @Override
    public void validate(Locale element) throws SpecParserException
    {
    }
}
