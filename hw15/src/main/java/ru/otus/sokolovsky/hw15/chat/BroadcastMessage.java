package ru.otus.sokolovsky.hw15.chat;

import ru.otus.l151.messageSystem.Address;
import ru.otus.l151.messageSystem.Addressee;
import ru.otus.l151.messageSystem.Message;
import ru.otus.sokolovsky.hw15.db.ChatMessageDataSet;
import ru.otus.sokolovsky.hw15.domain.ChatService;

public class BroadcastMessage extends Message {
    private ChatMessageDataSet chatMessage;

    public BroadcastMessage(Address from, Address to, ChatMessageDataSet chatMessage) {
        super(from, to);
        this.chatMessage = chatMessage;
    }

    @Override
    public void exec(Addressee addressee) {
        ChatService chat = (ChatService) addressee;
        chat.pushMessage(chatMessage);
    }
}
