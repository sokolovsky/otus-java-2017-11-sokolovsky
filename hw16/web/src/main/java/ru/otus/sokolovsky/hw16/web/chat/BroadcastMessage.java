package ru.otus.sokolovsky.hw16.web.chat;

import ru.otus.l151.messageSystem.Address;
import ru.otus.l151.messageSystem.Addressee;
import ru.otus.l151.messageSystem.Message;
import ru.otus.sokolovsky.hw15.domain.ChatMessage;
import ru.otus.sokolovsky.hw15.domain.ChatService;

public class BroadcastMessage extends Message {
    private ChatMessage chatMessage;

    public BroadcastMessage(Address from, Address to, ChatMessage chatMessage) {
        super(from, to);
        this.chatMessage = chatMessage;
    }

    @Override
    public void exec(Addressee addressee) {
        ChatService chat = (ChatService) addressee;
        chat.pushMessage(chatMessage);
    }
}
