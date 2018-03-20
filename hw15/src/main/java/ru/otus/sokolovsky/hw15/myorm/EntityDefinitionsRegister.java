package ru.otus.sokolovsky.hw15.myorm;

import java.util.HashMap;
import java.util.Map;

public class EntityDefinitionsRegister {
    private static Map<Class, EntityDefinition> store = new HashMap<>();

    public static <T> EntityDefinition getDefinition(Class<T> tClass) {
        if (!store.containsKey(tClass)) {
            store.put(tClass, new EntityDefinition<>(tClass));
        }
        return store.get(tClass);
    }
}
