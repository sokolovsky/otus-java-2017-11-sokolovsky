package ru.otus.sokolovsky.hw5.using.tests;

import ru.otus.sokolovsky.hw5.hwunit.annotations.After;
import ru.otus.sokolovsky.hw5.hwunit.annotations.Before;
import ru.otus.sokolovsky.hw5.hwunit.annotations.Test;
import ru.otus.sokolovsky.hw5.hwunit.annotations.TestSuite;
import ru.otus.sokolovsky.hw5.hwunit.assertions.Assertion;

@TestSuite
public class TestClass {

    @Before
    public void runBefore() {
        System.out.println("Run before method");
    }

    @Test
    public void sumOfTwoNumbers() {
        System.out.println("Run testing method");
        Assertion.assertEquals(5, 1 + 4);
    }

    @Test(expected = RuntimeException.class)
    public void customException() {
        System.out.println("Run testing method");
        throw new RuntimeException("Exception for testing class");
    }

    @After
    public void runAfter() {
        System.out.println("Run after method");
    }
}
