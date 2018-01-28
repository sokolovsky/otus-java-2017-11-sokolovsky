package ru.otus.sokolovsky.orm;

import java.sql.Connection;
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

    public <T extends DataSet> void save(T model) {
    }

    public <T extends DataSet> void load(long id, Class<T> cl) {
    }
}
