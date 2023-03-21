package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void isPositiveNumberShouldPass() {
        assertTrue(StringUtils.isPositiveNumber("1"));
        assertTrue(StringUtils.isPositiveNumber("222"));
        assertTrue(StringUtils.isPositiveNumber("9999"));
        assertFalse(StringUtils.isPositiveNumber("0"));
        assertFalse(StringUtils.isPositiveNumber("-100"));
        assertFalse(StringUtils.isPositiveNumber("-999"));
    }

    @Test
    void isPositiveNumberShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> StringUtils.isPositiveNumber("noNumber"));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.isPositiveNumber(""));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.isPositiveNumber(" "));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.isPositiveNumber("97_12"));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.isPositiveNumber("26 233 112 1"));
    }

    @Test
    void isPositiveNumberShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtils.isPositiveNumber(null));
    }
}
