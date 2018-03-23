package ru.otus.sokolovsky.hw15.chat;

import java.util.function.Consumer;

public interface ChatServer {
    void registerMessageHandler(Consumer<String> handler);

    void sendAll(String message);

    void start();
}
