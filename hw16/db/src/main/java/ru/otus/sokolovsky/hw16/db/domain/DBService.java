package ru.otus.sokolovsky.hw16.db.domain;

import ru.otus.l151.messageSystem.Address;

public interface DBService {
    void saveMessage(String author, ChatMessage message, Address initializer);

    void loadLastMessages(String user, Address initializer);
}
