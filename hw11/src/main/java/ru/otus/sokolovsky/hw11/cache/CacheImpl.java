package ru.otus.sokolovsky.hw11.cache;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Supplier;

public class CacheImpl<K, V> implements Cache<K, V> {

    private enum Event {
        ADD_ITEM, GET_ITEM
    }

    private class TimeServant implements Runnable {
        private long lifeTime = 0;
        private long idleTime = 0;

        private LinkedHashMap<K, Long> werePut = new LinkedHashMap<>();
        private LinkedHashMap<K, Long> wereGot = new LinkedHashMap<>();

        void setLifeTime(long seconds) {
            lifeTime = seconds;
        }

        void setIdleTime(long seconds) {
            idleTime = seconds;
        }

        void notify(Event e, K itemKey) {
            if (e == Event.ADD_ITEM) {
                werePut.put(itemKey, fNow.get());
                wereGot.put(itemKey, fNow.get());
            }
            if (e == Event.GET_ITEM) {
                wereGot.put(itemKey, fNow.get());
            }
        }

        boolean needIdleHandle() {
            return idleTime > 0;
        }

        boolean needLifeTimeHandle() {
            return lifeTime > 0;
        }

        void removeItemsByTime(LinkedHashMap<K, Long> store, Long now, long lifeTime) {
            Iterator<Map.Entry<K, Long>> iterator = store.entrySet().iterator();
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
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(50);

                    Long now = fNow.get();

                    if (needLifeTimeHandle()) {
                        removeItemsByTime(werePut, now, lifeTime);
                    }

                    if (needIdleHandle()) {
                        removeItemsByTime(wereGot, now, idleTime);
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

    CacheImpl() {
        timeServantThread = new Thread(timeServant);
        timeServantThread.start();
    }

    @Override
    public void setLifeTime(long seconds) {
        timeServant.setLifeTime(seconds);
    }

    @Override
    public void setIdleTime(long seconds) {
        timeServant.setIdleTime(seconds);
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
        timeServant.notify(Event.GET_ITEM, key);
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
    public void close() {
        storage = new LinkedHashMap<>(volume);
        timeServantThread.interrupt();
    }
}
