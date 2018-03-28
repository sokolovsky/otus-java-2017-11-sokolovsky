package ru.otus.sokolovsky.hw15.myorm;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class EntityDefinition<T> {
    private final String tableName;
    private Map<String, Field> columns = new HashMap<>();
    private Set<Field> primaries = new HashSet<>();
    private Class<T> cls;

    EntityDefinition(Class<T> cls) {
        this.cls = cls;
        Table entityAnnotation = Objects.requireNonNull(cls.getAnnotation(Table.class));

        tableName = entityAnnotation.name();

        Class iClass = cls;
        while (iClass != Object.class) {
            for (Field field : iClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    String name = field.getName();
                    Column annotation = field.getAnnotation(Column.class);
                    if (!annotation.name().equals("")) {
                        name = annotation.name();
                    }
                    columns.put(name, field);
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
        if (valueClass == LocalDateTime.class) {
            container = new ValueContainer(ValueContainer.Type.DATE_TIME, value);
        }
        return Objects.requireNonNull(container);
    }

    public Map<String, ValueContainer> getFieldsValues(T model) {
        Map<String, ValueContainer> res = new HashMap<>();
        for (Map.Entry<String, Field> entry : columns.entrySet()) {
            Field field = entry.getValue();
            field.setAccessible(true);

            ValueContainer container;
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

    @SuppressWarnings("unchecked")
    public void fillOne(T model, ResultSet resultSet) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = resultSet.getMetaData().getColumnName(i);
            if (!columns.containsKey(columnName)) {
                continue;
            }

            Field field = columns.get(columnName);
            field.setAccessible(true);

            Object value = null;
            Class<?> fieldType = field.getType();

            if (fieldType == Long.class || fieldType == long.class) {
                value = resultSet.getLong(i);
            }
            if (fieldType == Integer.class || fieldType == int.class) {
                value = resultSet.getInt(i);
            }
            if (fieldType == Double.class || fieldType == double.class) {
                value = resultSet.getDouble(i);
            }
            if (fieldType == Float.class || fieldType == float.class) {
                value = resultSet.getFloat(i);
            }
            if (fieldType == String.class) {
                value = resultSet.getString(i);
            }
            if (fieldType == LocalDateTime.class) {
                LocalDate date = resultSet.getDate(i).toLocalDate();
                Time time = resultSet.getTime(i);
                LocalTime localTime = LocalTime.of(time.getHours(), time.getMinutes(), time.getSeconds());
                value = LocalDateTime.of(date, localTime);
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
        Optional<Field> optionalField = primaries.stream().findFirst();
        if (!optionalField.isPresent()) {
            throw new RuntimeException("Model doesn't have pk");
        }
        Field field = optionalField.get();
        field.setAccessible(true);
        try {
            return (long) field.get(model);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPkValue(Object model, long pk) {
        Optional<Field> optionalField = primaries.stream().findFirst();
        if (!optionalField.isPresent()) {
            throw new RuntimeException("Model doesn't have pk");
        }
        Field field = optionalField.get();
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

    public Class<T> getDefinedClass() {
        return cls;
    }
}
