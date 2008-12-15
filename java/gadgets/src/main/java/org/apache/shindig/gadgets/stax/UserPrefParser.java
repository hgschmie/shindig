package org.apache.shindig.gadgets.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.UserPref;
import org.apache.shindig.gadgets.stax.model.SpecElement;



public class UserPrefParser extends AbstractSpecElementParser<UserPref>
{
    public UserPrefParser() {
        this(new QName("UserPref"));
        register(new EnumValueParser());
    }

    public UserPrefParser(final QName name) {
        super(name);
    }

    @Override
    protected UserPref newElement()
    {
        return new UserPref(getName());
    }

    @Override
    protected void addChild(XMLStreamReader reader, UserPref prefs, SpecElement child)
    {
    }

    @Override
    public void validate(UserPref element) throws SpecParserException
    {
    }
}
