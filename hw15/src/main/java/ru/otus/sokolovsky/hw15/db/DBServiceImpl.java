package ru.otus.sokolovsky.hw15.db;

import ru.otus.l151.messageSystem.Address;
import ru.otus.l151.messageSystem.Addressee;
import ru.otus.l151.messageSystem.MessageSystemContext;
import ru.otus.sokolovsky.hw15.chat.BroadcastMessage;
import ru.otus.sokolovsky.hw15.domain.ChatDBRepository;
import ru.otus.sokolovsky.hw15.domain.ChatMessage;
import ru.otus.sokolovsky.hw15.domain.ServiceMessageFactory;
import ru.otus.sokolovsky.hw15.domain.UserDBRepository;

import java.util.List;
import java.util.stream.Collectors;

public class DBServiceImpl  implements ru.otus.sokolovsky.hw15.domain.DBService, Addressee {
    private static int LATEST_MESSAGES_LIMIT = 10;

    private Address address = new Address("DB");

    private ServiceMessageFactory serviceMessageFactory;
    private UserDBRepository userDBRepo;

    private ChatDBRepository chatDBRepo;

    private MessageSystemContext msContext;

    @Override
    public Address getAddress() {
        return address;
    }

    public DBServiceImpl(ServiceMessageFactory serviceMessageFactory, UserDBRepository userDBRepo, ChatDBRepository chatDBRepo) {
        this.serviceMessageFactory = serviceMessageFactory;
        this.userDBRepo = userDBRepo;
        this.chatDBRepo = chatDBRepo;
    }

    @Override
    public void setMessageSystemContext(MessageSystemContext msContext) {
        this.msContext = msContext;
    }

    @Override
    public void saveMessage(String author, ChatMessage message, Address initializer) {
        List<UserDataSet> users = userDBRepo.readByLogin(author);
        ChatMessageDataSet chatMessageDataSet = (ChatMessageDataSet) message;
        chatMessageDataSet.setAuthor(users.get(0));
        chatDBRepo.save(chatMessageDataSet);
        msContext.send(serviceMessageFactory.createBroadcastMessage(getAddress(), initializer, message));
    }

    @Override
    public void loadLastMessages(String user, Address initializer) {
        List<ChatMessageDataSet> chatMessageDataSets = chatDBRepo.readLast(LATEST_MESSAGES_LIMIT);
        chatMessageDataSets.forEach((ChatMessageDataSet message) -> {
            // it's awful but orm is not designed to work with relations
            UserDataSet author = userDBRepo.read(message.getAuthorId());
            message.setAuthor(author.getLogin());
        });

        List<ChatMessage> list = chatMessageDataSets
                .stream()
                .map(ChatMessage.class::cast)
                .collect(Collectors.toList());
        msContext.send(serviceMessageFactory.createSenderOfUserBulkMessages(getAddress(), initializer, user, list));
    }
}
