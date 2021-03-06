package ru.otus.sokolovsky.hw11.domain;

import java.util.List;

public interface UserDBService extends DBService<UserDataSet> {
    List<UserDataSet> readByName(String name);
}
