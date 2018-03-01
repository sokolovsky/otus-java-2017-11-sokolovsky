package ru.otus.sokolovsky.hw13.myorm;

import java.sql.ResultSet;

public interface RecordSetHandler {
    void handle(ResultSet resultSet);
}
