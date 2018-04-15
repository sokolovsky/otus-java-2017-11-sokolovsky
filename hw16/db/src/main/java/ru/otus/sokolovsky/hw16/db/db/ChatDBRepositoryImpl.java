package ru.otus.sokolovsky.hw16.db.db;

import ru.otus.sokolovsky.hw16.db.domain.ChatDBRepository;

import java.sql.Connection;

public class ChatDBRepositoryImpl extends AbstractDBRepository<ChatMessageDataSet> implements ChatDBRepository {

    public ChatDBRepositoryImpl(Connection connection) {
        super(connection);
    }
}
