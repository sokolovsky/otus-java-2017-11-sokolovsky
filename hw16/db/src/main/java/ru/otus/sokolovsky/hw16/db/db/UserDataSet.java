package ru.otus.sokolovsky.hw16.db.db;

import ru.otus.sokolovsky.hw15.domain.DataSet;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class UserDataSet extends DataSet {

    @Column
    private String login;

    @Column
    private String password;

    @Column
    private int age;

    public String getLogin() {
        return login;
    }

    public void setLogin(String name) {
        this.login = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
