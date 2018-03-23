package ru.otus.l151.db;

import ru.otus.l151.app.DBService;
import ru.otus.l151.app.MessageSystemContext;
import ru.otus.l151.messageSystem.Address;

/**
 * Created by tully.
 */
public class DBServiceImpl implements DBService {
    private final Address address;
    private final MessageSystemContext context;

    public DBServiceImpl(MessageSystemContext context, Address address) {
        this.context = context;
        this.address = address;
    }

    public void init() {
        context.getMessageSystem().addAddressee(this);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public int getUserId(String name) {
        //todo: load id from ru.otus.sokolovsky.hw15.db
        return name.hashCode();
    }
}
