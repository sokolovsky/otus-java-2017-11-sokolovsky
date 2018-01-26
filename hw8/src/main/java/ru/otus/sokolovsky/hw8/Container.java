package ru.otus.sokolovsky.hw8;

import java.util.List;
import java.util.Map;

class Container {

    enum Type {
        OBJECT, ARRAY, NUMBER, NULL, STRING, BOOLEAN;
    }

    private final Type type;
    private final Object data;

    Container(Type type, Object data) {
        this.type = type;
        this.data = data;
    }

    Type getType() {
        return type;
    }

    Map<String, Container> getMap() {
        return (Map<String, Container>) data;
    }

    List<Container> getList() {
        return (List<Container>) data;
    }

    Object getValue() {
        return data;
    }
}
