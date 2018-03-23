package ru.otus.sokolovsky.hw15.domain;

import ru.otus.l151.messageSystem.Address;
import ru.otus.l151.messageSystem.Addressee;
import ru.otus.l151.messageSystem.Message;
import ru.otus.l151.messageSystem.MessageSystem;

import java.util.HashMap;
import java.util.Map;

public class MessageSystemContext {

    private final MessageSystem messageSystem;

    public enum AddressType {
        DB, FRONT
    }

    Map<AddressType, Address> addresses = new HashMap<>();

    public MessageSystemContext(MessageSystem messageSystem, Map<AddressType, Addressee> addressees) {
        this.messageSystem = messageSystem;
        addressees.forEach(this::addAddressee);
    }

    public void addAddressee(AddressType type, Addressee addressee) {
        addresses.put(type, addressee.getAddress());
        messageSystem.addAddressee(addressee);
    }

    public Address getAddress(AddressType type) {
        return addresses.get(type);
    }

    public void send(Message message) {
        messageSystem.sendMessage(message);
    }
}
