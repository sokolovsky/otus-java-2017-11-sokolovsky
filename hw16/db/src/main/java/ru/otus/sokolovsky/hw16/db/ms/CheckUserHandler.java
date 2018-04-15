package ru.otus.sokolovsky.hw16.db.ms;

import ru.otus.sokolovsky.hw16.db.domain.UserDBRepository;
import ru.otus.sokolovsky.hw16.integration.message.Message;
import ru.otus.sokolovsky.hw16.integration.message.MessageFactory;
import ru.otus.sokolovsky.hw16.integration.message.ParametrizedMessage;

import java.util.Map;

public class CheckUserHandler extends AbstractHandler {
    private UserDBRepository userRepo;

    public CheckUserHandler(UserDBRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void accept(Message message) {
        ParametrizedMessage pMessage = (ParametrizedMessage) message;
        Map<String, String> parameters = pMessage.getParameters();
        String login = parameters.get("login");
        String password = parameters.get("password");

        boolean result = userRepo.hasPassword(login, password);
        ParametrizedMessage replyMessage = (ParametrizedMessage) MessageFactory.generateReplyMessage(message);
        replyMessage.setParameter("exist", result ? "1" : "0");
        replyMessage.setParameter("success", "1");
        getSender().accept(replyMessage);
    }
}
