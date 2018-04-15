package ru.otus.sokolovsky.hw16.db.provider;

import ru.otus.sokolovsky.hw16.db.myorm.RecordSetHandler;
import ru.otus.sokolovsky.hw16.db.myorm.SqlExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class UsersProvider {

    private final Map<String, String> users = new HashMap<>();
    private SqlExecutor executor;

    public UsersProvider(String str, SqlExecutor executor) {
        this.executor = executor;
        String[] usersArray = str.split(",\\s*");
        Arrays.stream(usersArray)
                .map(s -> s
                        .replaceAll("]", "")
                        .replaceAll("\\[", ""))
                .forEach(s -> {
                    List<String> user = Arrays.stream(s.split("\\|")).map(String::trim).collect(Collectors.toList());
                    users.put(user.get(0), user.get(1));
                });
    }

    public Map<String, String> getRecords() {
        return Collections.unmodifiableMap(users);
    }

    public void registerIntoDatabase() {
        users.forEach((login, hash) -> {
            try {
                ExistExecutorHandler existHandler = new ExistExecutorHandler();
                executor.execSelect(String.format("SELECT id FROM user WHERE login=\"%s\"", login), existHandler);

                if (!existHandler.has()) {
                    executor.execInsert(String.format("INSERT INTO user SET login=\"%s\", age=20, password=\"%s\"", login, hash));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    private class ExistExecutorHandler implements RecordSetHandler {
        private boolean has = false;

        public boolean has() {
            return has;
        }

        @Override
        public void handle(ResultSet resultSet) {
            has = true;
        }
    }
}
