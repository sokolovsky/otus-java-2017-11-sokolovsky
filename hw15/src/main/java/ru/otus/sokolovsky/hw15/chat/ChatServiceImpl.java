package ru.otus.sokolovsky.hw15.chat;

import ru.otus.l151.messageSystem.Address;
import ru.otus.sokolovsky.hw15.domain.ChatService;

public class ChatServiceImpl implements ChatService {

    private ChatServer chatServer;

    private Address address = new Address("frontend");

    public ChatServiceImpl(ChatServer chatServer) {
        this.chatServer = chatServer;
        chatServer.registerMessageHandler(this::handleRequest);
    }

    @Override
    public void handleRequest(String json) {
        System.out.println("To handle request: " + json);
    }

    @Override
    public void pushMessage(String message) {
        chatServer.sendAll(message);
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
