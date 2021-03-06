package ru.otus.sokolovsky.hw5.hwunit;

import ru.otus.sokolovsky.hw5.hwunit.annotations.After;
import ru.otus.sokolovsky.hw5.hwunit.annotations.Before;
import ru.otus.sokolovsky.hw5.hwunit.annotations.Test;
import ru.otus.sokolovsky.hw5.hwunit.assertions.AssertionException;
import ru.otus.sokolovsky.hw5.hwunit.utils.ReflectionHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

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

            if (hasAmbiguousDeclarations(method)) {
                throw new RuntimeException("Methods  must have single annotation declare");
            }
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
            processMethod(method);
        }
    }

    private void processMethod(Method method) {
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
        } catch (Throwable thException) {
            total = MethodTotal.ERROR_EXCEPTION;
            message = thException.getMessage();
        }

        MethodResult methodResult = new MethodResult(method.getName(), total);
        methodResult.setMassage(message);
        this.result.add(methodResult);
    }

    public List<MethodResult> getResult() {
        return result;
    }

    @SuppressWarnings("unchecked")
    private boolean hasAmbiguousDeclarations(Method method) {
        Class<? extends Annotation>[] list = new Class[]{Test.class, Before.class, After.class};

        int declarationsCount = 0;
        for (Class<? extends Annotation> aClass : list) {
            if (method.isAnnotationPresent(aClass)) {
                declarationsCount++;

            }
        }
        return declarationsCount > 1;
    }



    private void runTestMethod(Object instance, Method method) throws Throwable {
        try {
            ReflectionHelper.callMethod(instance, method.getName());
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private void runAfterMethods(Object instance) {
        for (Method method : afterMethods) {
            try {
                ReflectionHelper.callMethod(instance, method.getName());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void runBeforeMethods(Object instance) {
        for (Method method : beforeMethods) {
            try {
                ReflectionHelper.callMethod(instance, method.getName());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
