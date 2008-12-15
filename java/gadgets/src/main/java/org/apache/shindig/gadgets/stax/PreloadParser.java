package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.Preload;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class PreloadParser extends AbstractSpecElementParser<Preload>
{
    public PreloadParser() {
        this(new QName("Preload"));
    }

    public PreloadParser(final QName name) {
        super(name);
    }

    @Override
    protected Preload newElement()
    {
        return new Preload(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, Preload prefs, SpecElement child)
    {
    }

    @Override
    public void validate(Preload element) throws SpecParserException
    {
    }
}
