package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void isPositiveNumberShouldPass() {
        assertTrue(StringUtils.isPositiveNumber("1"));
        assertTrue(StringUtils.isPositiveNumber("999"));
        assertFalse(StringUtils.isPositiveNumber("0"));
        assertFalse(StringUtils.isPositiveNumber("-999"));
    }

    @Test
    void isPositiveNumberShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> StringUtils.isPositiveNumber("number"));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.isPositiveNumber(""));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.isPositiveNumber("    "));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.isPositiveNumber("96X23"));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.isPositiveNumber("96 23 312"));
    }

    @Test
    void isPositiveNumberShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtils.isPositiveNumber(null));
    }
}
