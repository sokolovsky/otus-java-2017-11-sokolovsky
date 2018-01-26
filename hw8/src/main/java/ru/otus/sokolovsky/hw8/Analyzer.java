package ru.otus.sokolovsky.hw8;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class Analyzer {
    private Object value;

    private Set<Class> stringClasses = Arrays.stream(new Class[]{
        String.class
    }).collect(Collectors.toSet());

    private Set<Class> numberClasses = Arrays.stream(new Class[]{
        Number.class
    }).collect(Collectors.toSet());

    private Set<Class> booleanClasses = Arrays.stream(new Class[]{
        Boolean.class
    }).collect(Collectors.toSet());

    private Set<Class> arrayClasses = Arrays.stream(new Class[]{
        Collection.class
    }).collect(Collectors.toSet());

    private Set<Class> objectClasses = Arrays.stream(new Class[]{
        Map.class
    }).collect(Collectors.toSet());

    public Analyzer(Object value) {
        this.value = value;
    }

    public Container createContainer() {
        return walk(this.value);
    }

    public Container walk(Object value) {

        if (value == null) {
            return new Container(Container.Type.NULL, null);
        }

        if (instanceOfClasses(value, booleanClasses)) {
            return new Container(Container.Type.BOOLEAN, value);
        }

        if (instanceOfClasses(value, numberClasses)) {
            return new Container(Container.Type.NUMBER, value);
        }

        if (instanceOfClasses(value, stringClasses)) {
            return new Container(Container.Type.STRING, value);
        }

        if (instanceOfClasses(value, arrayClasses) || value.getClass().isArray()) {
            List<Container> containers = collectArrayContainer(value);
            return new Container(Container.Type.ARRAY, containers);
        }

        if (instanceOfClasses(value, objectClasses)) {
            Map<String, Container> containerMap = collectMapObjectContainer(value);
            return new Container(Container.Type.OBJECT, containerMap);
        }

        Map<String, Container> containerMap = collectPlainObjectContainer(value);
        return new Container(Container.Type.OBJECT, containerMap);
    }

    private Map<String, Container> collectPlainObjectContainer(Object value) {
        Map<String, Container> map = new HashMap<>();
        for (Field field : value.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), walk(field.get(value)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private Map<String, Container> collectMapObjectContainer(Object value) {
        Map<String, Container> map = new HashMap<>();
        if (! (value instanceof Map)) {
            throw new RuntimeException("Wrong map");
        }
        for (Object entry : ((Map) value).entrySet()) {
            Object key = ((Map.Entry) entry).getKey();
            if (!(key instanceof String)) {
                throw new RuntimeException("Keys of map is needed use only as String");
            }
            map.put((String) key, walk(((Map.Entry) entry).getValue()));
        }
        return map;
    }

    private List<Container> collectArrayContainer(Object value) {
        List<Container> list = new LinkedList<>();


        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            for (int i = 0; i < length; i ++) {
                list.add(walk(Array.get(value, i)));
            }
            return list;
        }

        List iterable;

        if (value instanceof List) {
            iterable = (List) value;
        } else {
            throw new RuntimeException();
        }

        for (Object iValue : iterable) {
            list.add(walk(iValue));
        }
        return list;
    }

    private boolean instanceOfClass(Object value, Class<?> cl) {
        if (cl == Object.class) {
            return true;
        }
        Class iClass = value.getClass();
        while (iClass != Object.class) {
            if (iClass == cl) {
                return true;
            }
            iClass = iClass.getSuperclass();
        }
        return false;
    }

    private boolean instanceOfClasses(Object value, Set<Class> setOfClasses) {
        return setOfClasses.stream().anyMatch(cl -> instanceOfClass(value, cl));
    }
}
