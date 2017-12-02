package ru.otus.sokolovsky.hw2.execution.creators;

import ru.otus.sokolovsky.hw2.execution.ObjectCreator;

public class EmptyStringWithConstructor implements ObjectCreator {

    @Override
    public String create() {
        return new String("");
    }

    @Override
    public Class getCreatedClass() {
        return String.class;
    }

    @Override
    public String description() {
        return "created with sugar";
    }
}
