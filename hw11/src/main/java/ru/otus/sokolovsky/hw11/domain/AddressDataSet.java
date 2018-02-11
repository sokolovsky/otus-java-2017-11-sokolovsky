package ru.otus.sokolovsky.hw11.domain;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class AddressDataSet extends DataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String street;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
