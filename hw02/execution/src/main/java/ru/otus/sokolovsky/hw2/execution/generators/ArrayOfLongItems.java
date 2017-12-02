package ru.otus.sokolovsky.hw2.execution.generators;

import ru.otus.sokolovsky.hw2.execution.AbstractDataGenerator;

public class ArrayOfLongItems extends AbstractDataGenerator {

    private long[] data;

    @Override
    public void createData() {
        data = new long[count()];
    }

    @Override
    public String description() {
        return String.format("Creation array of %d long numbers", count());
    }
}
