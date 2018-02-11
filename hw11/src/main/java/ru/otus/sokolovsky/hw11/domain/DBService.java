package ru.otus.sokolovsky.hw11.domain;

import java.util.List;

public interface DBService<E extends DataSet> {

    void save(E dataSet);

    E read(long id);

    List<E> readAll();

    void shutdown();
}
