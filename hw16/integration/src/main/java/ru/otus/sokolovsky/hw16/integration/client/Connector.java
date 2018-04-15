package ru.otus.sokolovsky.hw16.integration.client;

import ru.otus.sokolovsky.hw16.integration.message.Message;

import java.net.InetAddress;
import java.util.function.Consumer;

public interface Connector extends AutoCloseable {
    void setPort(int port);

    void connect();

    void sendMessage(Message message);

    void addMessageHandler(Consumer<Message> handler);

    void close();
}
