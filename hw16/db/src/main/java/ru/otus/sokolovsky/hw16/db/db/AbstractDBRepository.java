package ru.otus.sokolovsky.hw16.db.db;

import ru.otus.sokolovsky.hw16.db.domain.DBRepository;
import ru.otus.sokolovsky.hw16.db.domain.DataSet;
import ru.otus.sokolovsky.hw16.db.myorm.DataSetDao;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDBRepository<E extends DataSet> implements DBRepository<E> {

    private final DataSetDao<E> dao;
    private final Connection connection;

    public AbstractDBRepository(Connection connection) {
        this.connection = connection;
        this.dao = new DataSetDao<>(this.connection);
    }

    protected DataSetDao<E> getDao() {
        return dao;
    }

    @Override
    public void save(E dataSet) {
        dao.save(dataSet);
    }

    @Override
    public E read(long id) {
        return dao.load(id, getGenericClass());
    }

    @Override
    public List<E> readAll() {
        Map<String, Object> filter = new HashMap<>();
        filter.put("1", 1);
        return dao.loadByFilter(filter, getGenericClass());
    }

    @Override
    public List<E> readLast(int limit) {
        return dao.loadLatest(limit, getGenericClass());
    }

    @Override
    public void shutdown() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<E> getGenericClass() {
        ParameterizedType parametrizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<E>) parametrizedType.getActualTypeArguments()[0];
    }
}
