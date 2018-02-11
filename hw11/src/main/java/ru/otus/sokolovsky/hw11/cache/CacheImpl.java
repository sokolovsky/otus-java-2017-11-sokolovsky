package ru.otus.sokolovsky.hw11.cache;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Supplier;

public class CacheImpl<K, V> implements Cache<K, V> {

    private enum Event {
        ADD_ITEM
    }

    private class TimeServant implements Runnable {
        private long lifeTime = 0;
        private LinkedHashMap<K, Long> records = new LinkedHashMap<>();
        private boolean isActive = false;

        void setLifeTime(long seconds) {
            lifeTime = seconds;
        }

        void activate() {
            isActive = true;
        }

        void sleep() {
            isActive = false;
        }

        void notify(Event e, K itemKey) {
            if (e == Event.ADD_ITEM) {
                records.put(itemKey, fNow.get());
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);

                    if (!isActive) {
                        continue;
                    }
                    Long now = fNow.get();
                    Iterator<Map.Entry<K, Long>> iterator = records.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<K, Long> entry = iterator.next();
                        Long time = entry.getValue();
                        if (time + lifeTime < now) {
                            storage.remove(entry.getKey());
                            iterator.remove();
                            continue;
                        }
                        break;
                    }

                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private TimeServant timeServant = new TimeServant();
    private Thread timeServantThread;
    private Supplier<Long> fNow = () -> System.currentTimeMillis() / 1000;

    private LinkedHashMap<K, SoftReference<V>> storage = new LinkedHashMap<>();

    private int volume = 0;

    private int hitCount;
    private int missCount;

    public CacheImpl() {
        timeServantThread = new Thread(timeServant);
        timeServantThread.start();
    }

    @Override
    public void setLifeTime(long seconds) {
        timeServant.setLifeTime(seconds);
        timeServant.activate();
    }

    public void setTimeProducer(Supplier<Long> producer) {
        fNow = producer;
    }

    @Override
    public void setVolume(int capacity) {
        if (capacity == 0) {
            throw new RuntimeException("Capacity must be more than 0");
        }
        volume = capacity;
    }

    @Override
    public void put(K key, V object) {
        if (volume > 0 && storage.size() == volume) {
            K firstKey = storage.keySet().iterator().next();
            storage.remove(firstKey);
        }
        storage.put(key, new SoftReference<>(object));
        timeServant.notify(Event.ADD_ITEM, key);
    }

    @Override
    public V get(K key) {
        SoftReference<V> ref = storage.get(key);
        V val = null;
        if (ref != null && Objects.nonNull(ref.get())) {
            hitCount++;
            val = ref.get();
        } else {
            missCount++;
        }
        return val;
    }

    @Override
    public boolean isPresent(K key) {
        return storage.containsKey(key);
    }

    @Override
    public int hitCount() {
        return hitCount;
    }

    @Override
    public int missCount() {
        return missCount;
    }

    @Override
    public void close() throws Exception {
        storage = new LinkedHashMap<>(volume);
        timeServantThread.interrupt();
    }
}
