package ru.otus.sokolovsky.hw16.db.myorm;

import java.sql.ResultSet;

public interface RecordSetHandler {
    void handle(ResultSet resultSet);
}
