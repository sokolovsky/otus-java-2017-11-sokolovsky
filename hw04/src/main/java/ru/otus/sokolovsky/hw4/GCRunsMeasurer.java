package ru.otus.sokolovsky.hw4;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.io.PrintStream;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

public class GCRunsMeasurer implements Runnable {
    private List<GarbageCollectorMXBean> gcBeans;
    private PrintStream log;
    private boolean listening = false;

    private final int MEASURE_INTERVAL = 60_000;

    private Counter commonNewCounter = new Counter();
    private Counter commonOldCounter = new Counter();
    private Counter newCycleCounter = new Counter();
    private Counter oldCycleCounter = new Counter();

    public GCRunsMeasurer(List<GarbageCollectorMXBean> gcBeans, PrintStream log) {
        this.gcBeans = gcBeans;
        this.log = log;
    }

    public void listen(boolean doReport) {
        if (listening) {
            throw new RuntimeException("Listening has been started");
        }
        for (GarbageCollectorMXBean gcBean : gcBeans) {

            NotificationEmitter emitter = (NotificationEmitter) gcBean;
            NotificationListener listener = (notification, handback) -> {
                if (!notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    return;
                }
                GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                switch (info.getGcAction()) {
                    case "end of minor GC":
                        newCycleCounter.incrementCount();
                        newCycleCounter.addDuration(info.getGcInfo().getDuration());
                        break;
                    case "end of major GC":
                        oldCycleCounter.incrementCount();
                        oldCycleCounter.addDuration(info.getGcInfo().getDuration());
                        break;
                    default:
                        throw new RuntimeException("Another type of collecting do not expected.");
                }

                if (doReport) {
                    log.printf(
                            "--> %s: (id %d, name %s, cause %s), %d ms\n",
                            info.getGcAction(),
                            info.getGcInfo().getId(),
                            info.getGcName(),
                            info.getGcCause(),
                            info.getGcInfo().getDuration()
                    );
                }

            };
            emitter.addNotificationListener(listener, null, null);
            log.println("Init listener for GC: " + gcBean.getName());
        }
        listening = true;
    }

    @Override
    public void run() {
        listen(false);
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(MEASURE_INTERVAL);
                commonNewCounter.add(newCycleCounter);
                commonOldCounter.add(oldCycleCounter);
                publishMeasure();
                clearMeasure();
            }
        } catch (InterruptedException e) {
            log.println("Measurer was interrupted");
        }
    }

    private void clearMeasure() {
        newCycleCounter = new Counter();
        oldCycleCounter = new Counter();
    }

    private void publishMeasure() {
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        long uptimeOfSec = rb.getUptime() / 1000;
        log.printf("\nUptime: %d sec\n", uptimeOfSec);
        log.printf(
            "new GC generation: count %d, duration %d ms, average %d ms\n",
            newCycleCounter.getCount(),
            newCycleCounter.getDuration(),
            newCycleCounter.getAverage()
        );
        log.printf(
            "old GC generation: count %d, duration %d ms, average %d ms\n",
            oldCycleCounter.getCount(),
            oldCycleCounter.getDuration(),
            oldCycleCounter.getAverage()
        );
        log.printf("Common GC duration: %d ms\n\n", newCycleCounter.getDuration() + oldCycleCounter.getDuration());
    }

    public void publishCommonMeasure() {
        log.println("Common measure:");
        log.printf(
            "New generation: count %d, duration %d ms, average %d ms\n",
            commonNewCounter.getCount(),
            commonNewCounter.getDuration(),
            commonNewCounter.getAverage()
        );
        log.printf(
            "Old generation: count %d, duration %d ms, average %d ms\n",
            commonOldCounter.getCount(),
            commonOldCounter.getDuration(),
            commonOldCounter.getAverage()
        );
    }

    private class Counter {
        private int count = 0;
        private long duration = 0;

        void incrementCount() {
            count++;
        }

        void add(Counter counter) {
            this.count += counter.getCount();
            this.addDuration(counter.getDuration());
        }

        void addDuration(long duration) {
            this.duration += duration;
        }

        long getAverage() {
            return duration / (count > 0 ? count : 1);
        }

        int getCount() {
            return count;
        }

        long getDuration() {
            return duration;
        }
    }
}
