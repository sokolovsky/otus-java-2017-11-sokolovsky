package ru.otus.sokolovsky.hw14;

import java.util.*;
import java.util.function.Supplier;

public class MultithreadingSort {
    private int threadCount;
    private List<Thread> threads = new LinkedList<>();

    public MultithreadingSort(int threadCount) {
        if (threadCount < 1) {
            throw new RuntimeException("Count of threads must be more than 0");
        }
        this.threadCount = threadCount;
    }

    public void execute(int[] ar) {
        Objects.requireNonNull(ar);
        int partLen = ar.length / threadCount;

        for (int i = 0; i < threadCount - 1; i++) {
            int offset = i * partLen;
            sortInThread(ar, offset, partLen);
        }
        int offset = (threadCount - 1) * partLen;
        TimeMeasure timer = new TimeMeasure();
        timer.start();
        sortInThread(ar, offset, ar.length - offset);
        waitPartitionSort();
        System.out.printf("Threads work take %d ms of %d threads\n", timer.get(), threadCount);
        timer.finish();
        timer.start();
        merge(ar, partLen);
        System.out.printf("Merge work take %d ms\n", timer.get());
    }

    private void waitPartitionSort() {
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void merge(int[] ar, int partLen) {
        int[] pointers = new int[threadCount];

        Supplier<Integer> min = () -> {
            Integer res = Integer.MAX_VALUE;
            int minValueThread = -1;
            for (int i = 0; i < threadCount; i++) {
                int threadIndex = pointers[i];
                boolean isLastThread = i == threadCount - 1;
                if (!isLastThread && threadIndex == partLen) {
                    continue;
                }
                if (isLastThread && threadIndex + i * partLen >= ar.length) {
                    continue;
                }
                int value = ar[threadIndex + i * partLen];
                if (value < res) {
                    res = value;
                    minValueThread = i;
                }
            }
            pointers[minValueThread]++;
            return res;
        };

        int[] temp = new int[ar.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = min.get();
        }
        System.arraycopy(temp, 0, ar, 0, ar.length);
    }

    private void sortInThread(int[] ar, int start, int length) {
        Thread thread = new Thread(() -> {
            Utils.sort(ar, start, length);
        });
        threads.add(thread);
        thread.start();
    }
}
