package ru.otus.sokolovsky.hw4;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryUsage;
import java.util.List;
import java.util.Map;

public class GCRunsReporter {
    private List<GarbageCollectorMXBean> gcBeans;
    private boolean listening = false;

    public GCRunsReporter(List<GarbageCollectorMXBean> gcBeans) {
        this.gcBeans = gcBeans;
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

//                Map<String, MemoryUsage> memoryUsageBeforeGc = info.getGcInfo().getMemoryUsageBeforeGc();
//
//                memoryUsageBeforeGc.forEach((k, v) -> {
//                    System.out.println("before - " + k + ": " + v);
//                });
//
//                Map<String, MemoryUsage> memoryUsageAfterGc = info.getGcInfo().getMemoryUsageAfterGc();
//
//                memoryUsageBeforeGc.forEach((k, v) -> {
//                    System.out.println("after - " + k + ": " + v);
//                });

                if (doReport) {
                    System.out.printf(
                            "%s:(id %d, name %s, cause %s), %d ms\n",
                            info.getGcAction(),
                            info.getGcInfo().getId(),
                            info.getGcName(),
                            info.getGcCause(),
                            info.getGcInfo().getDuration()
                    );
                }
            };
            emitter.addNotificationListener(listener, null, null);
            System.out.println("Init listener for GC: " + gcBean.getName());
        }
        listening = true;
    }

    public String getReport() {
        StringBuilder stringBuilder = new StringBuilder("\nTotal gc report\n");
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            stringBuilder.append(String.format(
                    "%s: count - %d, time - %s ms, average time - %s\n",
                    gcBean.getName(),
                    gcBean.getCollectionCount(),
                    gcBean.getCollectionTime(),
                    gcBean.getCollectionTime() / gcBean.getCollectionCount() + " ms"
                )
            );
        }
        return stringBuilder.toString();
    }
}
