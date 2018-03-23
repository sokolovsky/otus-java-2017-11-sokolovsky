package ru.otus.sokolovsky.hw15.domain;

import ru.otus.sokolovsky.hw15.db.UserDataSet;

import java.util.List;

public interface UserDBRepository extends DBRepository<UserDataSet> {
    List<UserDataSet> readByLogin(String login);

    boolean hasPassword(String login, String pass);
}
