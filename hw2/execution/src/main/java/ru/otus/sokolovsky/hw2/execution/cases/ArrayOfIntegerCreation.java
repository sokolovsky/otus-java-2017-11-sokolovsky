package ru.otus.sokolovsky.hw2.execution.cases;

import ru.otus.sokolovsky.hw2.execution.MeasureCase;

public class ArrayOfIntegerCreation implements MeasureCase {

    private int[] data;

    @Override
    public void createData() {
        data = new int[count()];
    }

    @Override
    public String description() {
        return String.format("Creation of %d array items", count());
    }

    @Override
    public int count() {
        return 200_000;
    }
}
