package com.ssik.manageit.util;

import java.util.StringJoiner;

public class CSVUtils {

    private CSVUtils() {}

    /**
     * Append the string representation of the given objects, comma-separated, to the provided string.
     * Empty fields can be specified by StringUtils.EMPTY or by null.
     *
     * @param sb
     * @param args
     */
    public static void append(StringBuilder sb, final Object... args) {
        for (Object obj : args) {
            // Insert a comma except at the beginning
            if (sb.length() > 0) {
                sb.append(",");
            }

            // Escape any double quotes in the given string
            if (obj != null && obj.toString().length() != 0) {
                sb.append('"' + obj.toString().replace('"', '\"') + '"');
            } else {
                // append a space so that we know there is something there...
                sb.append(' ');
            }
        }
    }

    /**
     * Add the string representation of the given objects, comma-separated, to a new line in the provided string.
     * Empty fields can be specified by StringUtils.EMPTY or by null.
     *
     * @param sb
     * @param args
     */
    public static void add(StringBuilder sb, final Object... args) {
        StringJoiner sj = new StringJoiner(",");
        for (Object obj : args) {
            // Escape any double quotes in the given string
            if (obj != null && obj.toString().length() != 0) {
                sj.add('"' + obj.toString().replace('"', '\"') + '"');
            } else {
                sj.add("");
            }
        }
        sb.append(sj);
    }

    /**
     * Terminate the current row with the system line separator.
     *
     * @param sb
     */
    public static void endRow(StringBuilder sb) {
        sb.append(System.lineSeparator());
    }
}
