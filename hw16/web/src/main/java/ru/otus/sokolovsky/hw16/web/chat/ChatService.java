package ru.otus.sokolovsky.hw16.web.chat;

public interface ChatService {
    void handleRequest(String request);

    void pushMessage(String message);

    void pushMessage(String message, String user);
}
