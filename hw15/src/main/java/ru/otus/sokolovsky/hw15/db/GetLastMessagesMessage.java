package ru.otus.sokolovsky.hw15.db;

import ru.otus.l151.messageSystem.Address;
import ru.otus.l151.messageSystem.Addressee;
import ru.otus.l151.messageSystem.Message;
import ru.otus.sokolovsky.hw15.domain.DBService;

import java.util.List;

public class GetLastMessagesMessage extends Message {

    private final String login;

    public GetLastMessagesMessage(Address from, Address to, String login) {
        super(from, to);
        this.login = login;
    }

    @Override
    public void exec(Addressee addressee) {
        DBService service = (DBService) addressee;
        service.loadLastMessages(login, getFrom());
    }
}
