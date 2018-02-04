package ru.otus.sokolovsky.hw10.myorm;

import javax.persistence.*;

@Entity
public abstract class DataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
