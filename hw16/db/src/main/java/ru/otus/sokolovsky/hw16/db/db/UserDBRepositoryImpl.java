package ru.otus.sokolovsky.hw16.db.db;

import ru.otus.sokolovsky.hw15.domain.UserDBRepository;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDBRepositoryImpl extends AbstractDBRepository<UserDataSet> implements UserDBRepository {

    public UserDBRepositoryImpl(Connection connection) {
        super(connection);
    }

    @Override
    public List<UserDataSet> readByLogin(String login) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("login", login);
        return getDao().loadByFilter(filter, UserDataSet.class);
    }

    @Override
    public boolean hasPassword(String login, String pass) {
         return getDao().loadByFilter(
                 new HashMap<String, Object>() {{
                     this.put("login", login);
                     this.put("password", pass);
                 }}, UserDataSet.class
            ).size() > 0;
    }
}
