package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.LocaleMsg;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class LocaleMsgParser extends AbstractSpecElementParser<LocaleMsg>
{
    public LocaleMsgParser() {
        this(new QName("msg"));
    }

    public LocaleMsgParser(final QName name) {
        super(name);
    }

    @Override
    protected LocaleMsg newElement()
    {
        return new LocaleMsg(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, LocaleMsg prefs, SpecElement child)
    {
    }

    @Override
    public void validate(LocaleMsg element) throws SpecParserException
    {
    }
}
