package ru.otus.sokolovsky.hw10.myorm;

import ru.otus.sokolovsky.hw10.domain.DataSet;

import java.util.HashMap;
import java.util.Map;

public class EntityDefinitionsRegister {
    private static Map<Class, EntityDefinition<? extends DataSet>> store = new HashMap<>();

    public static <T extends DataSet> EntityDefinition<T> getDefinition(Class<T> tClass) {
        if (!store.containsKey(tClass)) {
            store.put(tClass, new EntityDefinition<>(tClass));
        }
        return (EntityDefinition<T>) store.get(tClass);
    }
}
