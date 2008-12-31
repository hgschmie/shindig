package org.apache.shindig.gadgets.stax;

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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.stax.model.EnumValue;
import org.apache.shindig.gadgets.stax.model.UserPref;

public final class StaxUtils {

  private StaxUtils() {
  }

  public static final List<Pair<String, String>> orderEnumValues(
      final UserPref userPref) {
    final List<EnumValue> enumValues = userPref.getEnumValues();
    if (enumValues.size() == 0) {
      return Collections.<Pair<String, String>> emptyList();
    }

    final List<Pair<String, String>> pairs = new LinkedList<Pair<String, String>>();
    for (EnumValue value : enumValues) {
      pairs.add(new Pair<String, String>(value.getValue(), value
          .getDisplayValue()));
    }

    return Collections.unmodifiableList(pairs);
  }

  public static final Map<String, String> enumValues(final UserPref userPref) {
    final List<EnumValue> enumValues = userPref.getEnumValues();
    if (enumValues.size() == 0) {
      return Collections.<String, String> emptyMap();
    }

    final Map<String, String> values = new HashMap<String, String>();
    for (EnumValue value : enumValues) {
      values.put(value.getValue(), value.getDisplayValue());
    }

    return Collections.unmodifiableMap(values);
  }

  public static final boolean isHttpUri(final Uri uri) {
    if (uri == null) {
      return false;
    }

    return "http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme());
  }
}
