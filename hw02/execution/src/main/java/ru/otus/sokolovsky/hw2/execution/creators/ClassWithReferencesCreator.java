package ru.otus.sokolovsky.hw2.execution.creators;

import ru.otus.sokolovsky.hw2.execution.ObjectCreator;
import ru.otus.sokolovsky.hw2.execution.mocks.ClassWithReferences;

public class ClassWithReferencesCreator implements ObjectCreator {
    @Override
    public ClassWithReferences create() {
        return new ClassWithReferences();
    }

    @Override
    public Class getCreatedClass() {
        return ClassWithReferences.class;
    }
}
