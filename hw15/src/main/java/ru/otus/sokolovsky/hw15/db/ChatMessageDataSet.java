package ru.otus.sokolovsky.hw15.db;

import ru.otus.sokolovsky.hw15.domain.DataSet;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "messages")
public class ChatMessageDataSet extends DataSet {

    @Column(name = "author_id")
    private long authorId;

    @Column
    private LocalDateTime time;

    @Column
    private String text;

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthor(UserDataSet author) {
        this.authorId = author.getId();
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
