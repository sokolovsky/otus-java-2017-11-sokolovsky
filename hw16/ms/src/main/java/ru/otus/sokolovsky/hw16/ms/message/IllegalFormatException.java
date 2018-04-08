package ru.otus.sokolovsky.hw16.ms.message;

public class IllegalFormatException extends Exception {
    public IllegalFormatException(IllegalArgumentException e) {
        super(e);
    }
}
