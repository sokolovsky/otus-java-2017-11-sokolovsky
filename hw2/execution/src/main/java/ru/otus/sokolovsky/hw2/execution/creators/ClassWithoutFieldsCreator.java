package ru.otus.sokolovsky.hw2.execution.creators;

import ru.otus.sokolovsky.hw2.execution.ObjectCreator;
import ru.otus.sokolovsky.hw2.execution.mocks.ClassWithoutFields;

public class ClassWithoutFieldsCreator implements ObjectCreator {
    @Override
    public java.lang.Object create() {
        return new ClassWithoutFields();
    }

    @Override
    public Class getCreatedClass() {
        return ClassWithoutFields.class;
    }
}
