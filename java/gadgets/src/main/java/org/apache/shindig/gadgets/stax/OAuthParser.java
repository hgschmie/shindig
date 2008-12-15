package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.OAuth;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class OAuthParser extends AbstractSpecElementParser<OAuth>
{
    public OAuthParser() {
        this(new QName("OAuth"));
        register(new OAuthServiceParser());
    }

    public OAuthParser(final QName name) {
        super(name);
    }

    @Override
    protected OAuth newElement()
    {
        return new OAuth(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, OAuth prefs, SpecElement child)
    {
    }

    @Override
    public void validate(OAuth element) throws SpecParserException
    {
    }
}
