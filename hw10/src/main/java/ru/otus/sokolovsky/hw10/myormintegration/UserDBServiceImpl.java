package ru.otus.sokolovsky.hw10.myormintegration;

import ru.otus.sokolovsky.hw10.domain.UserDBService;
import ru.otus.sokolovsky.hw10.domain.UserDataSet;
import ru.otus.sokolovsky.hw10.myorm.DataSetDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDBServiceImpl implements UserDBService {

    private final DataSetDao<UserDataSet> dao;

    public UserDBServiceImpl(DataSetDao<UserDataSet> dao) {
        this.dao = dao;
    }

    @Override
    public List<UserDataSet> readByName(String name) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("name", name);
        return dao.loadByFields(filter, UserDataSet.class);
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
        return null;
    }

    @Override
    public void shutdown() {
    }
}
