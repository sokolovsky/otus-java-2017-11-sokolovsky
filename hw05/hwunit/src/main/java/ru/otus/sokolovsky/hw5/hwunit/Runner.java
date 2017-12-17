package ru.otus.sokolovsky.hw5.hwunit;

import org.reflections.Reflections;
import ru.otus.sokolovsky.hw5.hwunit.annotations.TestSuite;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Runner {
    public static boolean run(Class<?> cls) {
        boolean res = runClass(cls, System.out);
        if (res) {
            outSuccess();
        } else {
            outFailure();
        }
        return res;
    }

    public static boolean run(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> setOfTestSuiteClasses = reflections.getTypesAnnotatedWith(TestSuite.class);
        boolean res = true;
        for (Class<?> cls : setOfTestSuiteClasses) {
            if (!runClass(cls, System.out)) {
                res = false;
            }
        }
        if (res) {
            outSuccess();
        } else {
            outFailure();
        }

        return res;
    }

    private static void outFailure() {
        System.out.println("-----");
        System.out.println("Test run is failure");
    }

    private static void outSuccess() {
        System.out.println("-----");
        System.out.println("Test run is success");
    }

    public static boolean runClass(Class<?> cls, PrintStream out) {
        ClassRunner runner = new ClassRunner(cls);
        runner.run();
        List<ClassRunner.MethodResult> result = runner.getResult();

        out.printf("\nClass:%s\n", cls.getCanonicalName());

        List<String> putOffMessages = new LinkedList<>();
        for (ClassRunner.MethodResult methodResult : result) {
            switch (methodResult.getTotal()) {
                case SUCCESS:
                    out.print(".");
                    break;
                case FAIL:
                    out.print("F");
                    putOffMessages.add(methodResult.getName() + ":" + methodResult.getMassage());
                    break;
                case ERROR_EXCEPTION:
                    out.print("E");
                    putOffMessages.add(methodResult.getName() + ":" + methodResult.getMassage());
                    break;
            }
        }
        System.out.print("\n");

        for (String message : putOffMessages) {
            out.println(message);
        }

        boolean res = true;
        for (ClassRunner.MethodResult methodResult : result) {
            if (methodResult.getTotal() != ClassRunner.MethodTotal.SUCCESS) {
                res = false;
                break;
            }
        }
        return res;
    }
}
