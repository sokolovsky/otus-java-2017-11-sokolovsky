package ru.otus.sokolovsky.hw2.execution;

import ru.otus.sokolovsky.hw2.execution.creators.*;
import ru.otus.sokolovsky.hw2.execution.creators.Object;
import ru.otus.sokolovsky.hw2.execution.generators.ArrayOfEmptyIntegerArrays;
import ru.otus.sokolovsky.hw2.execution.generators.ArrayOfIntItems;
import ru.otus.sokolovsky.hw2.execution.generators.ArrayOfLongItems;
import ru.otus.sokolovsky.hw2.instrumentation.InstrumentationAgent;
import ru.otus.sokolovsky.hw2.instrumentation.InstrumentationMeasurer;

public class App {

    private static int[] measureGrowth = {1000, 10000, 100000, 1000000};

    public static void main(String... args) throws InterruptedException {
        MemoryVolumeChecker memoryVolumeChecker = new MemoryVolumeChecker(Runtime.getRuntime());
        InstrumentationMeasurer measurer = InstrumentationAgent.getMeasurer();

        MemoryStand stand = new MemoryStand(memoryVolumeChecker, measurer);

        testMeasureGrowth(stand, new ObjectDataGenerator(new Object()));

        testMeasureGrowth(stand, new ObjectDataGenerator(new EmptyString()));
        testMeasureGrowth(stand, new ObjectDataGenerator(new EmptyStringWithConstructor()));
        testMeasureGrowth(stand, new ObjectDataGenerator(new EmptyStringWithCharConstructor()));

        testMeasureGrowth(stand, new ObjectDataGenerator(new ClassWithoutFieldsCreator()));
        testMeasureGrowth(stand, new ObjectDataGenerator(new ClassWithPrimitiveFieldsCreator()));
        testMeasureGrowth(stand, new ObjectDataGenerator(new ClassWithReferencesCreator()));

        testMeasureGrowth(stand, new ArrayOfIntItems());
        testMeasureGrowth(stand, new ArrayOfLongItems());
        testMeasureGrowth(stand, new ArrayOfEmptyIntegerArrays());

    }

    private static void testMeasureGrowth(MemoryStand stand, AbstractDataGenerator prototypeGenerator) {
        for (int aMeasureGrowth : measureGrowth) {
            DataGenerator dataGenerator = null;
            try {
                dataGenerator = prototypeGenerator.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                break;
            }
            dataGenerator.setCount(aMeasureGrowth);
            stand.useMeasureCase(dataGenerator);
        }
    }
}
