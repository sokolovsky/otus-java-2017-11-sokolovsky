package ru.otus.sokolovsky.hw5.using.tests;

import ru.otus.sokolovsky.hw5.hwunit.annotations.Test;
import ru.otus.sokolovsky.hw5.hwunit.annotations.TestSuite;
import ru.otus.sokolovsky.hw5.hwunit.assertions.Assertion;

@TestSuite
public class SecondTestClass {

    @Test
    public void success() {
        Assertion.assertEquals(1,1);
        Assertion.assertNotNull(new Object());
    }

    @Test
    public void failTest() {
        Assertion.assertTrue(false);
    }

    @Test
    public void unexpectedException() {
        throw new RuntimeException("Unexpected exception was thrown");
    }
}
