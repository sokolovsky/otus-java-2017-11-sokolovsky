package ru.otus.sokolovsky.hw2.execution.cases;

import ru.otus.sokolovsky.hw2.execution.MeasureCase;

public class ArrayOfLongItems implements MeasureCase {

    long[] data;

    @Override
    public void createData() {
        data = new long[count()];
    }

    @Override
    public String description() {
        return String.format("Creation array of %d long numbers", count());
    }

    @Override
    public int count() {
        return 200_000;
    }
}
