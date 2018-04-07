package ru.otus.sokolovsky.hw16.ms.channel;

import ru.otus.sokolovsky.hw16.ms.message.Message;

import java.util.function.Consumer;

public interface HandledChannel extends Channel {
    void registerHandler(Consumer<Message> handler);
}
