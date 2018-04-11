package ru.otus.sokolovsky.hw16.db.domain;

import java.time.LocalDateTime;

public interface ChatMessage {
    LocalDateTime getTime();
    void setTime(LocalDateTime now);

    String getAuthor();

    String getText();
    void setText(String message);
}
