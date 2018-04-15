package ru.otus.sokolovsky.hw16.db.db;

import ru.otus.sokolovsky.hw16.db.domain.DataSet;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class ChatMessageDataSet extends DataSet {

    @Column(name = "author_id")
    private long authorId;

    @Column
    private LocalDateTime time;

    @Column
    private String text;

    private String author;

    public String getAuthor() {
        return author;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthor(UserDataSet author) {
        this.authorId = author.getId();
        this.author = author.getLogin();
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
