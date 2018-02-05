package ru.otus.sokolovsky.hw10.domain;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.otus.sokolovsky.hw10.main.App;
import ru.otus.sokolovsky.hw10.myorm.SqlExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
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
        createExamples();
    }

    @AfterEach
    void tearDown() throws Exception {
        app().dropDbTables();
    }

    private void createExamples() throws SQLException {
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
            long id = 0;
            try {
                id = app().createExecutor().execInsert(s);
            } catch (SQLException e) {
                new RuntimeException(e);
            }
            userIds.add(id);
        });
    }

    static Stream<UserDBService> getAllOrmImplementations() {
        return Stream.of(app().getJdbcService(), app().getHibernateService());
    }

    static Stream<UserDBService> getHibernateOrmImplementations() {
        return Stream.of(app().getHibernateService());
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
        assertThat(user.getId(), is(firstId));

        System.out.println(userDBService.readAll().size());
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

    @ParameterizedTest
    @MethodSource("getHibernateOrmImplementations")
    void gettingDataSetWithOneToOneRelations(UserDBService userDBService) {
        UserDataSet user = userDBService.read(1);
        assertThat(user.getAddress().getStreet(), notNullValue());
    }

    @ParameterizedTest
    @MethodSource("getHibernateOrmImplementations")
    void savingDataSetWithOneToOneRelations(UserDBService userDBService) {
        UserDataSet user = userDBService.read(3);
        assertThat(user.getAddress(), nullValue());

        AddressDataSet address = new AddressDataSet();
        address.setStreet("Some Street");
        user.setAddress(address);
        userDBService.save(user);

        UserDataSet obtainedUser = userDBService.read(3);
        assertThat(obtainedUser.getAddress().getStreet(), is("Some Street"));

    }


    @ParameterizedTest
    @MethodSource("getHibernateOrmImplementations")
    void gettingDataSetWithOneToManeRelations(UserDBService userDBService) {
        UserDataSet user = userDBService.read(1);
        assertThat(user.getPhones().size(), is(2));
        assertThat(user.getPhones().get(0).getNumber(), is("1234"));
    }

    @ParameterizedTest
    @MethodSource("getHibernateOrmImplementations")
    void savingDataSetWithOneToManyRelations(UserDBService userDBService) {
        UserDataSet user = userDBService.read(2);
        PhoneDataSet phoneDataSet = new PhoneDataSet();
        phoneDataSet.setNumber("555");
        user.addPhone(phoneDataSet);
        userDBService.save(user);

        UserDataSet obtainedUser = userDBService.read(2);
        assertThat(
                obtainedUser.getPhones().stream()
                        .map(PhoneDataSet::getNumber).collect(Collectors.toList()),
                hasItem("555")
        );
    }
}
