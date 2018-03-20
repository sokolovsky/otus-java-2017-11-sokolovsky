package ru.otus.sokolovsky.hw15.myormintegration;

import ru.otus.sokolovsky.hw15.domain.UserDBService;
import ru.otus.sokolovsky.hw15.domain.UserDataSet;
import ru.otus.sokolovsky.hw15.myorm.DataSetDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDBServiceImpl implements UserDBService {

    private final DataSetDao<UserDataSet> dao;
    private final Connection connection;

    public UserDBServiceImpl(Connection connection) {
        this.connection = connection;
        this.dao = new DataSetDao<>(this.connection);
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
        UserDataSet dataSet = dao.load(id, UserDataSet.class);
        return dataSet;
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
