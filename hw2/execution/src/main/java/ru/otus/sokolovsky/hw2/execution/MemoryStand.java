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

    public void useCase(MeasureCase aCase) {
        counter++;

        long objectSize = instrumentationMeasurer.getMemorySize(aCase);

        volumeChecker.initMeasure();
        aCase.createData();
        long usageBytesCount = volumeChecker.takeMeasure();

        long recordSize = (long) Math.rint((double) usageBytesCount / aCase.count());
        reportCaseMeasure(aCase, usageBytesCount, recordSize, objectSize);
    }

    private void reportCaseMeasure(MeasureCase aCase, long dataUsage, long recordSize, long objectSize) {
        System.out.format(
                "\nMeasure case #%d\n%s\nObject size without dependencies: %d kb\nMemory growth: %d bytes (%d bytes/one)\n",
                counter,
                aCase.description(),
                objectSize,
                dataUsage,
                recordSize
        );
    }
}
