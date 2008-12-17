package org.apache.shindig.gadgets.stax;

import org.apache.shindig.common.uri.Uri;

public final class StaxUtils {

  private static final Uri EMPTY_URI = Uri.parse("");

    private StaxUtils() {
    }

  public static final Uri toUri(final String value) {
    if (value != null) {
      try {
        return Uri.parse(value);
      } catch (IllegalArgumentException e) {
        return EMPTY_URI;
      }
    } else {
      return EMPTY_URI;
    }
  }
}
