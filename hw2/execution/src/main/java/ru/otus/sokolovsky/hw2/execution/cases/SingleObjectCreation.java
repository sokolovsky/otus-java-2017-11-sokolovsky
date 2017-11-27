package ru.otus.sokolovsky.hw2.execution.cases;

import ru.otus.sokolovsky.hw2.execution.MeasureCase;

public class SingleObjectCreation implements MeasureCase {
    private Object object;

    @Override
    public void createData() {
        object = new Object();
    }

    @Override
    public String description() {
        return "Single object creation. As we can see it's not true, because unit of measure too small.";
    }
}
