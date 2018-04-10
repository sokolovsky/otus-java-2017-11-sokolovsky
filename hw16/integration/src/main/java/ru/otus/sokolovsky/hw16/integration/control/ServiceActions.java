package ru.otus.sokolovsky.hw16.integration.control;

import java.util.Arrays;
import java.util.Optional;

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
        Optional<ServiceActions> el = Arrays.stream(ServiceActions.values())
                .filter(n -> n.getName().equals(name))
                .findFirst();
        return el.orElse(null);
    }
}
