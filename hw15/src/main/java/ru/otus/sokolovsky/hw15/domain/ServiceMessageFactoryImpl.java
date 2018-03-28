package ru.otus.sokolovsky.hw15.domain;

import ru.otus.l151.messageSystem.Address;
import ru.otus.l151.messageSystem.Message;
import ru.otus.sokolovsky.hw15.chat.BroadcastMessage;
import ru.otus.sokolovsky.hw15.chat.UserBulkMessage;
import ru.otus.sokolovsky.hw15.db.GetLastMessagesMessage;
import ru.otus.sokolovsky.hw15.db.RecieveChatMessage;

import java.util.List;

public class ServiceMessageFactoryImpl implements ServiceMessageFactory {
    @Override
    public Message createGetLastMessagesMessage(Address from, Address to, String login) {
        return new GetLastMessagesMessage(from, to, login);
    }

    @Override
    public Message createReceiveChatMessage(Address from, Address to, String login, ChatMessage chatMessage) {
        return new RecieveChatMessage(from, to, login, chatMessage);
    }

    @Override
    public Message createSenderOfUserBulkMessages(Address from, Address to, String user, List<ChatMessage> list) {
        return new UserBulkMessage(from, to, user, list);
    }

    @Override
    public Message createBroadcastMessage(Address from, Address to, ChatMessage chatMessage) {
        return new BroadcastMessage(from, to, chatMessage);
    }
}
