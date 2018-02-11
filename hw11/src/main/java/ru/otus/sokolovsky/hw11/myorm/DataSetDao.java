package ru.otus.sokolovsky.hw11.myorm;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DataSetDao<T> {

    private SqlExecutor executor;

    public DataSetDao(Connection connection) {
        executor = new SqlExecutor(connection);
    }

    public T load(long id, Class<T> cl) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("id", id);
        return loadByFilter(filter, cl).get(0);
    }

    public List<T> loadByFilter(Map<String, Object> values, Class<T> cl) {
        EntityDefinition<T> definition = EntityDefinitionsRegister.getDefinition(cl);
        Supplier<T> createEmptyModel = () -> {
            T model;
            try {
                model = (T) cl.getDeclaredConstructors()[0].newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return model;
        };

        String sql = "SELECT * from %s";
        Map<String, ValueContainer> filter = new HashMap<>();
        values.forEach((k, v) -> filter.put(k, definition.createContainerByValue(v)));
        List<T> result = new LinkedList<>();

        try {
            executor.execSelect(String.format(sql, definition.tableName()) +  " where %s", filter, (resultSet -> {
                try {
                    int columnCount = resultSet.getMetaData().getColumnCount();
                    Map<String, String> pairs = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        pairs.put(resultSet.getMetaData().getColumnName(i), resultSet.getString(i));
                    }
                    T model = createEmptyModel.get();
                    definition.fillOne(model, pairs);
                    result.add(model);
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    public void save(T model) {
        EntityDefinition<T> definition = EntityDefinitionsRegister.getDefinition(model.getClass());
        Map<String, ValueContainer> values = definition.getFieldsValues(model);
        for (String primary : definition.getPrimaries()) {
            values.remove(primary);
        }

        if (definition.getPkValue(model) == 0) {
            long pk = executor.insert(definition.tableName(), values);
            definition.setPkValue(model, pk);
        } else {
            executor.update(definition.tableName(), values, definition.getPkValue(model));
        }
    }
}
