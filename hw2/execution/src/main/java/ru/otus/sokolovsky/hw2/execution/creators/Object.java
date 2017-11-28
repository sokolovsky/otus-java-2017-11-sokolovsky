package ru.otus.sokolovsky.hw2.execution.creators;

import ru.otus.sokolovsky.hw2.execution.ObjectCreator;

public class Object implements ObjectCreator {
    @Override
    public java.lang.Object create() {
        return new java.lang.Object();
    }

    @Override
    public Class getCreatedClass() {
        return java.lang.Object.class;
    }
}
