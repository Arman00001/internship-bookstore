package com.arman.internshipbookstore.service.util;

public class StringUtils {
    public static String removeSingleQuotes(String str) {
        String result = str;
        if (result.startsWith("'")) result = result.substring(1);
        if (result.endsWith("'")) result = result.substring(0, result.length() - 1);

        return result;
    }

    public static String removeSquareBrackets(String str) {
        return str.replace("[", "").replace("]", "");
    }

}
