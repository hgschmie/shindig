package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.Require;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class RequireParser extends AbstractSpecElementParser<Require>
{
    public RequireParser() {
        this(new QName("Require"));
        register(new FeatureParamParser());
    }

    public RequireParser(final QName name) {
        super(name);
    }

    @Override
    protected Require newElement()
    {
        return new Require(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, Require prefs, SpecElement child)
    {
    }

    @Override
    public void validate(Require element) throws SpecParserException
    {
    }
}
