package ru.otus.sokolovsky.hw4;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import static java.lang.management.ManagementFactory.*;

/**
 GC
 • VM options
 • JMX + mbeans + jconsole
 • Thread and heap dumps

 Написать приложение, которое следит за сборками мусора и пишет в лог количество сборок каждого типа
 (young, old) и время которое ушло на сборки в минуту.
 Добиться OutOfMemory в этом приложении через медленное подтекание по памяти (например добавлять
 элементы в List и удалять только половину).
 Настроить приложение (можно добавлять Thread.sleep(...)) так чтобы оно падало с OOM примерно через 5 минут
 после начала работы.
 Собрать статистику (количество сборок, время на сборрки) по разным типам GC.
 Сделать выводы.
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        System.out.println("CPU cores count: " + Runtime.getRuntime().availableProcessors());

        GCRunsMeasurer GCReporter = new GCRunsMeasurer(getGarbageCollectorMXBeans(), System.out);
        Thread reporterThread = new Thread(GCReporter);
        reporterThread.start();
        long oomTotalMemory;

        LeakedProcess leakedProcess = new LeakedProcess(System.out);
        try {
            leakedProcess.run();
        } catch (OutOfMemoryError e) {
            oomTotalMemory = Runtime.getRuntime().totalMemory();
            leakedProcess.erase();
            String mem = Utils.humanReadableByteCount(oomTotalMemory);

            RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
            long uptimeOfSec = rb.getUptime() / 1000;

            System.out.printf("\nMemory (%s) ran out, time %d sec\n", mem, uptimeOfSec);
            GCReporter.publishCommonMeasure();
            throw e;
        } finally {
            reporterThread.interrupt();
        }
    }
}
