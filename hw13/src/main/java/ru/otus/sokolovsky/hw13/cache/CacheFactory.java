package ru.otus.sokolovsky.hw13.cache;

public interface CacheFactory<K, V> {
     Cache<K, V> createInstance();
}
