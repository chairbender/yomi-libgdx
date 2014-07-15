package com.chairbender.yomi.api.csv;

/**
 * Created by Kyle on 6/28/2014.
 */
public class CSVUtility {

    /**
     *
     * @param s string to try to parse an int from
     * @param defaultInt int to use if parse failed
     * @return the int from s or default int if parsing failed
     */
    public static int parseIntWithDefault(String s, int defaultInt) {

        try {
            int i = Integer.parseInt(s);
            return i;
        } catch (Exception e) {
            return defaultInt;
        }
    }
}
