package ru.otus.sokolovsky.hw15.chat;

import ru.otus.l151.messageSystem.Address;
import ru.otus.l151.messageSystem.Addressee;
import ru.otus.l151.messageSystem.Message;
import ru.otus.sokolovsky.hw15.db.ChatMessageDataSet;
import ru.otus.sokolovsky.hw15.domain.ChatService;

import java.util.List;

public class UserBulkMessage extends Message {
    private String user;
    private List<ChatMessageDataSet> list;

    public UserBulkMessage(Address from, Address to, String user, List<ChatMessageDataSet> list) {
        super(from, to);
        this.user = user;
        this.list = list;
    }

    @Override
    public void exec(Addressee addressee) {
        ChatService chat = (ChatService) addressee;
        list.stream().forEach((ChatMessageDataSet message) -> chat.pushMessage(message, user));
    }
}
