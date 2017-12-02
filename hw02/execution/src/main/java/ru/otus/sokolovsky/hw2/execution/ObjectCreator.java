package ru.otus.sokolovsky.hw2.execution;

public interface ObjectCreator {

    Object create();

    Class getCreatedClass();

    default String description() {
        return null;
    }
}
