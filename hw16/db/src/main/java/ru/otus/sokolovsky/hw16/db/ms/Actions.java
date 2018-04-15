package ru.otus.sokolovsky.hw16.db.ms;

import java.util.Arrays;
import java.util.Optional;

public enum Actions {
    CHECK_USER("check-user"),
    GET_LAST_MESSAGES("get-last-messages"),
    REGISTER_NEW_MESSAGE("register-new-message");

    private String name;

    Actions(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Actions getByName(String name) {
        Optional<Actions> el = Arrays.stream(Actions.values())
                .filter(n -> n.getName().equals(name))
                .findFirst();
        return el.orElse(null);
    }
}
