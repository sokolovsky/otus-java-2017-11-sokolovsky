package ru.otus.sokolovsky.hw15.domain;

import ru.otus.l151.messageSystem.Addressee;

import java.util.List;

public interface DBRepository<E extends DataSet> {

    void save(E dataSet);

    E read(long id);

    List<E> readAll();

    List<E> readLast(int limit);

    void shutdown();
}
