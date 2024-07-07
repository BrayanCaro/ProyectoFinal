package org.brayancaro.records;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Coordinate(int x, int y) {
    public static final String PATTERN = "\\s*(?<x>\\d+)[^\\d]+(?<y>\\d+)\\s*";

    public static Coordinate parse(String coordinate) {
        Matcher matcher = Pattern.compile(PATTERN).matcher(coordinate);

        return new Coordinate(
                parseAxis(matcher.group("x")),
                parseAxis(matcher.group("y")));
    }

    protected static int parseAxis(String token) {
        int value = Integer.parseUnsignedInt(token);
        // the user input token is 1 based
        return Math.max(value - 1, 0);
    }
}
