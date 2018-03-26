package ru.otus.sokolovsky.hw15.domain;

import ru.otus.l151.messageSystem.Address;
import ru.otus.sokolovsky.hw15.db.ChatMessageDataSet;

import java.util.List;

public interface DBService {
    void saveMessage(String author, ChatMessageDataSet messageDataSet, Address initializer);

    void loadLastMessages(String user, Address initializer);
}
