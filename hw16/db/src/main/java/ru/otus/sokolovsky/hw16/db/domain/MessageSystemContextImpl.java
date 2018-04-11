package ru.otus.sokolovsky.hw16.db.domain;

import ru.otus.l151.messageSystem.*;

import java.util.HashMap;
import java.util.Map;

public class MessageSystemContextImpl implements MessageSystemContext {

    private final MessageSystem messageSystem;
    private final DBService dbService;

    private Map<String, Address> addresses = new HashMap<>();

    public MessageSystemContextImpl(MessageSystem messageSystem, Map<String, Addressee> addressees, DBService dbService) {
        this.messageSystem = messageSystem;
        this.dbService = dbService;
        addressees.forEach(this::addAddressee);
        messageSystem.start();
    }

    private void addAddressee(String type, Addressee addressee) {
        addresses.put(type, addressee.getAddress());
        messageSystem.addAddressee(addressee);
        addressee.setMessageSystemContext(this);
    }

    @Override
    public Address getAddress(String type) {
        return addresses.get(type);
    }

    @Override
    public void send(Message message) {
        messageSystem.sendMessage(message);
    }
}
