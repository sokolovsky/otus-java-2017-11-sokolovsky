package ru.otus.sokolovsky.hw15.domain;

import javax.persistence.*;

@Entity
@Table(name = "phone")
public class PhoneDataSet extends DataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserDataSet user;

    @Column
    private String number;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setOwner(UserDataSet user) {
        this.user = user;
    }

    public UserDataSet getOwner() {
        return user;
    }
}
