package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.FeatureParam;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class FeatureParamParser extends AbstractSpecElementParser<FeatureParam>
{
    public FeatureParamParser() {
        this(new QName("Param"));
    }

    public FeatureParamParser(final QName name) {
        super(name);
    }

    @Override
    protected FeatureParam newElement()
    {
        return new FeatureParam(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, FeatureParam prefs, SpecElement child)
    {
    }

    @Override
    public void validate(FeatureParam element) throws SpecParserException
    {
    }
}
