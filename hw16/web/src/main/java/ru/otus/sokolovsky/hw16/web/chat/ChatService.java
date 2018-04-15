package ru.otus.sokolovsky.hw16.web.chat;

public interface ChatService extends AutoCloseable {
    void start();

    void close();

    void handleRequest(String request);

    void pushMessage(ChatMessage message);

    void pushMessage(ChatMessage message, String user);
}
