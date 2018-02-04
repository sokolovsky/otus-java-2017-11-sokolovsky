package ru.otus.sokolovsky.hw10.myorm;

import java.sql.ResultSet;

public interface RecordSetHandler {
    void handle(ResultSet resultSet);
}
