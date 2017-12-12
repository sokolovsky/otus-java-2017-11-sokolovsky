package ru.otus.sokolovsky.hw4;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class LeakedProcess implements Runnable {
    private List<String> list = new ArrayList<>();
    private int iterationCounter = 0;
    private final int ITERATION_BATCH_COUNT = 1000;
    private PrintStream log;

    public LeakedProcess(PrintStream log) {
        this.log = log;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            runIteration();
            log.printf(
                    "%d. Allocated %d items of string, used memory: %s\n",
                    iterationCounter,
                    list.size(),
                    Utils.humanReadableByteCount(Utils.getUsedMemory())
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
        int newSize = iterationCounter * ITERATION_BATCH_COUNT;
        int transferredLinksCount = newSize / 2;

        transferredLinksCount = Math.min(transferredLinksCount, list.size());
        List<String> oldListObject = list;

        list = new ArrayList<>(newSize);
        if (transferredLinksCount > 0) {
            list.addAll(oldListObject.subList(0, transferredLinksCount));
        }

        for (int i = transferredLinksCount; i < newSize; i++) {
            list.add(
                new StringBuilder()
                    .append("Java garbage collection is ")
                    .append("the process by which Java programs perform automatic memory management. ")
                    .append("Java programs compile to bytecode that can be run on a Java Virtual Machine, ")
                    .append("or JVM for short. When Java programs run on the JVM, objects are created on the heap,")
                    .append(" which is a portion of memory dedicated to the program. Eventually, some objects will ")
                    .append("no longer be needed. The garbage collector finds these unused objects and deletes ")
                    .append("them to free up memory.\n")
                    .append("The string for infinite loop:")
                    .append(i)
                    .toString()
            );
        }
    }

    public void erase() {
        list = null;
        list = new ArrayList<>();
    }
}
