package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.Feature;
import org.apache.shindig.gadgets.stax.model.Icon;
import org.apache.shindig.gadgets.stax.model.Locale;
import org.apache.shindig.gadgets.stax.model.ModulePrefs;
import org.apache.shindig.gadgets.stax.model.OAuth;
import org.apache.shindig.gadgets.stax.model.Preload;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class ModulePrefsParser extends AbstractSpecElementParser<ModulePrefs>
{
    public ModulePrefsParser() {
        this(new QName("ModulePrefs"));
        register(new RequireParser());
        register(new OptionalParser());
        register(new PreloadParser());
        register(new IconParser());
        register(new LocaleParser());
        register(new LinkParser());
        register(new OAuthParser());
    }

    public ModulePrefsParser(final QName name) {
        super(name);
    }

    @Override
    protected ModulePrefs newElement()
    {
        return new ModulePrefs(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, ModulePrefs prefs, SpecElement child)
    {
        if (child instanceof Feature) {
            prefs.addFeature((Feature) child);
        } else if (child instanceof Feature) {
            prefs.addFeature((Feature) child);
        } else if (child instanceof Preload) {
            prefs.addPreload((Preload) child);
        } else if (child instanceof Icon) {
            prefs.addIcon((Icon) child);
        } else if (child instanceof Locale) {
            prefs.addLocale((Locale) child);
        } else if (child instanceof OAuth) {
            prefs.setOAuth((OAuth) child);
        } else {
            throw new IllegalArgumentException("Can not add " + child.getClass().getName() + " to " + prefs.getClass().getName());
        }
    }

    @Override
    public void validate(ModulePrefs element) throws SpecParserException
    {
        // TODO - add validation
    }

}
