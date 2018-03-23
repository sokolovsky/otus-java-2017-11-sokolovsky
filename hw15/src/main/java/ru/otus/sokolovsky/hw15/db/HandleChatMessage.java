package ru.otus.sokolovsky.hw15.db;

import ru.otus.l151.messageSystem.Address;
import ru.otus.l151.messageSystem.Addressee;
import ru.otus.l151.messageSystem.Message;
import ru.otus.sokolovsky.hw15.domain.DBService;

public class HandleChatMessage extends Message {

    private final String login;
    private final ChatMessageDataSet chatMessage;

    public HandleChatMessage(Address from, Address to, String login, ChatMessageDataSet chatMessage) {
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
