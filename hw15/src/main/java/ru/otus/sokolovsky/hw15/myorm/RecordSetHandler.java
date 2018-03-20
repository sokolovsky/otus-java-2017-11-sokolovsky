package ru.otus.sokolovsky.hw15.myorm;

import java.sql.ResultSet;

public interface RecordSetHandler {
    void handle(ResultSet resultSet);
}
