package ru.otus.sokolovsky.orm;

import ru.otus.sokolovsky.orm.annotations.Column;
import ru.otus.sokolovsky.orm.annotations.Entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EntityDefinition<T> {
    private final String tableName;
    private Map<String, Field> columns = new HashMap<>();

    public EntityDefinition(Class<T> cls) {
        Entity entityAnnotation = cls.getAnnotation(Entity.class);
        Objects.requireNonNull(entityAnnotation);

        tableName = entityAnnotation.tableName();

        Class iClass = cls;
        while (iClass != Object.class) {
            for (Field field : iClass.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }
                columns.put(field.getName(), field);
            }
            iClass = iClass.getSuperclass();
        }
    }

    public Map<String, String> getFieldsPairs(T model) {
        Map<String, String> res = new HashMap<>();
        for (Map.Entry<String, Field> entry : columns.entrySet()) {
            Field field = entry.getValue();
            field.setAccessible(true);

            Object value = null;
            try {
                value = field.get(model);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (value == null) {
                value = "";
            }
            if (value.getClass() == String.class) {
                value = "'" + value + "'";
            }
            res.put(entry.getKey(), value.toString());
        }
        return res;
    }

    public void fillOne(T model, Map<String, String> pairs) {
        for (Map.Entry<String, String> entry : pairs.entrySet()) {
            if (!columns.containsKey(entry.getKey())) {
                continue;
            }

            Field field = columns.get(entry.getKey());
            field.setAccessible(true);

            Object value = null;
            String strValue = entry.getValue();
            Class<?> fieldType = field.getType();

            if (fieldType == Long.class || fieldType == long.class) {
                value = Long.parseLong(strValue);
            }
            if (fieldType == Integer.class || fieldType == int.class) {
                value = Integer.parseInt(strValue);
            }
            if (fieldType == Double.class || fieldType == double.class) {
                value = Double.parseDouble(strValue);
            }
            if (fieldType == Float.class || fieldType == float.class) {
                value = Float.parseFloat(strValue);
            }
            if (fieldType == String.class) {
                value = strValue;
            }
            if (value == null) {
                continue;
            }
            try {
                field.set(model, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public String tableName() {
        return tableName;
    }
}
