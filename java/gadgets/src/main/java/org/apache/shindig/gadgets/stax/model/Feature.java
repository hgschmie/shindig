package org.apache.shindig.gadgets.stax.model;

import javax.xml.namespace.QName;

public abstract class Feature extends AbstractSpecElement {

    private final boolean required;

    public Feature(final QName name, final boolean required) {
        super(name);
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public abstract String getName();
}
