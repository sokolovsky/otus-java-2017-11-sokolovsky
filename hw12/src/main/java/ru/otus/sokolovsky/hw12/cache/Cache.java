package ru.otus.sokolovsky.hw12.cache;

import java.util.Map;
import java.util.function.Supplier;

public interface Cache<K, V> extends AutoCloseable {

    void setLifeTime(long seconds);

    void setIdleTime(long seconds);

    void setTimeProducer(Supplier<Long> producer);

    void setVolume(int count);

    void put(K key, V object);

    V get(K key);

    boolean isPresent(K key);

    int hitCount();

    int missCount();

    Map<K, V> data();
}
