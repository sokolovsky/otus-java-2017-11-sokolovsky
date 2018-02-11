package ru.otus.sokolovsky.hw11.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CacheRepository {
    private static final CacheRepository instance = new CacheRepository();

    private Map<String, Cache> repo = new HashMap<>();

    private CacheRepository() {
    }

    public static CacheRepository getInstance() {
        return instance;
    }

    public <V> Cache<String, V> getCache(String name) {
        Objects.requireNonNull(name);
        if (!repo.containsKey(name)) {
            repo.put(name, CacheRepository.<V>createCache());
        }
        return (Cache<String, V>) repo.get(name);
    }

    public void clear(String name) {
        repo.remove(name);
    }

    private static <V> Cache<String, V> createCache() {
        return new CacheImpl<>();
    }
}
