package org.apache.shindig.gadgets.stax.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class GadgetSpec extends AbstractSpecElement {

    private ModulePrefs modulePrefs = null;

    private List<UserPref> userPrefs = new ArrayList<UserPref>();

    private Content content = null;

    public GadgetSpec(final QName name) {
        super(name);
    }

    public void setModulePrefs(final ModulePrefs modulePrefs)
    {
        if (!isSealed()) {
            this.modulePrefs = modulePrefs;
        }
    }

    public void addUserPref(final UserPref userPref)
    {
        if (!isSealed()) {
            this.userPrefs.add(userPref);
        }
    }

    public void setContent(final Content content)
    {
        if (!isSealed()) {
            this.content = content;
        }
    }

    public ModulePrefs getModulePrefs() {
        return modulePrefs;
    }

    public List<UserPref> getUserPrefs() {
        return userPrefs;
    }

    public Content getContent() {
        return content;
    }

    @Override
    protected void addXml(final XMLStreamWriter writer) throws XMLStreamException
    {
        if (modulePrefs != null) {
            modulePrefs.toXml(writer);
        }
        for (UserPref pref: userPrefs) {
            pref.toXml(writer);
        }
        if (content != null) {
            content.toXml(writer);
        }
    }
}
