package ru.otus.sokolovsky.hw15.db;

import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.l151.messageSystem.Address;
import ru.otus.l151.messageSystem.Addressee;
import ru.otus.sokolovsky.hw15.domain.ChatDBRepository;
import ru.otus.sokolovsky.hw15.domain.UserDBRepository;

import java.util.Date;
import java.util.List;

public class DBService implements Addressee {

    private Address address = new Address("DB");

    @Autowired
    private UserDBRepository userDBRepo;

    @Autowired
    private ChatDBRepository chatDBRepo;

    @Override
    public Address getAddress() {
        return address;
    }

    public void recieveMessage(String login, Date time, String text) {
        List<UserDataSet> userDataSets = userDBRepo.readByName(login);
        if (userDataSets.size() == 0) {
            throw new RuntimeException(String.format("User with login %s isn't exists", login));
        }

        ChatMessageDataSet messageDataSet = new ChatMessageDataSet();
        messageDataSet.setAuthor(userDataSets.get(0));
        messageDataSet.setTime(time);
        messageDataSet.setText(text);

        chatDBRepo.save(messageDataSet);
    }
}
