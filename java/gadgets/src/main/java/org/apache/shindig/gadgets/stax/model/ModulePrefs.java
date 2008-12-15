package org.apache.shindig.gadgets.stax.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.gadgets.spec.LinkSpec;
import org.apache.shindig.gadgets.spec.LocaleSpec;
import org.apache.shindig.gadgets.spec.OAuthSpec;

public class ModulePrefs extends AbstractSpecElement {

    private final List<Preload> preloads = new ArrayList<Preload>();
    private final Map<String, Feature> features = new HashMap<String, Feature>();
    private final List<Icon> icons = new ArrayList<Icon>();
    private final Map<Locale, LocaleSpec> locales = new HashMap<Locale, LocaleSpec>();
    private final Map<String, LinkSpec> links = new HashMap<String, LinkSpec>();
    private OAuthSpec oauthSpec = null;

    public ModulePrefs(final QName name) {
        super(name);
    }

    public List<Preload> getPreloads()
    {
        return preloads;
    }

    public Map<String, Feature> getFeatures()
    {
        return features;
    }

    public List<Icon> getIcons()
    {
        return icons;
    }

    public Map<Locale, LocaleSpec> getLocales()
    {
        return locales;
    }

    public Map<String, LinkSpec> getLinks()
    {
        return links;
    }

    public OAuthSpec getOauthSpec()
    {
        return oauthSpec;
    }

    public void addPreload(final Preload preload) {
        if (!isSealed()) {
            preloads.add(preload);
        }
    }

    public void addFeature(final Feature feature) {
        if (!isSealed()) {
            features.put(feature.getName(), feature);
        }
    }

    public void addIcon(final Icon icon) {
        if (!isSealed()) {
            icons.add(icon);
        }
    }

    public void addLocale(final Locale locale) {
        if (!isSealed()) {
        }
    }

    public void addLink(final Link link) {
        if (!isSealed()) {
        }
    }

    public void setOAuth(final OAuth oauth) {
    }


    @Override
    protected void addXml(XMLStreamWriter writer) throws XMLStreamException
    {
        for (Preload preload : preloads) {
            preload.toXml(writer);
        }
        for (Feature feature: features.values()) {
            feature.toXml(writer);
        }
        for (Icon icon : icons) {
            icon.toXml(writer);
        }
/*        for (Locale locale : locales.values()) {
            locale.toXml(writer);
        }
        for (Link link : links) {
            links.toXml(writer);
        }
        if (oauth != null) {
            oauth.toXml(writer);
        }
        */
    }
}
