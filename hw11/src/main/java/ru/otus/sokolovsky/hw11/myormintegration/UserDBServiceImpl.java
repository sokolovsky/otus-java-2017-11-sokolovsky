package ru.otus.sokolovsky.hw11.myormintegration;

import ru.otus.sokolovsky.hw11.cache.Cache;
import ru.otus.sokolovsky.hw11.domain.UserDBService;
import ru.otus.sokolovsky.hw11.domain.UserDataSet;
import ru.otus.sokolovsky.hw11.myorm.DataSetDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDBServiceImpl implements UserDBService {

    private final DataSetDao<UserDataSet> dao;
    private final Connection connection;
    private Cache<Long, UserDataSet> cache = new HoleyCache<>();

    public UserDBServiceImpl(Connection connection) {
        this.connection = connection;
        this.dao = new DataSetDao<>(this.connection);
    }

    @Override
    public List<UserDataSet> readByName(String name) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("name", name);
        List<UserDataSet> list = dao.loadByFilter(filter, UserDataSet.class);
        for (UserDataSet userDataSet : list) {
            cache.put(userDataSet.getId(), userDataSet);
        }
        return list;
    }

    @Override
    public void save(UserDataSet dataSet) {
        dao.save(dataSet);
        cache.put(dataSet.getId(), dataSet);
    }

    @Override
    public UserDataSet read(long id) {
        if (cache.isPresent(id)) {
            return cache.get(id);
        }
        UserDataSet dataSet = dao.load(id, UserDataSet.class);
        cache.put(id, dataSet);
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

    @Override
    public void setCache(Cache<Long, UserDataSet> cache) {
        this.cache = cache;
    }
}
