package org.apache.shindig.gadgets.stax;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.SpecElement;

public abstract class AbstractSpecElementParser<T extends SpecElement> {

    private static final Logger LOG = Logger.getLogger(AbstractSpecElementParser.class.getName());

    private final Map<QName, AbstractSpecElementParser<? extends SpecElement>> children = new HashMap<QName, AbstractSpecElementParser<? extends SpecElement>>();

    private final QName name;

    protected AbstractSpecElementParser(final QName name) {
        this.name = name;
    }

    public QName getName() {
        return name;
    }

    protected void register(AbstractSpecElementParser<? extends SpecElement> parseElement) {
        children.put(parseElement.getName(), parseElement);
    }

    public T parse(final XMLStreamReader reader) throws IllegalStateException, XMLStreamException, SpecParserException {

        // This assumes, that parse it at the right element.
        T element = newElement();
        addAttributes(reader, element);
        addNamespaces(reader, element);

        while(true) {
            int event = reader.next();

            switch (event) {
                case XMLStreamConstants.ATTRIBUTE:
                    addAttributes(reader, element);
                    break;
                case XMLStreamConstants.END_ELEMENT:
                case XMLStreamConstants.END_DOCUMENT:
                    element.seal();
                    return element;

                case XMLStreamConstants.START_ELEMENT:
                    final QName elementName = reader.getName();
                    if (children.containsKey(elementName)) {
                        final SpecElement child = children.get(elementName).parse(reader);
                        addChild(reader, element, child);
                    } else {// Ignore non-defined children. TODO: Does that make sense?
                        LOG.warning("No idea what to do with " + elementName + ", ignoring!");
                        final SpecElement child = new GenericElementParser(elementName).parse(reader);

                        element.addChild(child);
                    }
                    break;
                default:
                    break; // TODO: Do we need to parse more things?
            }
        }
    }

    protected abstract T newElement();

    protected void addAttributes(final XMLStreamReader reader, final T element) {
        for (int i=0; i < reader.getAttributeCount(); i++) {
            element.setAttribute(reader.getAttributeName(i), reader.getAttributeValue(i));
        }
    }

    protected void addNamespaces(final XMLStreamReader reader, final T element) {
        for (int i=0; i < reader.getNamespaceCount(); i++) {
            element.addNamespace(reader.getNamespacePrefix(i), reader.getNamespaceURI(i));
        }
    }

    protected abstract void addChild(final XMLStreamReader reader, final T element, final SpecElement child);

    public abstract void validate(final T element) throws SpecParserException;
}
