package ru.otus.sokolovsky.orm;

public abstract class DataSet {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
