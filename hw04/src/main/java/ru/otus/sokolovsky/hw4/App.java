package ru.otus.sokolovsky.hw4;

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
    public static void main(String[] args) throws InterruptedException {
        GCRunsReporter GCReporter = new GCRunsReporter(getGarbageCollectorMXBeans());
        GCReporter.listen(false);

        LeakedProcess leakedProcess = new LeakedProcess();
        try {
            leakedProcess.run();
        } catch (OutOfMemoryError e) {
            String mem = Utils.humanReadableByteCount(Runtime.getRuntime().totalMemory());
            System.out.printf("Memory (%s) is run out\n%s", mem, GCReporter.getReport());
        }
    }
}
