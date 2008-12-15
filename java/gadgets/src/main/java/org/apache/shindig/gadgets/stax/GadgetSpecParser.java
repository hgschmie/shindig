package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.Content;
import org.apache.shindig.gadgets.stax.model.GadgetSpec;
import org.apache.shindig.gadgets.stax.model.ModulePrefs;
import org.apache.shindig.gadgets.stax.model.SpecElement;
import org.apache.shindig.gadgets.stax.model.UserPref;



public class GadgetSpecParser extends AbstractSpecElementParser<GadgetSpec>
{
    public GadgetSpecParser() {
        super(new QName("Module"));
        register(new ModulePrefsParser());
        register(new UserPrefParser());
        register(new ContentParser());
    }

    @Override
    protected GadgetSpec newElement()
    {
        return new GadgetSpec(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, GadgetSpec spec, SpecElement child)
    {
        if (child instanceof ModulePrefs) {
            spec.setModulePrefs((ModulePrefs) child);
        } else if (child instanceof UserPref) {
            spec.addUserPref((UserPref) child);
        } else if (child instanceof Content) {
            spec.setContent((Content) child);
        } else {
            throw new IllegalArgumentException("Can not add " + child.getClass().getName() + " to " + spec.getClass().getName());
        }
    }

    @Override
    public void validate(final GadgetSpec element) throws SpecParserException
    {
        if (element.getModulePrefs() == null) {
            throw new SpecParserException("No <ModulePrefs> section found!");
        }
        if (element.getContent() == null) {
            throw new SpecParserException("No <Content> section found!");
        }
    }
}
