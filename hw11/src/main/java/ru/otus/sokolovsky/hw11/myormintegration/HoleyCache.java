package ru.otus.sokolovsky.hw11.myormintegration;

import ru.otus.sokolovsky.hw11.cache.Cache;
import ru.otus.sokolovsky.hw11.domain.DataSet;

import java.util.function.Supplier;

public class HoleyCache<E extends DataSet> implements Cache<Long, E> {
    private int missCount = 0;
    @Override
    public void setLifeTime(long seconds) {
    }

    @Override
    public void setIdleTime(long seconds) {
    }

    @Override
    public void setTimeProducer(Supplier<Long> producer) {
    }

    @Override
    public void setVolume(int count) {
    }

    @Override
    public void put(Long key, E object) {
    }

    @Override
    public E get(Long key) {
        missCount++;
        return null;
    }

    @Override
    public boolean isPresent(Long key) {
        return false;
    }

    @Override
    public int hitCount() {
        return 0;
    }

    @Override
    public int missCount() {
        return missCount;
    }

    @Override
    public void close() {
    }
}
