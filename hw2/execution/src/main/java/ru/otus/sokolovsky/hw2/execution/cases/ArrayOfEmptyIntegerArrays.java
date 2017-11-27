package ru.otus.sokolovsky.hw2.execution.cases;

import ru.otus.sokolovsky.hw2.execution.MeasureCase;

public class ArrayOfEmptyIntegerArrays implements MeasureCase {

    int[][] data;

    @Override
    public void createData() {
        data = new int[count()][0];
    }

    @Override
    public String description() {
        return String.format("Creation of %d empty integer arrays", count());
    }

    @Override
    public int count() {
        return 200_000;
    }
}
