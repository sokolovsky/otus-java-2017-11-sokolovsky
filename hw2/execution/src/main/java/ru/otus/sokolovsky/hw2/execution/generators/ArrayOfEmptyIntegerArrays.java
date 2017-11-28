package ru.otus.sokolovsky.hw2.execution.generators;

import ru.otus.sokolovsky.hw2.execution.AbstractDataGenerator;

public class ArrayOfEmptyIntegerArrays extends AbstractDataGenerator {

    private int[][] data;

    @Override
    public void createData() {
        data = new int[count()][0];
    }

    @Override
    public String description() {
        return String.format("Creation of %d empty integer arrays", count());
    }
}
