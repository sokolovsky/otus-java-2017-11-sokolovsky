package ru.otus.sokolovsky.hw11.cache;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
class CacheTest {

    private Cache<String, Integer> getCache() {
        return CacheRepository.getInstance().getCache("test");
    }

    @AfterEach
    void clearCacheInRepo() {
        CacheRepository.getInstance().clear("test");
    }

    @Test
    void storeValues() throws Exception {
        try (Cache<String, Integer> cache = getCache()) {
            cache.put("1", 1);

            assertThat(cache.get("1"), is(1));
        }
    }

    @Test
    void hitCount() throws Exception {
        try (Cache<String, Integer> cache = getCache()) {
            cache.put("1", 1);
            cache.put("2", 1);
            cache.put("3", 1);

            cache.get("1");
            cache.get("2");
            cache.get("6");

            assertThat(cache.hitCount(), is(2));
        }
    }

    @Test
    void missCount() throws Exception {
        try (Cache<String, Integer> cache = getCache()) {
            cache.put("1", 1);
            cache.put("2", 1);
            cache.put("3", 1);

            cache.get("1");
            cache.get("4");
            cache.get("6");

            assertThat(cache.missCount(), is(2));
        }
    }

    @Test
    void isPresentTest() throws Exception {
        try (Cache<String, Integer> cache = getCache()) {
            cache.put("1", 1);
            cache.put("2", 1);

            assertThat(cache.isPresent("1"), is(true));
            assertThat(cache.isPresent("4"), is(false));
        }
    }

    @Test
    void memoryOverflow() throws Exception {
        try (Cache<String, int[]> cache = CacheRepository.getInstance().getCache("test")) {

            Supplier<int[]> createArray = () -> new int[1024 * 1024];
            for (long i = 0; i <= 1000; i++) {
                cache.put(i + "", createArray.get());
            }
        }
    }

    @Test
    void capacityConstraints() throws Exception {
        try (Cache<String, Integer> cache = getCache()) {
            cache.setVolume(10);

            for (int i = 0; i < 30; i++) {
                cache.put(i + "", 1);
            }

            assertThat(cache.get("1"), nullValue());
            assertThat(cache.get("29"), notNullValue());
        }
    }

    @Test
    void lifeTimeOfData() throws Exception {
        class HandleTimer implements Supplier<Long> {
            private long time = 0;

            private void setTime(long time) {
                this.time = time;
            }
            @Override
            public Long get() {
                return time;
            }
        }
        HandleTimer handleTimer = new HandleTimer();
        try (Cache<String, Integer> cache = getCache()) {
            cache.setTimeProducer(handleTimer);
            cache.setLifeTime(100);
            cache.put("1", 1);
            cache.put("2", 1);
            cache.put("3", 1);
            cache.put("4", 1);

            handleTimer.setTime(50);
            Thread.sleep(100);

            assertThat(cache.get("1"), notNullValue());

            handleTimer.setTime(101);
            Thread.sleep(100);

            assertThat(cache.get("1"), nullValue());
        }
    }
}
