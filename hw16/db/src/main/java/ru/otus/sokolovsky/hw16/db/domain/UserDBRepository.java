package ru.otus.sokolovsky.hw16.db.domain;

import ru.otus.sokolovsky.hw16.db.db.UserDataSet;

import java.util.List;

public interface UserDBRepository extends DBRepository<UserDataSet> {
    List<UserDataSet> readByLogin(String login);

    boolean hasPassword(String login, String pass);
}
