package ru.otus.sokolovsky.orm;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Executor {

    private Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public int execUpdate(String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            return statement.getUpdateCount();
        }
    }

    public long execInsert(String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return (generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating failed, no id obtained.");
                }
            }

        }
    }


    public void execSelect(String sql, RecordSetHandler handler) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                handler.handle(resultSet);
            }
        }
    }

    public <T extends DataSet> void save(T model) {
    }

    public <T extends DataSet> T load(long id, Class<T> cl) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return (T) cl.getConstructors()[0].newInstance();
    }
}
