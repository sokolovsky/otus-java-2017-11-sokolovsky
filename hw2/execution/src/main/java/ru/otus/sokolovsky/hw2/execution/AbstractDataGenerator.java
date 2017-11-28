package ru.otus.sokolovsky.hw2.execution;

abstract public class AbstractDataGenerator implements DataGenerator, Cloneable {
    private int count = 0;

    @Override
    public int count() {
        return count;
    }

    @Override
    public void setCount(int value) {
        count = value;
    }

    public AbstractDataGenerator clone() throws CloneNotSupportedException {
        return (AbstractDataGenerator) super.clone();
    }
}
