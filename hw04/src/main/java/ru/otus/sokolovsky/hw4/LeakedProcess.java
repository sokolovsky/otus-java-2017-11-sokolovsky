package ru.otus.sokolovsky.hw4;

import java.util.ArrayList;
import java.util.List;

public class LeakedProcess implements Runnable {
    private List<String> list = new ArrayList<>();
    private int iterationCounter = 0;
    private final int ITERATION_ITEMS_COUNT = 1000;

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            runIteration();
            Runtime runtime = Runtime.getRuntime();
            System.out.printf(
                    "%d. Allocated %d items of string, used memory: %s\n",
                    iterationCounter,
                    list.size(),
                    Utils.humanReadableByteCount(runtime.totalMemory() - runtime.freeMemory())
            );
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void runIteration() {
        iterationCounter++;
        int newSize = iterationCounter * ITERATION_ITEMS_COUNT;
        int transferredLinksCount = newSize / 2;

        transferredLinksCount = Math.min(transferredLinksCount, list.size());
        List<String> oldListObject = list;

        list = new ArrayList<>(newSize);
        if (transferredLinksCount > 0) {
            list.addAll(oldListObject.subList(0, transferredLinksCount));
        }

        for (int i = transferredLinksCount; i < newSize; i++) {
            list.add("The string for infinite loop:" + i);
        }
    }
}
