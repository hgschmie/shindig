package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.OAuthAuthorization;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class OAuthAuthorizationParser extends AbstractSpecElementParser<OAuthAuthorization>
{
    public OAuthAuthorizationParser() {
        this(new QName("OAuthAuthorization"));
    }

    public OAuthAuthorizationParser(final QName name) {
        super(name);
    }

    @Override
    protected OAuthAuthorization newElement()
    {
        return new OAuthAuthorization(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, OAuthAuthorization prefs, SpecElement child)
    {
    }

    @Override
    public void validate(OAuthAuthorization element) throws SpecParserException
    {
    }
}
