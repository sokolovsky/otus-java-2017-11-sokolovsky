package ru.otus.sokolovsky.hw5.hwunit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks test method in suite class
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    Class<? extends Throwable> expected() default NoneExpected.class;

    public static class NoneExpected extends Throwable {
    }
}
