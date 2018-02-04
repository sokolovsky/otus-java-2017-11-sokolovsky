package ru.otus.sokolovsky.hw10.myormintegration;

import ru.otus.sokolovsky.hw10.domain.UserDBService;
import ru.otus.sokolovsky.hw10.domain.UserDataSet;

import java.util.List;

public class UserDBServiceImpl implements UserDBService {
    @Override
    public List<UserDataSet> readByName(String name) {
        return null;
    }

    @Override
    public void save(UserDataSet dataSet) {

    }

    @Override
    public UserDataSet read(long id) {
        return null;
    }

    @Override
    public List<UserDataSet> readAll() {
        return null;
    }

    @Override
    public void shutdown() {

    }
}
