package ru.otus.sokolovsky.hw2.execution;

public class MemoryChecker {

    private Runtime runtime;
    private boolean running = false;
    private long initFilled;

    MemoryChecker(Runtime runtime) {
        this.runtime = runtime;
    }

    public void initMeasure() {
        if (running) {
            throw new RuntimeException("Measure must be only once init before taking");
        }
        gc();
        running = true;
        initFilled = runtime.totalMemory() - runtime.freeMemory();
    }

    private void gc() {
        System.gc();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public long takeMeasure() {
        if (!running) {
            throw new RuntimeException("Taking measure must be only after measure init");
        }
        running = false;

        System.gc();
        return (runtime.totalMemory() - runtime.freeMemory()) - initFilled;
    }
}
