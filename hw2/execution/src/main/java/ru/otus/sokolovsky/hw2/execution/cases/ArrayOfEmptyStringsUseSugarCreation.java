package ru.otus.sokolovsky.hw2.execution.cases;

import ru.otus.sokolovsky.hw2.execution.MeasureCase;

public class ArrayOfEmptyStringsUseSugarCreation implements MeasureCase {

    String[] data;

    @Override
    public void createData() {
        data = new String[count()];
        for (int i = 0; i < data.length; i++) {
            data[i] = "";
        }
    }

    @Override
    public String description() {
        return String.format("Creation of %d empty strings using code sugar", count());
    }

    @Override
    public int count() {
        return 200_000;
    }
}
