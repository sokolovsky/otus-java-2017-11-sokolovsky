package ru.otus.sokolovsky.hw16.integration.client;

import ru.otus.sokolovsky.hw16.integration.message.Message;

import java.util.function.Consumer;

public interface Connector extends AutoCloseable {
    void setPort(int port);

    void connect();

    void sendMessage(Message message);

    Message sendMessageAndWaitResponse(Message message, int waitingInSec) throws InterruptedException;

    void addMessageHandler(Consumer<Message> handler);

    void addChannelMessageHandler(String channelName, Consumer<Message> handler);

    void close();
}
