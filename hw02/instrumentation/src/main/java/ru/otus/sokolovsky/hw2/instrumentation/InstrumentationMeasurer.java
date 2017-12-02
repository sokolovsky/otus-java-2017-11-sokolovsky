package ru.otus.sokolovsky.hw2.instrumentation;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.*;
import java.util.*;

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
        if (instrumentation == null) {
            throw new IllegalStateException(
                    "Instrumentation environment not initialised.");
        }

        return instrumentation.getObjectSize(obj);
    }

    /**
     * Returns deep size of object with field reference objects
     */
    public long deepMemorySize(Object obj) {
        Map visited = new IdentityHashMap();
        Stack<Object> stack = new Stack<>();
        stack.push(obj);

        long result = 0;
        do {
            result += internalSizeOf(stack.pop(), stack, visited);
        } while (!stack.isEmpty());
        return result;
    }

    private boolean skipObject(Object obj, Map visited) {
        return obj == null || visited.containsKey(obj);
    }

    private long internalSizeOf(Object obj, Stack<Object> stack, Map visited) {
        if (skipObject(obj, visited)) {
            return 0;
        }
        visited.put(obj, null);

        Class aClass = obj.getClass();
        if (aClass.isArray()) {
            addArrayElementsToStack(aClass, obj, stack);
            return getMemorySize(obj);
        }
        while (aClass != null) {
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                boolean isStaticField = Modifier.isStatic(field.getModifiers());
                boolean isPrimitiveTypeField = field.getType().isPrimitive();

                if (!isStaticField && !isPrimitiveTypeField) {
                    try {
                        stack.add(field.get(obj));
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            aClass = aClass.getSuperclass();
        }
        return getMemorySize(obj);
    }

    private void addArrayElementsToStack(Class aClass, Object obj, Stack<Object> stack) {
        if (aClass.getComponentType().isPrimitive()) {
            return ;
        }

        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            stack.add(Array.get(obj, i));
        }
    }
}
