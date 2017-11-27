package ru.otus.sokolovsky.hw2.execution;

import ru.otus.sokolovsky.hw2.execution.cases.*;
import ru.otus.sokolovsky.hw2.instrumentation.InstrumentationAgent;
import ru.otus.sokolovsky.hw2.instrumentation.InstrumentationMeasurer;

public class App {

    public static void main(String... args) throws InterruptedException {
        MemoryVolumeChecker memoryVolumeChecker = new MemoryVolumeChecker(Runtime.getRuntime());
        InstrumentationMeasurer measurer = InstrumentationAgent.getMeasurer();

        MemoryStand stand = new MemoryStand(memoryVolumeChecker, measurer);

        stand.useCase(new SingleObjectCreation());
        stand.useCase(new EmptyStringCreation());
        stand.useCase(new ArrayOfIntegerCreation());
        stand.useCase(new ArrayOfEmptyStringsCreation());
        stand.useCase(new ArrayOfEmptyStringsUseSugarCreation());
    }
}
