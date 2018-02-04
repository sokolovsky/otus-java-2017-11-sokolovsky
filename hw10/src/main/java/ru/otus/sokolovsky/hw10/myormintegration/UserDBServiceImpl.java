package ru.otus.sokolovsky.hw10.myormintegration;

import ru.otus.sokolovsky.hw10.domain.UserDBService;
import ru.otus.sokolovsky.hw10.domain.UserDataSet;
import ru.otus.sokolovsky.hw10.myorm.DataSetDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDBServiceImpl implements UserDBService {

    private final DataSetDao<UserDataSet> dao;
    private final Connection connection;

    public UserDBServiceImpl(Connection connection) {
        this.dao = new DataSetDao<>(connection);
        this.connection = connection;
    }

    @Override
    public List<UserDataSet> readByName(String name) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("name", name);
        return dao.loadByFilter(filter, UserDataSet.class);
    }

    @Override
    public void save(UserDataSet dataSet) {
        dao.save(dataSet);
    }

    @Override
    public UserDataSet read(long id) {
        return dao.load(id, UserDataSet.class);
    }

    @Override
    public List<UserDataSet> readAll() {
        Map<String, Object> filter = new HashMap<>();
        filter.put("1", 1);
        return dao.loadByFilter(filter, UserDataSet.class);
    }

    @Override
    public void shutdown() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
