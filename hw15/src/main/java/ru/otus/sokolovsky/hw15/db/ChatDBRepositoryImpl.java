package ru.otus.sokolovsky.hw15.db;

import ru.otus.sokolovsky.hw15.domain.ChatDBRepository;

import java.sql.Connection;

public class ChatDBRepositoryImpl extends AbstractDBRepository<ChatMessageDataSet> implements ChatDBRepository {

    public ChatDBRepositoryImpl(Connection connection) {
        super(connection);
    }
}
