package ru.otus.sokolovsky.hw2.execution.cases;

import ru.otus.sokolovsky.hw2.execution.MeasureCase;
import ru.otus.sokolovsky.hw2.execution.mocks.ClassWithPrimitiveFields;
import ru.otus.sokolovsky.hw2.execution.mocks.ClassWithoutFields;

public class ArrayOfCustomClassObjectsWithPrimitiveFields implements MeasureCase {

    ClassWithPrimitiveFields[] data;

    @Override
    public void createData() {
        data = new ClassWithPrimitiveFields[count()];
        for (int i = 0; i < data.length; i++) {
            data[i] = new ClassWithPrimitiveFields();
        }
    }

    @Override
    public String description() {
        return String.format("Creation of %d objects of class with primitive fields (int, long)", count());
    }

    @Override
    public int count() {
        return 200_000;
    }
}
