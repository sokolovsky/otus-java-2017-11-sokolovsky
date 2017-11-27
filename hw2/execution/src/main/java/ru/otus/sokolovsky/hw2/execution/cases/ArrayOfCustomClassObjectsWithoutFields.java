package ru.otus.sokolovsky.hw2.execution.cases;

import ru.otus.sokolovsky.hw2.execution.MeasureCase;
import ru.otus.sokolovsky.hw2.execution.mocks.ClassWithoutFields;

public class ArrayOfCustomClassObjectsWithoutFields implements MeasureCase {

    ClassWithoutFields[] data;

    @Override
    public void createData() {
        data = new ClassWithoutFields[count()];
        for (int i = 0; i < data.length; i++) {
            data[i] = new ClassWithoutFields();
        }
    }

    @Override
    public String description() {
        return String.format("Creation of %d objects of class without fields", count());
    }

    @Override
    public int count() {
        return 200_000;
    }
}
