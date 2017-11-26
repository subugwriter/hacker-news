package com.xy.hackernews;

/**
 * Created by xy on 25/11/2017.
 */

public class UnitTestUtil {

    public static String concatStringsWSep(Iterable<Integer> integers, String separator) {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for (Integer i : integers) {
            sb.append(sep).append(i);
            sep = separator;
        }
        return sb.toString();
    }
}
