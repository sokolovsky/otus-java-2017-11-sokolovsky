package ru.otus.l151.messageSystem;

public interface MessageSystemContext {
    void send(Message message);

    Address getAddress(String type);
}
