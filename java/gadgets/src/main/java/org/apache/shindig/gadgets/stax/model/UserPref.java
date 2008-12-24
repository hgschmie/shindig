package org.apache.shindig.gadgets.stax.model;

/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shindig.gadgets.spec.SpecParserException;

public class UserPref extends SpecElement {
  public static final String ELEMENT_NAME = "UserPref";

  private static final String ATTR_NAME = "name";
  private static final String ATTR_DISPLAY_NAME = "display_name";
  private static final String ATTR_DEFAULT_VALUE = "default_value";
  private static final String ATTR_REQUIRED = "required";
  private static final String ATTR_DATATYPE = "datatype";
  private static final String ATTR_URLPARAM = "urlparam";
  private static final String ATTR_AUTOCOMPLETE_URL = "autocomplete_url";
  private static final String ATTR_NUM_MINVAL = "num_minval";
  private static final String ATTR_NUM_MAXVAL = "num_maxval";
  private static final String ATTR_STR_MAXLEN = "str_maxlen";
  private static final String ATTR_RESTRICT_TO_COMPLETIONS = "restrict_to_completions";
  private static final String ATTR_PREFIX_MATCH = "prefix_match";
  private static final String ATTR_PUBLISH = "publish";
  private static final String ATTR_LISTEN = "listen";
  private static final String ATTR_ON_CHANGE = "on_change";
  private static final String ATTR_GROUP = "group";

  private String name = null;
  private String displayName = null;
  private String defaultValue = null;
  private String required = null;
  private String datatype = null;
  private String urlparam = null;
  private String autocompleteUrl = null;
  private String numMinval = null;
  private String numMaxval = null;
  private String strMaxlen = null;
  private String restrictToCompletions = null;
  private String prefixMatch = null;
  private String publish = null;
  private String listen = null;
  private String onChange = null;
  private String group = null;

  private List<EnumValue> enumValues = new LinkedList<EnumValue>();

  public UserPref(final QName name) {
    super(name);
  }

  public String getName() {
    return name;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getRequired() {
    return required;
  }

  public DataType getDataType() {
    return DataType.parse(datatype);
  }

  public String getUrlparam() {
    return urlparam;
  }

  public String getAutocompleteUrl() {
    return autocompleteUrl;
  }

  public double getNumMinval() {
    return NumberUtils.toDouble(numMinval);
  }

  public double getNumMaxval() {
    return NumberUtils.toDouble(numMaxval);
  }

  public int getStrMaxlen() {
    return NumberUtils.toInt(strMaxlen);
  }

  public String getRestrictToCompletions() {
    return restrictToCompletions;
  }

  public boolean getPrefixMatch() {
    return BooleanUtils.toBoolean(prefixMatch);
  }

  public boolean getPublish() {
    return BooleanUtils.toBoolean(publish);
  }

  public boolean getListen() {
    return BooleanUtils.toBoolean(listen);
  }

  public String getOnChange() {
    return onChange;
  }

  public String getGroup() {
    return group;
  }

  public List<EnumValue> getEnumValues() {
    return Collections.unmodifiableList(enumValues);
  }

  private void setName(final String name) {
    this.name = name;
  }

  private void setDisplayName(final String displayName) {
    this.displayName = displayName;
  }

  private void setDefaultValue(final String defaultValue) {
    this.defaultValue = defaultValue;
  }

  private void setRequired(final String required) {
    this.required = required;
  }

  private void setDataType(final String datatype) {
    this.datatype = datatype;
  }

  private void setUrlparam(final String urlparam) {
    this.urlparam = urlparam;
  }

  private void setAutocompleteUrl(final String autocompleteUrl) {
    this.autocompleteUrl = autocompleteUrl;
  }

  private void setNumMinval(final String numMinval) {
    this.numMinval = numMinval;
  }

  private void setNumMaxval(final String numMaxval) {
    this.numMaxval = numMaxval;
  }

  private void setStrMaxlen(final String strMaxlen) {
    this.strMaxlen = strMaxlen;
  }

  private void setRestrictToCompletions(final String restrictToCompletions) {
    this.restrictToCompletions = restrictToCompletions;
  }

  private void setPrefixMatch(final String prefixMatch) {
    this.prefixMatch = prefixMatch;
  }

  private void setPublish(final String publish) {
    this.publish = publish;
  }

  private void setListen(final String listen) {
    this.listen = listen;
  }

  private void setOnChange(final String onChange) {
    this.onChange = onChange;
  }

  private void setGroup(final String group) {
    this.group = group;
  }

  private void addEnumValue(final EnumValue enumValue) {
    this.enumValues.add(enumValue);
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();

    if (name != null) {
      writer.writeAttribute(namespaceURI, ATTR_NAME, getName());
    }

    if (displayName != null) {
      writer.writeAttribute(namespaceURI, ATTR_DISPLAY_NAME, getDisplayName());
    }

    if (defaultValue != null) {
      writer
          .writeAttribute(namespaceURI, ATTR_DEFAULT_VALUE, getDefaultValue());
    }

    if (required != null) {
      writer.writeAttribute(namespaceURI, ATTR_REQUIRED, getRequired());
    }

    if (datatype != null) {
      writer.writeAttribute(namespaceURI, ATTR_DATATYPE, getDataType()
          .toString());
    }

    if (urlparam != null) {
      writer.writeAttribute(namespaceURI, ATTR_URLPARAM, getUrlparam());
    }

    if (autocompleteUrl != null) {
      writer.writeAttribute(namespaceURI, ATTR_AUTOCOMPLETE_URL,
          getAutocompleteUrl());
    }

    if (numMinval != null) {
      writer.writeAttribute(namespaceURI, ATTR_NUM_MINVAL, String
          .valueOf(getNumMinval()));
    }

    if (numMaxval != null) {
      writer.writeAttribute(namespaceURI, ATTR_NUM_MAXVAL, String
          .valueOf(getNumMaxval()));
    }

    if (strMaxlen != null) {
      writer.writeAttribute(namespaceURI, ATTR_STR_MAXLEN, String
          .valueOf(getStrMaxlen()));
    }

    if (restrictToCompletions != null) {
      writer.writeAttribute(namespaceURI, ATTR_RESTRICT_TO_COMPLETIONS,
          getRestrictToCompletions());
    }

    if (prefixMatch != null) {
      writer.writeAttribute(namespaceURI, ATTR_PREFIX_MATCH, String
          .valueOf(getPrefixMatch()));
    }

    if (publish != null) {
      writer.writeAttribute(namespaceURI, ATTR_PUBLISH, String
          .valueOf(getPublish()));
    }

    if (listen != null) {
      writer.writeAttribute(namespaceURI, ATTR_LISTEN, String
          .valueOf(getListen()));
    }

    if (onChange != null) {
      writer.writeAttribute(namespaceURI, ATTR_ON_CHANGE, getOnChange());
    }

    if (group != null) {
      writer.writeAttribute(namespaceURI, ATTR_GROUP, getGroup());
    }
  }

  @Override
  protected void writeChildren(final XMLStreamWriter writer)
      throws XMLStreamException {
    for (EnumValue enumValue : enumValues) {
      enumValue.toXml(writer);
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (name == null) {
      throw new SpecParserException(name().getLocalPart()
          + "@name must be set!");
    }
  }

  /**
   * Possible values for UserPref@datatype
   */
  public static enum DataType {
    STRING, HIDDEN, BOOL, ENUM, LIST, NUMBER;

    /**
     * Parses a data type from the input string.
     *
     * @param value
     * @return The data type of the given value.
     */
    public static DataType parse(String value) {
      for (DataType type : DataType.values()) {
        if (type.toString().compareToIgnoreCase(value) == 0) {
          return type;
        }
      }
      return STRING;
    }
  }

  public static class Parser extends SpecElement.Parser<UserPref> {

    private final QName attrName;
    private final QName attrDisplayName;
    private final QName attrDefaultValue;
    private final QName attrRequired;
    private final QName attrDatatype;
    private final QName attrUrlparam;
    private final QName attrAutocompleteUrl;
    private final QName attrNumMinval;
    private final QName attrNumMaxval;
    private final QName attrStrMaxlen;
    private final QName attrRestrictToCompletions;
    private final QName attrPrefixMatch;
    private final QName attrPublish;
    private final QName attrListen;
    private final QName attrOnChange;
    private final QName attrGroup;

    public Parser() {
      this(new QName(ELEMENT_NAME));
    }

    public Parser(final QName name) {
      super(name);
      register(new EnumValue.Parser());
      this.attrName = buildQName(name, ATTR_NAME);
      this.attrDisplayName = buildQName(name, ATTR_DISPLAY_NAME);
      this.attrDefaultValue = buildQName(name, ATTR_DEFAULT_VALUE);
      this.attrRequired = buildQName(name, ATTR_REQUIRED);
      this.attrDatatype = buildQName(name, ATTR_DATATYPE);
      this.attrUrlparam = buildQName(name, ATTR_URLPARAM);
      this.attrAutocompleteUrl = buildQName(name, ATTR_AUTOCOMPLETE_URL);
      this.attrNumMinval = buildQName(name, ATTR_NUM_MINVAL);
      this.attrNumMaxval = buildQName(name, ATTR_NUM_MAXVAL);
      this.attrStrMaxlen = buildQName(name, ATTR_STR_MAXLEN);
      this.attrRestrictToCompletions = buildQName(name,
          ATTR_RESTRICT_TO_COMPLETIONS);
      this.attrPrefixMatch = buildQName(name, ATTR_PREFIX_MATCH);
      this.attrPublish = buildQName(name, ATTR_PUBLISH);
      this.attrListen = buildQName(name, ATTR_LISTEN);
      this.attrOnChange = buildQName(name, ATTR_ON_CHANGE);
      this.attrGroup = buildQName(name, ATTR_GROUP);
    }

    @Override
    protected UserPref newElement() {
      return new UserPref(getName());
    }

    @Override
    protected void setAttribute(final UserPref userPref, final QName name,
        final String value) {
      if (name.equals(attrName)) {
        userPref.setName(value);
      } else if (name.equals(attrDisplayName)) {
        userPref.setDisplayName(value);
      } else if (name.equals(attrDefaultValue)) {
        userPref.setDefaultValue(value);
      } else if (name.equals(attrRequired)) {
        userPref.setRequired(value);
      } else if (name.equals(attrDatatype)) {
        userPref.setDataType(value);
      } else if (name.equals(attrUrlparam)) {
        userPref.setUrlparam(value);
      } else if (name.equals(attrAutocompleteUrl)) {
        userPref.setAutocompleteUrl(value);
      } else if (name.equals(attrNumMinval)) {
        userPref.setNumMinval(value);
      } else if (name.equals(attrNumMaxval)) {
        userPref.setNumMaxval(value);
      } else if (name.equals(attrStrMaxlen)) {
        userPref.setStrMaxlen(value);
      } else if (name.equals(attrRestrictToCompletions)) {
        userPref.setRestrictToCompletions(value);
      } else if (name.equals(attrPrefixMatch)) {
        userPref.setPrefixMatch(value);
      } else if (name.equals(attrPublish)) {
        userPref.setPublish(value);
      } else if (name.equals(attrListen)) {
        userPref.setListen(value);
      } else if (name.equals(attrOnChange)) {
        userPref.setOnChange(value);
      } else if (name.equals(attrGroup)) {
        userPref.setGroup(value);
      } else {
        super.setAttribute(userPref, name, value);
      }
    }

    @Override
    protected void addChild(XMLStreamReader reader, final UserPref userPref,
        final SpecElement child) throws SpecParserException {
      if (child instanceof EnumValue) {
        userPref.addEnumValue((EnumValue) child);
      } else {
        super.addChild(reader, userPref, child);
      }
    }
  }
}
