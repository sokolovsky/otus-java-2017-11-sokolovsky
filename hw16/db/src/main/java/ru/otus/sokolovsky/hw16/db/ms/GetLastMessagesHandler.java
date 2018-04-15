package ru.otus.sokolovsky.hw16.db.ms;

import ru.otus.sokolovsky.hw16.db.db.ChatMessageDataSet;
import ru.otus.sokolovsky.hw16.db.db.UserDataSet;
import ru.otus.sokolovsky.hw16.db.domain.ChatDBRepository;
import ru.otus.sokolovsky.hw16.db.domain.UserDBRepository;
import ru.otus.sokolovsky.hw16.integration.client.AbstractHandler;
import ru.otus.sokolovsky.hw16.integration.message.Message;
import ru.otus.sokolovsky.hw16.integration.message.MessageFactory;

import java.util.List;

public class GetLastMessagesHandler extends AbstractHandler {
    private ChatDBRepository chatRepo;
    private UserDBRepository userRepo;
    private int limit;

    public GetLastMessagesHandler(ChatDBRepository chatRepo, UserDBRepository userRepo, int limit) {
        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
        this.limit = limit;
    }

    @Override
    public void accept(Message message) {
        List<ChatMessageDataSet> chatMessageDataSets = chatRepo.readLast(limit);
        chatMessageDataSets.forEach((ChatMessageDataSet dbMessage) -> {
            // it's awful but orm is not designed to work with relations
            UserDataSet author = userRepo.read(dbMessage.getAuthorId());
            dbMessage.setAuthor(author.getLogin());
        });
        Message replyMessage = MessageFactory.generateReplyMessage(message);
        getSender().accept(replyMessage);
    }
}
