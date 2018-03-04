package ru.otus.sokolovsky.hw14;

public class TimeMeasure {

    private long startTime;
    private boolean isFinished = false;
    private long finishTime;

    void start() {
        startTime = System.currentTimeMillis();
        isFinished = false;
        finishTime = 0;
    }

    long get() {
        if (isFinished) {
            return getFinishTime();
        }
        return System.currentTimeMillis() - startTime;
    }

    long getFinishTime() {
        return finishTime;
    }

    void finish() {
        finishTime = get();
        isFinished = true;
    }
}
