package ru.otus.sokolovsky.hw15.domain;

public enum AddressTypes {

    DB("DB"), FRONT("FRONTEND");

    private String name;

    AddressTypes(String typeName) {
        name = typeName;
    }

    public String getName() {
        return name;
    }
}
