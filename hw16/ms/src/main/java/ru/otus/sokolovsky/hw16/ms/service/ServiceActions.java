package ru.otus.sokolovsky.hw16.ms.service;

public enum ServiceActions {
    CREATE_NEW_NAMED_CHANNEL("create-new-named-channel"),
    SUBSCRIBE_ON_CHANNEL("subscribe-on-channel");

    private String name;

    ServiceActions(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ServiceActions getByName(String name) {
        return ServiceActions.valueOf(name);
    }
}
