package ru.otus.sokolovsky.domain;

import org.junit.Test;
import ru.otus.sokolovsky.App;

import static org.junit.Assert.*;

public class DataSetTest {

    private App app() {
        return App.getApplication();
    }

    @org.junit.Before
    public void setUp() throws Exception {
        app().createUsesTable();

        app().createExecutor().execUpdate("INSERT INTO users SET name=\"Иван\", age=18");
        app().createExecutor().execUpdate("INSERT INTO users SET name=\"Петр\", age=19");
        app().createExecutor().execUpdate("INSERT INTO users SET name=\"Николай\", age=20");
    }

    @org.junit.After
    public void tearDown() throws Exception {
        app().dropUsersTable();
    }

    @Test
    public void idle() {
        String a = "10";
    }
}