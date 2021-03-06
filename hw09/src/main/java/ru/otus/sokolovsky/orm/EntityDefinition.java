package ru.otus.sokolovsky.orm;

import ru.otus.sokolovsky.orm.annotations.Column;
import ru.otus.sokolovsky.orm.annotations.Entity;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class EntityDefinition<T> {
    private final String tableName;
    private Map<String, Field> columns = new HashMap<>();
    private Set<Field> primaries = new HashSet<>();

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
                if (field.getAnnotation(Column.class).isPrimary()) {
                    primaries.add(field);
                }
            }
            iClass = iClass.getSuperclass();
        }
    }

    public Map<String, ValueContainer> getFieldsValues(T model) {
        Map<String, ValueContainer> res = new HashMap<>();
        for (Map.Entry<String, Field> entry : columns.entrySet()) {
            Field field = entry.getValue();
            field.setAccessible(true);

            ValueContainer container = null;
            try {
                Object value = field.get(model);
                Class<?> valueClass = value.getClass();
                if (valueClass == Integer.class) {
                    container = new ValueContainer(ValueContainer.Type.INT, value);
                }
                if (valueClass == Long.class) {
                    container = new ValueContainer(ValueContainer.Type.LONG, value);
                }
                if (valueClass == Boolean.class) {
                    container = new ValueContainer(ValueContainer.Type.BOOL, value);
                }
                if (valueClass == Double.class) {
                    container = new ValueContainer(ValueContainer.Type.DOUBLE, value);
                }
                if (valueClass == Float.class) {
                    container = new ValueContainer(ValueContainer.Type.FLOAT, value);
                }
                if (valueClass == String.class) {
                    container = new ValueContainer(ValueContainer.Type.STRING, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }

            if (container == null) {
                throw new RuntimeException(String.format("Type of column `%s` is not compatible", field.getName()));
            }
            res.put(entry.getKey(), container);
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

    public Set<String> getPrimaries() {
        return primaries.stream().map(Field::getName).collect(Collectors.toSet());
    }

    public boolean isPrimary(String field) {
        for (Field primary : primaries) {
            if (primary.getName().equals(field)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPrimary(Field field) {
        return primaries.contains(field);
    }

    public String tableName() {
        return tableName;
    }
}
