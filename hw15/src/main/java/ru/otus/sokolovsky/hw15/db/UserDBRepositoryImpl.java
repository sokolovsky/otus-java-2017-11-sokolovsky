package ru.otus.sokolovsky.hw15.db;

import ru.otus.sokolovsky.hw15.domain.UserDBRepository;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDBRepositoryImpl extends AbstractDBRepository<UserDataSet> implements UserDBRepository {

    public UserDBRepositoryImpl(Connection connection) {
        super(connection);
    }

    @Override
    public List<UserDataSet> readByName(String name) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("name", name);
        return getDao().loadByFilter(filter, UserDataSet.class);
    }
}
