package ru.otus.sokolovsky.hw15.chat;

import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.l151.messageSystem.Address;
import ru.otus.sokolovsky.hw15.domain.ChatService;

public class ChatServiceImpl implements ChatService {

    @Autowired
    WebSocketServer webSocketServer;

    Address address = new Address("frontend");

    @Override
    public void handleRequest(String request) {
    }

    @Override
    public void pushMessage(String message) {
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
