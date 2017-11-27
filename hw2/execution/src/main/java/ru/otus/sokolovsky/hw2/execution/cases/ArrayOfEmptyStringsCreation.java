package ru.otus.sokolovsky.hw2.execution.cases;

import ru.otus.sokolovsky.hw2.execution.MeasureCase;

public class ArrayOfEmptyStringsCreation implements MeasureCase {

    String[] data;

    @Override
    public void createData() {
        data = new String[count()];
        for (int i = 0; i < data.length; i++) {
            data[i] = new String("");
        }
    }

    @Override
    public String description() {
        return String.format("Creation of %d empty strings with constructor", count());
    }

    @Override
    public int count() {
        return 200_000;
    }
}
