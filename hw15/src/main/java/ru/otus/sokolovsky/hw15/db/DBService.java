package ru.otus.sokolovsky.hw15.db;

import org.springframework.context.annotation.Lazy;
import ru.otus.l151.messageSystem.Address;
import ru.otus.l151.messageSystem.Addressee;
import ru.otus.sokolovsky.hw15.chat.BroadcastMessage;
import ru.otus.sokolovsky.hw15.domain.ChatDBRepository;
import ru.otus.sokolovsky.hw15.domain.MessageSystemContext;
import ru.otus.sokolovsky.hw15.domain.UserDBRepository;

import java.util.List;

public class DBService implements ru.otus.sokolovsky.hw15.domain.DBService, Addressee {

    private Address address = new Address("DB");

    private UserDBRepository userDBRepo;

    private ChatDBRepository chatDBRepo;

    private MessageSystemContext msContext;

    @Override
    public Address getAddress() {
        return address;
    }

    public DBService(UserDBRepository userDBRepo, ChatDBRepository chatDBRepo) {

        this.userDBRepo = userDBRepo;
        this.chatDBRepo = chatDBRepo;
    }

    @Override
    public void setMessageSystemContext(MessageSystemContext msContext) {
        this.msContext = msContext;
    }

    @Override
    public void saveMessage(String author, ChatMessageDataSet messageDataSet, Address initializer) {
        List<UserDataSet> users = userDBRepo.readByLogin(author);
        messageDataSet.setAuthor(users.get(0));
        chatDBRepo.save(messageDataSet);
        msContext.send(new BroadcastMessage(getAddress(), initializer, messageDataSet));
    }
}
