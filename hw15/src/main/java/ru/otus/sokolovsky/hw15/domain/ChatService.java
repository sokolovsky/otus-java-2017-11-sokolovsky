package ru.otus.sokolovsky.hw15.domain;

import ru.otus.l151.messageSystem.Addressee;

public interface ChatService extends Addressee {
    void handleRequest(String request);

    void pushMessage(ChatMessage message);

    void pushMessage(ChatMessage message, String user);
}
