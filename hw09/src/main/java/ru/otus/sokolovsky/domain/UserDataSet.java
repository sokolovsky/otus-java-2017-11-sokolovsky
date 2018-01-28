package ru.otus.sokolovsky.domain;

import ru.otus.sokolovsky.orm.DataSet;
import ru.otus.sokolovsky.orm.annotations.Column;
import ru.otus.sokolovsky.orm.annotations.Entity;

@Entity(tableName = "users")
public class UserDataSet extends DataSet {
    @Column
    private String name;
    @Column
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
