package ru.otus.sokolovsky.hw5.hwunit.assertions;

public class AssertionException extends RuntimeException {

    private String message;
    private Object actual;
    private Object expected;
    private boolean withoutExpectedValue = false;

    AssertionException(String message, Object actual) {
        this.message = message;
        this.actual = actual;
        withoutExpectedValue = true;
    }

    AssertionException(String message, Object actual, Object expected) {
        this.message = message;
        this.actual = actual;
        this.expected = expected;
    }

    public String getExpected() {
        if (expected == null) {
            return "";
        }
        return expected.toString();
    }

    public String getActual() {
        return actual.toString();
    }

    @Override
    public String getMessage() {
        if (withoutExpectedValue) {
            return message;
        }
        return String.format(
                "\n%s\nActual:%s\nExpected:%s\n",
                message,
                getActual(),
                getExpected()
        );
    }
}
