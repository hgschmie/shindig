package org.apache.shindig.gadgets.stax;

import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.stax.model.SpecElement;
import org.apache.shindig.gadgets.stax.model.SpecElement.Parser;



public final class StaxTestUtils {

  private static final XMLInputFactory factory;

  static {
    factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
  }

  private StaxTestUtils() {
  }

  public static final <T extends SpecElement> T parseElement(final String xml, final Parser<T> parser) throws SpecParserException {

    XMLStreamReader reader = null;
    try {
      reader = factory.createXMLStreamReader(new StringReader(xml));
      T element = null;

      loop:
        while (true) {
          final int event = reader.next();
          switch (event) {
          case XMLStreamConstants.END_DOCUMENT:
            reader.close();
            break loop;
          case XMLStreamConstants.START_ELEMENT:
            element = parser.parse(reader);
            break;
          default:
            break;
          }
        }

      return element;
    } catch (XMLStreamException e) {
      throw new SpecParserException("Could not parse XML:", e);
    }
  }
}
