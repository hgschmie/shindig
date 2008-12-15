package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.OAuthAccess;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class OAuthAccessParser extends AbstractSpecElementParser<OAuthAccess>
{
    public OAuthAccessParser() {
        this(new QName("OAuthAccess"));
    }

    public OAuthAccessParser(final QName name) {
        super(name);
    }

    @Override
    protected OAuthAccess newElement()
    {
        return new OAuthAccess(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, OAuthAccess prefs, SpecElement child)
    {
    }

    @Override
    public void validate(OAuthAccess element) throws SpecParserException
    {
    }
}
