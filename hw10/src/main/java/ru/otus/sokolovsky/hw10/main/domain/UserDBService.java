package ru.otus.sokolovsky.hw10.main.domain;

import java.util.List;

public interface UserDBService extends DBService<UserDataSet> {
    List<UserDataSet> readByName(String name);
}
