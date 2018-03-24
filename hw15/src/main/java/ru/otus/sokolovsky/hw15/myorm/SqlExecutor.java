package ru.otus.sokolovsky.hw15.myorm;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SqlExecutor {

    private Connection connection;

    public SqlExecutor(Connection connection) {
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

    public void execSelect(String sql, Map<String, ValueContainer> filter, RecordSetHandler handler) throws SQLException {
        List<String> columns = new ArrayList<>(filter.keySet());
        sql = String.format(sql, collectSqlSet(columns, " && "));
        List<ValueContainer> values = columns.stream().map(filter::get).collect(Collectors.toList());
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setPreparedValues(statement, values);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                handler.handle(resultSet);
            }
            resultSet.close();
        }
    }

    public void execSelect(String sql, RecordSetHandler handler) throws SQLException {
        execSelect(sql, new HashMap<>(), handler);
    }

    void update(String table, Map<String, ValueContainer> map, long id) {
        String sql = "UPDATE `%s` SET %s where id = ?";

        List<String> columns = new ArrayList<>(map.keySet());
        sql = String.format(sql, table, collectSqlSet(columns, ", "));
        List<ValueContainer> values = columns.stream().map(map::get).collect(Collectors.toList());
        values.add(new ValueContainer(ValueContainer.Type.LONG, id));

        try {
            execUpdate(sql, values);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    long insert(String table, Map<String, ValueContainer> map) {
        String sql = "INSERT INTO %s SET %s";
        List<String> columns = new LinkedList<>(map.keySet());
        sql = String.format(sql, table, collectSqlSet(columns, ","));
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
                case DATE_TIME:
                    statement.setObject(i, container.getValue());
            }
        }
    }

    private String collectSqlSet(List<String> columns, String delimiter) {
        List<String> set = new LinkedList<>();
        columns.forEach((key) -> set.add(key + "= ?" ));
        return String.join(delimiter, set);
    }
}
