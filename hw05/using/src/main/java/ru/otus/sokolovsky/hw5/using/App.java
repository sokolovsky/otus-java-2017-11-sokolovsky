package ru.otus.sokolovsky.hw5.using;

import ru.otus.sokolovsky.hw5.hwunit.Runner;
import ru.otus.sokolovsky.hw5.using.tests.TestClass;

public class App {
    public static void main(String[] args) {
        System.out.println("\nThe first test run\n");
        Runner.run(TestClass.class);

        System.out.println("\nThe second test run\n");
        Runner.run("ru.otus.sokolovsky.hw5.using.tests");
    }
}
