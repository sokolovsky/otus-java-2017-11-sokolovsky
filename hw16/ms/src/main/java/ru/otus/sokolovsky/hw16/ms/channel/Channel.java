package ru.otus.sokolovsky.hw16.ms.channel;

import ru.otus.sokolovsky.hw16.ms.message.Message;

public interface Channel {
    String getName();

    void addMessage(Message message);

    boolean hasMessages();

    Message poll();

    void confirm(Message message);

    int confirmWaited();
}
