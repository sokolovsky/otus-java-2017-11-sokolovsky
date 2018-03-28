package ru.otus.sokolovsky.hw15.domain;

import ru.otus.l151.messageSystem.Address;
import ru.otus.l151.messageSystem.Message;

import java.util.List;

public interface ServiceMessageFactory {

    Message createGetLastMessagesMessage(Address from, Address to, String login);

    Message createReceiveChatMessage(Address from, Address to, String login, ChatMessage chatMessage);

    Message createSenderOfUserBulkMessages(Address from, Address to, String user, List<ChatMessage> list);

    Message createBroadcastMessage(Address from, Address to, ChatMessage chatMessage);
}
