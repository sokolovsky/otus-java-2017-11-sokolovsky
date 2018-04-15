package ru.otus.sokolovsky.hw16.web.chat;

import java.util.function.Consumer;

public interface ChatServer extends AutoCloseable {
    void registerMessageHandler(Consumer<String> handler);

    void registerConnectionHandler(Consumer<String> handler);

    void sendAll(String message);

    void send(String message, String destination);

    void listen();

    void close();
}
