package ru.otus.sokolovsky.hw11.myorm;

import java.sql.ResultSet;

public interface RecordSetHandler {
    void handle(ResultSet resultSet);
}
