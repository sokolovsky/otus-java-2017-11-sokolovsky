package ru.otus.sokolovsky.hw16.db.domain;

import java.util.List;

public interface DBRepository<E extends DataSet> {

    void save(E dataSet);

    E read(long id);

    List<E> readAll();

    List<E> readLast(int limit);

    void shutdown();
}
