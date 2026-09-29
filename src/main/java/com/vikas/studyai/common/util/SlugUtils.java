package com.vikas.studyai.common.util;

import java.util.Locale;

public final class SlugUtils {
    private SlugUtils() { }

    public static String slugify(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT)
                .trim().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }
}
