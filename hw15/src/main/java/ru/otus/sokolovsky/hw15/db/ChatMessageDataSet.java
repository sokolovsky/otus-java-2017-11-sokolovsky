package ru.otus.sokolovsky.hw15.db;

import ru.otus.sokolovsky.hw15.domain.DataSet;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
public class ChatMessageDataSet extends DataSet {

    @Column(name = "author_id")
    private long authorId;

    @Column
    private Date time;

    @Column
    private String text;

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthor(UserDataSet author) {
        this.authorId = author.getId();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
