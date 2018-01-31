package ru.otus.sokolovsky.orm;

import ru.otus.sokolovsky.orm.annotations.Column;

public abstract class DataSet {

    @Column(isPrimary = true)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
