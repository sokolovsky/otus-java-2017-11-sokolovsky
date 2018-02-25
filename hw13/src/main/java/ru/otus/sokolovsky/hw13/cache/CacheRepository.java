package ru.otus.sokolovsky.hw13.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CacheRepository {
    public static final String COMMON_DATA = "common";

    private static final CacheRepository instance = new CacheRepository();

    private Map<String, Cache> repo = new HashMap<>();

    private CacheRepository() {
    }

    public static CacheRepository getInstance() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> getCache(String name) {
        Objects.requireNonNull(name);
        if (!repo.containsKey(name)) {
            repo.put(name, CacheRepository.<V>createCache());
        }
        return (Cache<K, V>) repo.get(name);
    }

    public void clear(String name) {
        repo.remove(name);
    }

    private static <V> Cache<String, V> createCache() {
        return new CacheImpl<>();
    }
}
