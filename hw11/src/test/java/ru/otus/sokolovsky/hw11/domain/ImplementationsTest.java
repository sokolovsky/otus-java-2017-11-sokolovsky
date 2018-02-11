package ru.otus.sokolovsky.hw11.domain;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.sokolovsky.hw11.cache.Cache;
import ru.otus.sokolovsky.hw11.cache.CacheRepository;
import ru.otus.sokolovsky.hw11.main.App;
import ru.otus.sokolovsky.hw11.myorm.SqlExecutor;
import ru.otus.sokolovsky.hw11.myormintegration.HoleyCache;
import ru.otus.sokolovsky.hw11.myormintegration.UserDBServiceImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

class ImplementationsTest {
    private List<Long> userIds = new LinkedList<>();
    private static Connection connection;
    private static UserDBServiceImpl jdbcService;

    private static App app() {
        return App.getApplication();
    }

    @BeforeEach
    void setUp() throws Exception {
        app().createDbTables(connection);
        createExamples();
        // setting up for previous tests
        jdbcService.setCache(new HoleyCache<>());
    }

    @AfterEach
    void tearDown() throws Exception {
        app().dropDbTables(connection);
        CacheRepository.getInstance().clear("test");
    }

    @BeforeAll
    static void createServices() {
        connection = app().createConnection();
        jdbcService = app().createUserJDBCService(connection);
    }

    @AfterAll
    static void disposeServices() {
        Stream.of(jdbcService).filter(Objects::nonNull).forEach(DBService::shutdown);
        jdbcService = null;
    }

    private void createExamples() {
        String[] sqlStrings = {
                "INSERT INTO address SET street=\"пл Комсомольская\", id=1",
                "INSERT INTO address SET street=\"пл Комсомольская\", id=2",
                "INSERT INTO user SET name=\"Иван\", age=18, address_id=1, id=1",
                "INSERT INTO user SET name=\"Петр\", age=19, address_id=2, id=2",
                "INSERT INTO user SET name=\"Николай\", age=20, id=3",
                "INSERT INTO phone SET number=\"1234\", user_id=1",
                "INSERT INTO phone SET number=\"4321\", user_id=1",

        };
        Arrays.stream(sqlStrings).forEach(s -> {
            long id;
            try (Connection connection = app().createConnection()) {
                id = app().createExecutor(connection).execInsert(s);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            userIds.add(id);
        });
    }

    private static Stream<UserDBService> getOrmImplementation() {
        return Stream.of(jdbcService);
    }

    @ParameterizedTest
    @MethodSource("getOrmImplementation")
    void loadUser(UserDBService userDBService) {
        UserDataSet user = userDBService.read(userIds.get(0));

        assertThat(user.getName(), is("Иван"));
        assertThat(user.getAge(), is(18));
        assertThat(user.getId(), not(0));
    }

    @ParameterizedTest
    @MethodSource("getOrmImplementation")
    void changeUser(UserDBService userDBService) throws SQLException {
        UserDataSet user = userDBService.read(userIds.get(2));

        user.setAge(11);
        user.setName("Иннокентий");

        userDBService.save(user);

        try(Connection connection = app().createConnection()) {
            SqlExecutor executor = app().createExecutor(connection);
            executor.execSelect("select * from user where id="+user.getId(),
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
    }

    @ParameterizedTest
    @MethodSource("getOrmImplementation")
    void createNewUser(UserDBService userDBService) {
        UserDataSet user = new UserDataSet();
        user.setName("Новый пользователь");
        user.setAge(20);

        userDBService.save(user);

        assertThat(user.getId(), not(anyOf(nullValue(), is(0L))));
    }

    @ParameterizedTest
    @MethodSource("getOrmImplementation")
    void checkSqlInjection(UserDBService userDBService) {
        Long firstId = userIds.get(0);
        UserDataSet user = userDBService.read(firstId);

        user.setName(String.format("Анатолий' where id=%d;\n select * from users where name='", firstId));
        userDBService.save(user);

        UserDataSet restoredUser = userDBService.read(firstId);
        assertThat(user.getId(), is(firstId));

        assertThat(user, not(restoredUser));
        assertThat(restoredUser.getName(), is("Анатолий' where id=1;\n select * from users where name='"));
    }

    @ParameterizedTest
    @MethodSource("getOrmImplementation")
    void shouldReadAllRecordsInDb(UserDBService userDBService) {
        assertThat(userDBService.readAll().size(), is(3));
    }

    @ParameterizedTest
    @MethodSource("getOrmImplementation")
    void shouldSearchUserByName(UserDBService userDBService) {
        UserDataSet ivan = userDBService.readByName("Иван").get(0);
        assertThat(ivan.getAge(), is(18));
    }

    @ParameterizedTest
    @MethodSource("getOrmImplementation")
    void shouldGetFromCache(UserDBService userDBService) {
        Cache<Long, UserDataSet> cache = getCache();
        userDBService.setCache(cache);
        UserDataSet ivan = userDBService.readByName("Иван").get(0);

        userDBService.read(ivan.getId());
        assertThat(cache.hitCount(), is(1));
    }

    @ParameterizedTest
    @MethodSource("getOrmImplementation")
    void shouldUpdateCache(UserDBService userDBService) {
        Cache<Long, UserDataSet> cache = getCache();
        userDBService.setCache(cache);
        UserDataSet ivan = userDBService.readByName("Иван").get(0);

        assertThat(cache.hitCount(), is(0));
        ivan.setName("Ваня");
        userDBService.save(ivan);

        UserDataSet updatedIvan = userDBService.read(ivan.getId());
        assertThat(cache.hitCount(), is(1));
        assertThat(updatedIvan, sameInstance(ivan));
    }

    @ParameterizedTest
    @MethodSource("getOrmImplementation")
    void shouldSaveNewInstanceInCache(UserDBService userDBService) {
        Cache<Long, UserDataSet> cache = getCache();
        userDBService.setCache(cache);

        UserDataSet user = new UserDataSet();
        user.setName("Новый пользователь");
        user.setAge(20);

        userDBService.save(user);

        assertThat(user.getId(), allOf(notNullValue(), not(0)));
        assertThat(cache.hitCount(), is(0));

        userDBService.read(user.getId());
        assertThat(cache.hitCount(), is(1));
    }

    private Cache<Long, UserDataSet> getCache() {
        return CacheRepository.getInstance().getCache("test");
    }
}
