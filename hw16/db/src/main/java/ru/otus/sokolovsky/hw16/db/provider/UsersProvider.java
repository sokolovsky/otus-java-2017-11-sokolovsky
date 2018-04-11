package ru.otus.sokolovsky.hw16.db.provider;

import java.util.*;
import java.util.stream.Collectors;

public class UsersProvider {
    private final Map<String, String> users = new HashMap<>();

    public UsersProvider(String str) {
        String users = str;
        String[] usersArray = users.split(",\\s*");
        Arrays.stream(usersArray)
                .map(s -> s
                        .replaceAll("]", "")
                        .replaceAll("\\[", ""))
                .forEach(s -> {
                    List<String> user = Arrays.stream(s.split("\\|")).map(String::trim).collect(Collectors.toList());
                    this.users.put(user.get(0), user.get(1));
                });
    }

    public Map<String, String> getRecords() {
        return Collections.unmodifiableMap(users);
    }
}
