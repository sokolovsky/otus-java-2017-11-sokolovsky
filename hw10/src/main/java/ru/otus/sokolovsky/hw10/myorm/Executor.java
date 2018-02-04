package ru.otus.sokolovsky.hw10.myorm;

import ru.otus.sokolovsky.hw10.main.domain.DataSet;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Executor {

    private Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public int execUpdate(String sql, List<ValueContainer> values) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setPreparedValues(statement, values);
            statement.executeUpdate();
            return statement.getUpdateCount();
        }
    }

    public int execUpdate(String sql) throws SQLException {
        return execUpdate(sql, new ArrayList<>());
    }

    public long execInsert(String sql, List<ValueContainer> values) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setPreparedValues(statement, values);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return (generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating failed, no id obtained.");
                }
            }
        }
    }

    public long execInsert(String sql) throws SQLException {
        return execInsert(sql, new ArrayList<>());
    }

    public void execSelect(String sql, List<ValueContainer> values, RecordSetHandler handler) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setPreparedValues(statement, values);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                handler.handle(resultSet);
            }
        }
    }

    public void execSelect(String sql, RecordSetHandler handler) throws SQLException {
        execSelect(sql, new ArrayList<>(), handler);
    }

    public <T extends DataSet> void save(T model) {
        EntityDefinition<T> definition = EntityDefinitionsRegister.getDefinition((Class<T>) model.getClass());
        Map<String, ValueContainer> values = definition.getFieldsValues(model);
        for (String primary : definition.getPrimaries()) {
            values.remove(primary);
        }

        if (model.getId() == 0) {
            long id = insert(definition.tableName(), values);
            model.setId(id);
        } else {
            update(definition.tableName(), values, model.getId());
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
        String sql = "SELECT * from %s where id = ? ";
        try {
            LinkedList<ValueContainer> values = new LinkedList<>();
            values.add(new ValueContainer(ValueContainer.Type.LONG, id));

            execSelect(String.format(sql, definition.tableName()), values,(resultSet -> {
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

    private void update(String table, Map<String, ValueContainer> map, long id) {
        String sql = "UPDATE `%s` SET %s where id = ?";

        List<String> columns = new ArrayList<>(map.keySet());
        sql = String.format(sql, table, collectSqlSet(columns));
        List<ValueContainer> values = columns.stream().map(map::get).collect(Collectors.toList());
        values.add(new ValueContainer(ValueContainer.Type.LONG, id));

        try {
            execUpdate(sql, values);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private long insert(String table, Map<String, ValueContainer> map) {
        String sql = "INSERT INTO %s SET %s";
        List<String> columns = new LinkedList<>(map.keySet());
        sql = String.format(sql, table, collectSqlSet(columns));
        List<ValueContainer> values = columns.stream().map(map::get).collect(Collectors.toList());

        try {
            return execInsert(sql, values);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private void setPreparedValues(PreparedStatement statement, List<ValueContainer> values) throws SQLException {
        for (int i = 1; i <= values.size(); i++) {
            ValueContainer container = values.get(i - 1);
            switch (container.getType()) {
                case LONG:
                    statement.setLong(i, (long) container.getValue());
                    break;
                case INT:
                    statement.setInt(i, (int) container.getValue());
                    break;
                case BOOL:
                    statement.setBoolean(i, (boolean) container.getValue());
                    break;
                case FLOAT:
                    statement.setFloat(i, (float) container.getValue());
                    break;
                case DOUBLE:
                    statement.setDouble(i, (double) container.getValue());
                    break;
                case STRING:
                    statement.setString(i, (String) container.getValue());
            }
        }
    }

    private String collectSqlSet(List<String> columns) {
        List<String> set = new LinkedList<>();
        columns.forEach((key) -> set.add(key + "= ?" ));
        return String.join(", ", set);
    }
}
