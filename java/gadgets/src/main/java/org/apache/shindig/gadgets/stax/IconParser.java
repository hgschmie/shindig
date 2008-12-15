package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.Icon;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class IconParser extends AbstractSpecElementParser<Icon>
{
    public IconParser() {
        this(new QName("Icon"));
    }

    public IconParser(final QName name) {
        super(name);
    }

    @Override
    protected Icon newElement()
    {
        return new Icon(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, Icon prefs, SpecElement child)
    {
    }

    @Override
    public void validate(Icon element) throws SpecParserException
    {
    }
}
