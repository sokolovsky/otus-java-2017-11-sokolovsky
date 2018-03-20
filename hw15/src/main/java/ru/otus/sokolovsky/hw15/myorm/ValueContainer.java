package ru.otus.sokolovsky.hw15.myorm;

public class ValueContainer {
    enum Type {
        INT, LONG, FLOAT, DOUBLE, STRING, BOOL
    }

    private Type type;
    private Object value;

    ValueContainer(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
