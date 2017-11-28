package ru.otus.sokolovsky.hw2.execution;

import ru.otus.sokolovsky.hw2.instrumentation.InstrumentationMeasurer;

public class MemoryStand {
    private final MemoryVolumeChecker volumeChecker;
    private final InstrumentationMeasurer instrumentationMeasurer;
    private int counter = 0;

    MemoryStand(MemoryVolumeChecker volumeChecker, InstrumentationMeasurer instrumentationMeasurer) {
        this.volumeChecker = volumeChecker;
        this.instrumentationMeasurer = instrumentationMeasurer;
    }

    public void useMeasureCase(DataGenerator dataGenerator) {
        counter++;
        long objectSize = instrumentationMeasurer.getMemorySize(dataGenerator);
        volumeChecker.initMeasure();
        dataGenerator.createData();
        long usageBytesCount = volumeChecker.takeMeasure();

        long recordSize = (long) Math.rint((double) usageBytesCount / dataGenerator.count());
        reportCaseMeasure(dataGenerator, usageBytesCount, recordSize, objectSize);
    }

    private void reportCaseMeasure(DataGenerator aCase, long dataUsage, long recordSize, long objectSize) {
        System.out.format(
                "\nMeasure case #%d\n%s\nGenerator size without dependencies: %d bytes\nMemory growth: %d bytes (%d bytes/one)\n",
                counter,
                aCase.description(),
                objectSize,
                dataUsage,
                recordSize
        );
    }
}
