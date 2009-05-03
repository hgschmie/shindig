package org.apache.shindig.common.util;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

public final class BooleanConvertUtils {

    private BooleanConvertUtils() {
    }

    public static final boolean toBoolean(final String value) {
        final String val = StringUtils.trimToEmpty(value);
        return "1".equals(val) || BooleanUtils.toBoolean(val);
    }
}
