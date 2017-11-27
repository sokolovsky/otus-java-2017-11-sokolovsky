package ru.otus.sokolovsky.hw2.execution.cases;

import ru.otus.sokolovsky.hw2.execution.MeasureCase;

public class ArrayOfIntItems implements MeasureCase {

    int[] data;

    @Override
    public void createData() {
        data = new int[count()];
    }

    @Override
    public String description() {
        return String.format("Creation array of %d int numbers", count());
    }

    @Override
    public int count() {
        return 200_000;
    }
}
