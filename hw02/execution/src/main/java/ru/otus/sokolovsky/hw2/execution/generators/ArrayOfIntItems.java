package ru.otus.sokolovsky.hw2.execution.generators;

import ru.otus.sokolovsky.hw2.execution.AbstractDataGenerator;

public class ArrayOfIntItems extends AbstractDataGenerator {

    private int[] data;

    @Override
    public void createData() {
        data = new int[count()];
    }

    @Override
    public String description() {
        return String.format("Creation array of %d int numbers", count());
    }
}
