package ru.otus.sokolovsky.domain;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import ru.otus.sokolovsky.App;
import ru.otus.sokolovsky.orm.Executor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class DataSetTest {
    private List<Long> userIds = new LinkedList<>();

    private App app() {
        return App.getApplication();
    }

    @org.junit.Before
    public void setUp() throws Exception {
        app().createUsesTable();

        long id;
        id = app().createExecutor().execInsert("INSERT INTO users SET name=\"Иван\", age=18");
        userIds.add(id);

        id = app().createExecutor().execInsert("INSERT INTO users SET name=\"Петр\", age=19");
        userIds.add(id);

        id = app().createExecutor().execInsert("INSERT INTO users SET name=\"Николай\", age=20");
        userIds.add(id);
    }

    @org.junit.After
    public void tearDown() throws Exception {
        app().dropUsersTable();
    }

    @Test
    public void idle() {
    }

    @Test
    public void loadUser() throws SQLException {
        Executor executor = app().createExecutor();
        UserDataSet user = executor.load(userIds.get(0), UserDataSet.class);

        assertThat(user.getName(), is("name"));
        assertThat(user.getAge(), is(18));
    }

    @Test
    public void changeUser() throws SQLException {
        Executor executor = app().createExecutor();
        UserDataSet user = executor.load(userIds.get(2), UserDataSet.class);

        user.setAge(11);
        user.setName("Иннокентий");

        executor.save(user);

        executor.execSelect("select * from users where id="+user.getId(),
                (ResultSet resultSet) -> {
                    try {
                        assertThat(resultSet.getString("name"), is("Иннокентий"));
                        assertThat(resultSet.getLong("age"), is(11));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            );
    }

    public void createNewUser() throws SQLException {
        UserDataSet user = new UserDataSet();
        user.setName("Новый пользователь");
        user.setAge(20);

        Executor executor = app().createExecutor();
        executor.save(user);

        assertThat(user.getId(), not(anyOf(nullValue(), is(0))));
    }
}