package ru.otus.sokolovsky.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.sokolovsky.hw10.domain.UserDBService;
import ru.otus.sokolovsky.hw10.domain.UserDataSet;
import ru.otus.sokolovsky.hw10.main.App;
import ru.otus.sokolovsky.hw10.myorm.DataSetDao;
import ru.otus.sokolovsky.hw10.myorm.SqlExecutor;
import ru.otus.sokolovsky.hw10.myormintegration.UserDBServiceImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

class ImplementationsTest {
    private List<Long> userIds = new LinkedList<>();

    private static App app() {
        return App.getApplication();
    }

    @BeforeEach
    void setUp() throws Exception {
        app().createDbTables();
        createUsersExamples();
    }

    private void createUsersExamples() throws SQLException {
        String[] sqlStrings = {
            "INSERT INTO users SET name=\"Иван\", age=18",
            "INSERT INTO users SET name=\"Петр\", age=19",
            "INSERT INTO users SET name=\"Николай\", age=20"
        };
        Arrays.stream(sqlStrings).forEach(s -> {
            long id = 0;
            try {
                id = app().createExecutor().execInsert(s);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            userIds.add(id);
        });
    }

    @AfterEach
    void tearDown() throws Exception {
        app().dropDbTables();
    }

    static Stream<UserDBService> getAllOrmImplementations() throws SQLException {
        return Stream.of(new UserDBServiceImpl(new DataSetDao(app().getConnection())));
    }

    @ParameterizedTest
    @MethodSource("getAllOrmImplementations")
    void loadUser(UserDBService userDBService) throws SQLException {
        UserDataSet user = userDBService.read(userIds.get(0));

        assertThat(user.getName(), is("Иван"));
        assertThat(user.getAge(), is(18));
        assertThat(user.getId(), not(0));
    }

    @ParameterizedTest
    @MethodSource("getAllOrmImplementations")
    void changeUser(UserDBService userDBService) throws SQLException {
        UserDataSet user = userDBService.read(userIds.get(2));

        user.setAge(11);
        user.setName("Иннокентий");

        userDBService.save(user);

        SqlExecutor executor = app().createExecutor();
        executor.execSelect("select * from users where id="+user.getId(),
                (ResultSet resultSet) -> {
                    try {
                        assertThat(resultSet.getString("name"), is("Иннокентий"));
                        assertThat(resultSet.getInt("age"), is(11));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            );
    }

    @ParameterizedTest
    @MethodSource("getAllOrmImplementations")
    void createNewUser(UserDBService userDBService) throws SQLException {
        UserDataSet user = new UserDataSet();
        user.setName("Новый пользователь");
        user.setAge(20);

        userDBService.save(user);

        assertThat(user.getId(), not(anyOf(nullValue(), is(0L))));
    }

    @ParameterizedTest
    @MethodSource("getAllOrmImplementations")
    void checkSqlInjection(UserDBService userDBService) throws SQLException {
        Long firstId = userIds.get(0);
        UserDataSet user = userDBService.read(firstId);

        user.setName(String.format("Анатолий' where id=%d;\n select * from users where name='", firstId));
        userDBService.save(user);

        UserDataSet restoredUser = userDBService.read(firstId);

        assertThat(user, not(restoredUser));
        assertThat(restoredUser.getName(), is("Анатолий' where id=1;\n select * from users where name='"));
    }

    @ParameterizedTest
    @MethodSource("getAllOrmImplementations")
    void shouldReadAllRecordsInDb(UserDBService userDBService) {
        assertThat(userDBService.readAll().size(), is(3));
    }

    @ParameterizedTest
    @MethodSource("getAllOrmImplementations")
    void shouldSearchUserByName(UserDBService userDBService) {
        UserDataSet ivan = userDBService.readByName("Иван").get(0);
        assertThat(ivan.getAge(), is(18));
    }

    @Test
    @Disabled
    void gettingDataSetWithOneToOneRelations() {
    }

    @Test
    @Disabled
    void savingDataSetWithOneToOneRelations() {
    }

    @Test
    @Disabled
    void gettingDataSetWithOneToManeRelations() {
    }

    @Test
    @Disabled
    void savingDataSetWithOneToManyRelations() {
    }
}
