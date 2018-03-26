package ru.otus.sokolovsky.hw15.myorm;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
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

    private T newModel(Class<T> tClass) {
        T model;
        try {
            model = (T) tClass.getDeclaredConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return model;
    }

    private RecordSetHandler hidrate(EntityDefinition<T> definition, List<T> aggregator) {
        Class<T> cl = definition.getDefinedClass();
        return (ResultSet resultSet) -> {
            try {
                T model = newModel(cl);
                definition.fillOne(model, resultSet);
                aggregator.add(model);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        };

    }

    @SuppressWarnings("unchecked")
    public List<T> loadByFilter(Map<String, Object> values, Class<T> cl) {
        EntityDefinition<T> definition = EntityDefinitionsRegister.getDefinition(cl);

        String sql = "SELECT * from %s";
        Map<String, ValueContainer> filter = new HashMap<>();
        values.forEach((k, v) -> filter.put(k, definition.createContainerByValue(v)));
        List<T> result = new LinkedList<>();

        try {
            executor.execSelect(
                    String.format(sql, definition.tableName()) + " where %s",
                    filter,
                    this.hidrate(definition, result)
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<T> loadLatest(int limit, Class<T> cl) {
        EntityDefinition<T> definition = EntityDefinitionsRegister.getDefinition(cl);

        String sql = "SELECT * from %s order by id desc limit %d";
        Map<String, ValueContainer> filter = new HashMap<>();
        List<T> result = new LinkedList<>();

        try {
            executor.execSelect(
                    String.format(sql, definition.tableName(), limit),
                    filter,
                    this.hidrate(definition, result)
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
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
