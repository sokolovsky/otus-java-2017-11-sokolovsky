package ru.otus.sokolovsky.hw2.instrumentation;

import java.lang.instrument.Instrumentation;

public class InstrumentationMeasurer {

    private Instrumentation instrumentation;

    InstrumentationMeasurer(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
        System.out.println("Instrumentation was init");
    }

    /**
     * Returns estimate of object size without referenced objects
     */
    public long getMemorySize(Object obj) {
        return instrumentation.getObjectSize(obj);
    }

    public long getMemorySize(Object[] array) {
        long res = 0;
        for (Object obj :array) {
            if (obj == null) {
                continue;
            }
            res += getMemorySize(obj);
        }
        return res;
    }
}
