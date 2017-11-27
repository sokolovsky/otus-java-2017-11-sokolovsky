package ru.otus.sokolovsky.hw2.execution.cases;

import ru.otus.sokolovsky.hw2.execution.MeasureCase;
import ru.otus.sokolovsky.hw2.execution.mocks.ClassWithPrimitiveFields;
import ru.otus.sokolovsky.hw2.execution.mocks.ClassWithReferences;

public class ArrayOfCustomClassObjectsWithPrimitiveReferences implements MeasureCase {

    ClassWithReferences[] data;

    @Override
    public void createData() {
        data = new ClassWithReferences[count()];
    }

    @Override
    public String description() {
        return String.format("Creation of %d objects of class with references", count());
    }

    @Override
    public int count() {
        return 200_000;
    }
}
