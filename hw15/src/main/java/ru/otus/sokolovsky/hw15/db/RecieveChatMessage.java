package ru.otus.sokolovsky.hw15.db;

import ru.otus.l151.messageSystem.Address;
import ru.otus.l151.messageSystem.Addressee;
import ru.otus.l151.messageSystem.Message;
import ru.otus.sokolovsky.hw15.domain.ChatMessage;
import ru.otus.sokolovsky.hw15.domain.DBService;

public class RecieveChatMessage extends Message {

    private final String login;
    private final ChatMessage chatMessage;

    public RecieveChatMessage(Address from, Address to, String login, ChatMessage chatMessage) {
        super(from, to);
        this.login = login;
        this.chatMessage = chatMessage;
    }

    @Override
    public void exec(Addressee addressee) {
        DBService service = (DBService) addressee;
        service.saveMessage(login, chatMessage, this.getFrom());
    }
}
