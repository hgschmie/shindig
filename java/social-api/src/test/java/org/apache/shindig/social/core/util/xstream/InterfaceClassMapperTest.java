package org.apache.shindig.social.core.util.xstream;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.mapper.Mapper;

import junit.framework.TestCase;

import org.apache.shindig.common.PropertiesModule;
import org.apache.shindig.social.core.config.SocialApiGuiceModule;
import org.apache.shindig.social.opensocial.model.Url;

import java.util.HashMap;

public class InterfaceClassMapperTest extends TestCase {

  private Injector injector;
  private Url mockUrl;

  @Override
  public void setUp() throws Exception {
    super.setUp();

    this.mockUrl = new CustomUrl();

    this.injector = Guice.createInjector(new SocialApiGuiceModule(),
        new PropertiesModule(), new Module() {
          public void configure(Binder binder) {
            binder.bind(Url.class).to(mockUrl.getClass());
          }
        });
  }

  public void testGuiceClass() {
    Url url = injector.getInstance(Url.class);

    assertEquals(mockUrl.getClass(), url.getClass());
  }

  public void testMapping() {
    InterfaceClassMapper mapper = new InterfaceClassMapper(injector, null,
        new DummyMapper(), null, null, null, null,
        new HashMap<String, Class<?>>());

    Class<?> mapClass = mapper.realClass(Url.class.getName());
    assertEquals(mockUrl.getClass(), mapClass);
  }

  public static class CustomUrl implements Url {

    @Override
    public String getLinkText() {
      return "";
    }

    @Override
    public void setLinkText(String linkText) {
    }

    @Override
    public Boolean getPrimary() {
      return Boolean.FALSE;
    }

    @Override
    public String getType() {
      return "";
    }

    @Override
    public String getValue() {
      return "";
    }

    @Override
    public void setPrimary(Boolean primary) {
    }

    @Override
    public void setType(String type) {
    }

    @Override
    public void setValue(String value) {
    }
  }

  public static class DummyMapper implements Mapper {

    @Override
    public String aliasForAttribute(String attribute) {
      return null;
    }

    @Override
    public String attributeForAlias(String alias) {
      return null;
    }

    @Override
    public String attributeForClassDefiningField() {
      return null;
    }

    @Override
    public String attributeForEnumType() {
      return null;
    }

    @Override
    public String attributeForImplementationClass() {
      return null;
    }

    @Override
    public String attributeForReadResolveField() {
      return null;
    }

    @Override
    public Class defaultImplementationOf(Class type) {
      return null;
    }

    @Override
    public SingleValueConverter getConverterFromAttribute(String name) {
      return null;
    }

    @Override
    public SingleValueConverter getConverterFromItemType(Class type) {
      return null;
    }

    @Override
    public SingleValueConverter getConverterFromItemType(String fieldName,
        Class type) {
      return null;
    }

    @Override
    public String getFieldNameForItemTypeAndName(Class definedIn,
        Class itemType, String itemFieldName) {
      return null;
    }

    @Override
    public ImplicitCollectionMapping getImplicitCollectionDefForFieldName(
        Class itemType, String fieldName) {
      return null;
    }

    @Override
    public Class getItemTypeForItemFieldName(Class definedIn,
        String itemFieldName) {
      return null;
    }

    @Override
    public boolean isImmutableValueType(Class type) {
      return false;
    }

    @Override
    public Mapper lookupMapperOfType(Class type) {
      return null;
    }

    @Override
    public Class realClass(String elementName) {
      try {
        return Class.forName(elementName);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public String realMember(Class type, String serialized) {
      return null;
    }

    @Override
    public String serializedClass(Class type) {
      return null;
    }

    @Override
    public String serializedMember(Class type, String memberName) {
      return null;
    }

    @Override
    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
      return false;
    }
  }
}
