package ru.otus.sokolovsky.hw5.hwunit;

import ru.otus.sokolovsky.hw5.hwunit.annotations.After;
import ru.otus.sokolovsky.hw5.hwunit.annotations.Before;
import ru.otus.sokolovsky.hw5.hwunit.annotations.Test;
import ru.otus.sokolovsky.hw5.hwunit.assertions.AssertionException;
import ru.otus.sokolovsky.hw5.hwunit.utils.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.*;

class ClassRunner {
    private Class<?> cls;

    private List<Method> beforeMethods = new ArrayList<>();
    private List<Method> afterMethods = new ArrayList<>();
    private List<Method> testMethods = new ArrayList<>();
    private Map<Method, Class<? extends Throwable>> expectedException = new HashMap<>();

    private List<MethodResult> result = new LinkedList<>();

    public enum MethodTotal {
        ERROR_EXCEPTION, FAIL, SUCCESS
    }

    public class MethodResult {
        private final String name;
        private final MethodTotal total;
        private String massage;

        MethodResult(String name, MethodTotal total) {
            this.name = name;
            this.total = total;
        }

        public String getMassage() {
            return massage;
        }

        public void setMassage(String massage) {
            this.massage = massage;
        }

        public String getName() {
            return name;
        }

        public MethodTotal getTotal() {
            return total;
        }
    }

    ClassRunner(Class<?> cls) {
        this.cls = cls;

        for (Method method : cls.getMethods()) {
            Test testAnnotation = method.getAnnotation(Test.class);
            Before beforeAnnotation = method.getAnnotation(Before.class);
            After afterAnnotation = method.getAnnotation(After.class);

            if (testAnnotation != null) {
                testMethods.add(method);
            }

            if (testAnnotation != null && testAnnotation.expected() != Test.NoneExpected.class) {
                expectedException.put(method, testAnnotation.expected());
            }

            if (beforeAnnotation != null) {
                beforeMethods.add(method);
            }

            if (afterAnnotation != null) {
                afterMethods.add(method);
            }
        }
    }

    public void run() {
        for (Method method : testMethods) {
            Object instance = ReflectionHelper.instantiate(this.cls);
            MethodTotal total = MethodTotal.SUCCESS;
            String message = null;
            try {
                runBeforeMethods(instance);
                try {
                    runTestMethod(instance, method);
                } catch (Throwable e) {
                    Class<? extends Throwable> expected = expectedException.get(method);
                    if (e.getClass() != expected) {
                        throw e;
                    }
                }
                runAfterMethods(instance);
            } catch (AssertionException assertionException) {
                total = MethodTotal.FAIL;
                message = assertionException.getMessage();
            } catch (RuntimeException rException) {
                total = MethodTotal.ERROR_EXCEPTION;
                message = rException.getMessage();
            }

            MethodResult methodResult = new MethodResult(method.getName(), total);
            methodResult.setMassage(message);
            this.result.add(methodResult);
        }
    }

    public List<MethodResult> getResult() {
        return result;
    }

    private void runTestMethod(Object instance, Method method) {
        ReflectionHelper.callMethod(instance, method.getName());
    }

    private void runAfterMethods(Object instance) {
        for (Method method : afterMethods) {
            ReflectionHelper.callMethod(instance, method.getName());
        }
    }

    private void runBeforeMethods(Object instance) {
        for (Method method : beforeMethods) {
            ReflectionHelper.callMethod(instance, method.getName());
        }
    }
}
