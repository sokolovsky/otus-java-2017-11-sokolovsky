package ru.otus.sokolovsky.hw2.instrumentation;

import java.lang.instrument.Instrumentation;

public class InstrumentationAgent {
    private static InstrumentationMeasurer measurer;

    public static void premain(String agentArgument, Instrumentation instrumentation) {
        InstrumentationAgent.measurer = new InstrumentationMeasurer(instrumentation);
    }

    public static InstrumentationMeasurer getMeasurer() {
        InstrumentationMeasurer measurer = InstrumentationAgent.measurer;
        if (measurer == null) {
            throw new RuntimeException("Instrumentation lib doesn't run");
        }
        return measurer;
    }
}
