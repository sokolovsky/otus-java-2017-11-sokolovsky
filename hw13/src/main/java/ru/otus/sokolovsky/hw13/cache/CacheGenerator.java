package ru.otus.sokolovsky.hw13.cache;

public class CacheGenerator {
    private Cache<Integer, String> cache;

    public CacheGenerator(Cache<Integer, String> cache) {
        this.cache = cache;
    }

    public void start() {
        cache.setLifeTime(5);
        new Thread(new Runnable() {
            int counter = 0;
            @Override
            public void run() {
                while (true) {
                    for (int i = 0; i <= 3; i++) {
                        cache.put(counter++, counter + "--");
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        }).start();
    }

    public Cache<Integer, String> getCache() {
        return cache;
    }
}
