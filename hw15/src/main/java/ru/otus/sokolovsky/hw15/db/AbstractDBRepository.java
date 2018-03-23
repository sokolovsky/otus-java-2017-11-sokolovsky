package ru.otus.sokolovsky.hw15.db;

import ru.otus.sokolovsky.hw15.domain.DBRepository;
import ru.otus.sokolovsky.hw15.domain.DataSet;
import ru.otus.sokolovsky.hw15.myorm.DataSetDao;

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
    public void shutdown() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<E> getGenericClass() {
        return (Class<E>) getClass().getGenericSuperclass();
    }
}
