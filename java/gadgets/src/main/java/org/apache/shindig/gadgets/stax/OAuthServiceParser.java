package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.OAuthService;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class OAuthServiceParser extends AbstractSpecElementParser<OAuthService>
{
    public OAuthServiceParser() {
        this(new QName("Service"));
        register(new OAuthRequestParser());
        register(new OAuthAccessParser());
        register(new OAuthAuthorizationParser());
    }

    public OAuthServiceParser(final QName name) {
        super(name);
    }

    @Override
    protected OAuthService newElement()
    {
        return new OAuthService(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, OAuthService prefs, SpecElement child)
    {
    }

    @Override
    public void validate(OAuthService element) throws SpecParserException
    {
    }
}
