package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.OAuthRequest;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class OAuthRequestParser extends AbstractSpecElementParser<OAuthRequest>
{
    public OAuthRequestParser() {
        this(new QName("OAuthRequest"));
    }

    public OAuthRequestParser(final QName name) {
        super(name);
    }

    @Override
    protected OAuthRequest newElement()
    {
        return new OAuthRequest(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, OAuthRequest prefs, SpecElement child)
    {
    }

    @Override
    public void validate(OAuthRequest element) throws SpecParserException
    {
    }
}
