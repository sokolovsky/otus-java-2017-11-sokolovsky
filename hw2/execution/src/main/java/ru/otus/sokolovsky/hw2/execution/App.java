package ru.otus.sokolovsky.hw2.execution;

import ru.otus.sokolovsky.hw2.execution.cases.*;
import ru.otus.sokolovsky.hw2.instrumentation.InstrumentationAgent;
import ru.otus.sokolovsky.hw2.instrumentation.InstrumentationMeasurer;

public class App {

    public static void main(String... args) throws InterruptedException {
        MemoryVolumeChecker memoryVolumeChecker = new MemoryVolumeChecker(Runtime.getRuntime());
        InstrumentationMeasurer measurer = InstrumentationAgent.getMeasurer();

        MemoryStand stand = new MemoryStand(memoryVolumeChecker, measurer);

        stand.useCase(new ArrayOfIntegerCreation());
        stand.useCase(new ArrayOfEmptyStringsCreation());
        stand.useCase(new ArrayOfEmptyStringsUseSugarCreation());
        stand.useCase(new ArrayOfLongItems());
        stand.useCase(new ArrayOfIntItems());
        stand.useCase(new ArrayOfCharsUnderString());
        stand.useCase(new ArrayOfCustomClassObjectsWithoutFields());
        stand.useCase(new ArrayOfCustomClassObjectsWithPrimitiveFields());
        stand.useCase(new ArrayOfCustomClassObjectsWithPrimitiveReferences());
        stand.useCase(new SingleObjectCreation());
        stand.useCase(new EmptyStringCreation());
    }
}
