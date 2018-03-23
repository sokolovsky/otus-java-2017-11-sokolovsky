package ru.otus.sokolovsky.hw15.domain;

import ru.otus.l151.messageSystem.Address;
import ru.otus.sokolovsky.hw15.db.ChatMessageDataSet;

public interface DBService {
    void saveMessage(String author, ChatMessageDataSet messageDataSet, Address from);
}
