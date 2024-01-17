package com.hcmute.drink.utils;

import java.util.Locale;

public class RegexUtils {
    public static String generateFilterRegexString(String filter) {
        filter = filter.toLowerCase();
        return ".*" + filter + ".*";
    }
}
