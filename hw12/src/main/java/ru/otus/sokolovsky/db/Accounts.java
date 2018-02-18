package ru.otus.sokolovsky.db;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Accounts {
    public static final Accounts instance = new Accounts();
    private final Map<String, String> data = new HashMap<>();

    public void add(String user, String password) {
        data.put(user, password);
    }

    public boolean hasPassword(String user, String password) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(password);
        String pass = data.get(user);
        return password.equals(pass);
    }

    public boolean hasUser(String user) {
        return data.containsKey(user);
    }
}
