package ru.otus.sokolovsky.hw16.integration.client;

import ru.otus.sokolovsky.hw16.integration.message.Message;

import java.util.function.Consumer;

public abstract class AbstractHandler implements Consumer<Message> {
    private Connector connector;

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public Consumer<Message> getSender() {
        return connector::sendMessage;
    }
}
