package ru.otus.sokolovsky.hw16.db.ms;

import ru.otus.sokolovsky.hw16.integration.message.Message;

import java.util.function.Consumer;

public abstract class AbstractHandler implements Consumer<Message> {
    private Consumer<Message> sender;

    public void setSender(Consumer<Message> sender) {
        this.sender = sender;
    }

    public Consumer<Message> getSender() {
        return sender;
    }
}
