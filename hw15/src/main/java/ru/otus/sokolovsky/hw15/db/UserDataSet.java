package ru.otus.sokolovsky.hw15.db;

import ru.otus.sokolovsky.hw15.domain.DataSet;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "user")
public class UserDataSet extends DataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private int age;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<ChatMessageDataSet> messages = new LinkedList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public List<ChatMessageDataSet> getMessages() {
        return messages;
    }

    public void addPhone(ChatMessageDataSet message) {
        this.messages.add(message);
        message.setAuthor(this);
    }
}
