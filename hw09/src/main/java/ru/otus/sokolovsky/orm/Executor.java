package ru.otus.sokolovsky.orm;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

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
        EntityDefinition<T> definition = EntityDefinitionsRegister.getDefinition((Class<T>) model.getClass());
        Map<String, String> pairs = definition.getFieldsPairs(model);

        if (model.getId() == 0) {
            long id = insert(definition.tableName(), pairs);
            model.setId(id);
        } else {
            update(definition.tableName(), pairs, model.getId());
        }
    }

    public <T extends DataSet> T load(long id, Class<T> cl) {
        EntityDefinition<T> definition = EntityDefinitionsRegister.getDefinition(cl);
        T model;
        try {
            model = (T) cl.getDeclaredConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        String sql = "SELECT * from %s where id=%d";
        try {
            execSelect(String.format(sql, definition.tableName(), id), (resultSet -> {
                try {
                    int columnCount = resultSet.getMetaData().getColumnCount();
                    Map<String, String> pairs = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        pairs.put(resultSet.getMetaData().getColumnName(i), resultSet.getString(i));
                    }
                    definition.fillOne(model, pairs);
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return model;
    }

    private void update(String table, Map<String, String> pairs, long id) {
        String sql = "UPDATE %s SET %s WHERE id=%d";

        sql = String.format(sql, table, collectSqlSet(pairs), id);

        try {
            execUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private long insert(String table, Map<String, String> pairs) {
        String sql = "INSERT INTO %s SET %s";
        sql = String.format(sql, table, collectSqlSet(pairs));
        try {
            return execInsert(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String collectSqlSet(Map<String, String> pairs) {
        List<String> set = new LinkedList<>();
        pairs.forEach((key, value) -> set.add(key + "=" + value));
        return String.join(", ", set);
    }
}
