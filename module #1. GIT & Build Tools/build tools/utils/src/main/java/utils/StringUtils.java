package utils;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;

public class StringUtils {
    private StringUtils() {
    }

    public static boolean isPositiveNumber(String str) {
        Validate.notBlank(str, "Input parameter is blank...");
        if (!NumberUtils.isCreatable(str)) {
            throw new IllegalArgumentException("Input parameter is not a number...");
        }
        return NumberUtils.createDouble(str) > 0;
    }
}
