package ru.otus.sokolovsky.hw5.hwunit.assertions;

public class Assertion {
    public static void assertNotNull(Object actual) {
        if (actual == null) {
            throw new AssertionException("Expected as not null", actual);
        }
    }

    public static void assertEquals(Object actual, Object expected) {
        if (!actual.equals(expected)) {
            throw new AssertionException("Expected equals", actual, expected);
        }
    }

    public static void assertTrue(boolean value) {
        if (!value) {
            throw new AssertionException("Expected as true", value);
        }
    }
}
