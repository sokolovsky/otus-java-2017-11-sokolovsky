package ru.otus.sokolovsky.hw16.db.ms;

import ru.otus.sokolovsky.hw16.db.db.ChatMessageDataSet;
import ru.otus.sokolovsky.hw16.db.db.UserDataSet;
import ru.otus.sokolovsky.hw16.db.domain.ChatDBRepository;
import ru.otus.sokolovsky.hw16.db.domain.UserDBRepository;
import ru.otus.sokolovsky.hw16.integration.message.Message;
import ru.otus.sokolovsky.hw16.integration.message.MessageFactory;
import ru.otus.sokolovsky.hw16.integration.message.ParametrizedMessage;

import java.time.LocalDateTime;

public class RegisterNewMessageHandler extends AbstractHandler {

    private ChatDBRepository chatRepo;
    private UserDBRepository userRepo;

    public RegisterNewMessageHandler(ChatDBRepository chatRepo, UserDBRepository userRepo) {
        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void accept(Message message) {
        ParametrizedMessage pMessage = (ParametrizedMessage) message;

        UserDataSet author = userRepo.readByLogin(pMessage.getParameters().get("login")).get(0);


        ChatMessageDataSet messageDataSet = new ChatMessageDataSet();
        messageDataSet.setAuthor(author);
        messageDataSet.setText(pMessage.getParameters().get("text"));
        messageDataSet.setTime(LocalDateTime.now());

        chatRepo.save(messageDataSet);

        ParametrizedMessage replyMessage = (ParametrizedMessage) MessageFactory.generateReplyMessage(message);
        replyMessage.setParameter("success", "1");
        getSender().accept(replyMessage);
    }
}
