package ru.otus.sokolovsky.hw2.execution;

import ru.otus.sokolovsky.hw2.instrumentation.InstrumentationAgent;
import ru.otus.sokolovsky.hw2.instrumentation.InstrumentationMeasurer;

public class App {

    public static void main(String... args) throws InterruptedException {
        MemoryChecker memoryChecker = new MemoryChecker(Runtime.getRuntime());
        InstrumentationMeasurer measurer = InstrumentationAgent.getMeasurer();

        singleObjectMeasure(memoryChecker, measurer);
        singleStringMeasure(memoryChecker, measurer);
        multipleObjectMeasure(memoryChecker, measurer, 100_000);
    }

    private static void singleObjectMeasure(MemoryChecker memoryChecker, InstrumentationMeasurer measurer) {
        memoryChecker.initMeasure();
        Object obj = new Object();
        long checkerResult = memoryChecker.takeMeasure();
        long measurerResult = measurer.getMemorySize(obj);
        printMeasureResult("Single object", checkerResult, measurerResult);
    }

    private static void singleStringMeasure(MemoryChecker memoryChecker, InstrumentationMeasurer measurer) {
        memoryChecker.initMeasure();
        String string = new String("");
        long checkerResult = memoryChecker.takeMeasure();
        long measurerResult = measurer.getMemorySize(string);
        printMeasureResult("Single object", checkerResult, measurerResult);
    }

    private static void multipleObjectMeasure(MemoryChecker memoryChecker, InstrumentationMeasurer measurer, int count) {
        memoryChecker.initMeasure();
        Object[] array = new Object[count];
        printMeasureResult("Array of references", memoryChecker.takeMeasure(), measurer.getMemorySize(array));

        memoryChecker.initMeasure();
        array = new Object[count];
        for (int i = 0; i < count; i++) {
            array[i] = new Object();
        }
        printMeasureResult("Array of objects", memoryChecker.takeMeasure(), measurer.getMemorySize(array));
    }

    private static void printMeasureResult(String subject, long result, long measurerResult) {
        System.out.format("Result of measure `%s` \n" +
                "Grows of memory is: %d in bytes\n" +
                "Measure estimation is: %d in bytes\n\n",
                subject, result, measurerResult
        );
    }
}