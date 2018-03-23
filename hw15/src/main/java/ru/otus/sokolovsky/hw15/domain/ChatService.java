package ru.otus.sokolovsky.hw15.domain;

import ru.otus.l151.messageSystem.Addressee;
import ru.otus.sokolovsky.hw15.db.ChatMessageDataSet;

public interface ChatService extends Addressee {
    void handleRequest(String request);

    void pushMessage(ChatMessageDataSet message);
}
