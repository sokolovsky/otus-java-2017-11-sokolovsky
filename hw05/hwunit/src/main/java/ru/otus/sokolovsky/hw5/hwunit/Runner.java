package ru.otus.sokolovsky.hw5.hwunit;

import org.reflections.Reflections;
import ru.otus.sokolovsky.hw5.hwunit.annotations.TestSuite;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Runner {
    public static boolean run(Class<?> cls) {
        return runClass(cls, System.out);
    }

    public static boolean run(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> setOfTestSuiteClasses = reflections.getTypesAnnotatedWith(TestSuite.class);
        boolean res = true;
        for (Class<?> cls : setOfTestSuiteClasses) {
            if (!run(cls)) {
                res = false;
            }
        }
        return res;
    }

    public static boolean runClass(Class<?> cls, PrintStream out) {
        ClassRunner runner = new ClassRunner(cls);
        runner.run();
        List<ClassRunner.MethodResult> result = runner.getResult();

        out.printf("\nClass:%s\n", cls.getCanonicalName());

        List<String> putOffMessages = new LinkedList<>();
        for (ClassRunner.MethodResult methodResult : result) {
            switch (methodResult.getTotal()) {
                case FAIL:
                    out.print("F");
                    break;
                case SUCCESS:
                    out.print(".");
                    putOffMessages.add(methodResult.getName() + ":" + methodResult.getMassage());
                    break;
                case ERROR_EXCEPTION:
                    out.print("E");
                    putOffMessages.add(methodResult.getName() + ":" + methodResult.getMassage());
                    break;
            }
        }

        for (String message : putOffMessages) {
            out.println(message);
        }

        for (ClassRunner.MethodResult methodResult : result) {
            if (methodResult.getTotal() != ClassRunner.MethodTotal.SUCCESS) {
                return false;
            }
        }
        return true;
    }
}
