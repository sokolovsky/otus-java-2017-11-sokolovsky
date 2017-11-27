package ru.otus.sokolovsky.hw2.execution.cases;

import ru.otus.sokolovsky.hw2.execution.MeasureCase;

public class EmptyStringCreation implements MeasureCase {
    String data;
    @Override
    public void createData() {
        data = "";
    }

    @Override
    public String description() {
        return "Creation of empty string. As we can see it's not true, because unit of measure too small.";
    }
}
