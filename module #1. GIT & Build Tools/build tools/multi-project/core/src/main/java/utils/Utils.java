package utils;

import org.apache.commons.lang3.Validate;

import java.util.Arrays;

public class Utils {
    private Utils() {
    }

    public static boolean isAllPositiveNumbers(String... str) {
        Validate.notEmpty(str);
        return Arrays.stream(str).allMatch(StringUtils::isPositiveNumber);
    }
}
