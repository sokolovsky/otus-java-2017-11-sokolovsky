package ru.otus.sokolovsky.hw16.db.domain;

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
