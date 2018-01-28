package ru.otus.sokolovsky.orm;

import java.sql.ResultSet;

public interface RecordSetHandler {
    void handle(ResultSet resultSet);
}
