package ru.otus.sokolovsky.hw13.domain;

import java.util.List;

public interface UserDBService extends DBService<UserDataSet> {
    List<UserDataSet> readByName(String name);
}
