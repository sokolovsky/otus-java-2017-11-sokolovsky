package ru.otus.sokolovsky.hw13.myorm;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class EntityDefinition<T> {
    private final String tableName;
    private Map<String, Field> columns = new HashMap<>();
    private Set<Field> primaries = new HashSet<>();

    public EntityDefinition(Class<T> cls) {
        Table entityAnnotation = Objects.requireNonNull(cls.getAnnotation(Table.class));

        tableName = entityAnnotation.name();

        Class iClass = cls;
        while (iClass != Object.class) {
            for (Field field : iClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    columns.put(field.getName(), field);
                }
                if (field.isAnnotationPresent(Id.class)) {
                    columns.put(field.getName(), field);
                    primaries.add(field);
                }
            }
            iClass = iClass.getSuperclass();
        }
    }

    ValueContainer createContainerByValue(Object value) {
        ValueContainer container = null;
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
        return Objects.requireNonNull(container);
    }

    public Map<String, ValueContainer> getFieldsValues(T model) {
        Map<String, ValueContainer> res = new HashMap<>();
        for (Map.Entry<String, Field> entry : columns.entrySet()) {
            Field field = entry.getValue();
            field.setAccessible(true);

            ValueContainer container = null;
            try {
                container = createContainerByValue(field.get(model));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }

            Objects.requireNonNull(container, String.format("Type of column `%s` is not compatible", field.getName()));
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

    public long getPkValue(Object model) {
        Field field = primaries.stream().findFirst().get();
        field.setAccessible(true);
        try {
            return (long) field.get(model);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPkValue(Object model, long pk) {
        Field field = primaries.stream().findFirst().get();
        field.setAccessible(true);
        try {
            field.set(model, pk);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> getPrimaries() {
        return primaries.stream().map(Field::getName).collect(Collectors.toSet());
    }

    public boolean isPrimary(String field) {
        return primaries.stream().anyMatch(f -> field.equals(f.getName()));
    }

    private boolean isPrimary(Field field) {
        return primaries.contains(field);
    }

    public String tableName() {
        return tableName;
    }
}
