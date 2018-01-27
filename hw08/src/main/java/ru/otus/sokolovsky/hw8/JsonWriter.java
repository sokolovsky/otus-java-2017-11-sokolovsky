package ru.otus.sokolovsky.hw8;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JsonWriter {

    private static Set<Container.Type> primitiveTypes = new HashSet<>();

    static {
        primitiveTypes.add(Container.Type.NUMBER);
        primitiveTypes.add(Container.Type.NULL);
        primitiveTypes.add(Container.Type.STRING);
        primitiveTypes.add(Container.Type.BOOLEAN);
    }

    public static String parse(Object value) {
        Analyzer analyzer = new Analyzer(value);
        Container analyzedData = analyzer.analyze();
        if (isPrimitive(analyzedData)) {
            return value.toString();
        }
        return traverse(analyzedData).toJSONString();
    }

    public static JSONAware traverse(Container container) {
        JSONAware json;
        switch (container.getType()){
            case OBJECT:
                json = new JSONObject();
                renderObject(container, (JSONObject) json);
                return json;
            case ARRAY:
                json = new JSONArray();
                renderArray(container, (JSONArray) json);
                return json;
            default:
                throw new RuntimeException("Can't traverse not iterable value");
        }
    }

    private static void renderObject(Container container, JSONObject json) {
        for (Map.Entry<String, Container> entry : container.getMap().entrySet()) {
            Container iContainer = entry.getValue();
            Object value = iContainer.getValue();
            if (!isPrimitive(iContainer)) {
                value = traverse(iContainer);
            }
            json.put(entry.getKey(), value);
        }
    }

    private static void renderArray(Container container, JSONArray json) {
        for (Container iContainer : container.getList()) {
            if (isPrimitive(iContainer)) {
                json.add(iContainer.getValue());
                continue;
            }
            json.add(traverse(iContainer));
        }
    }

    private static boolean isPrimitive(Container container) {
        return primitiveTypes.contains(container.getType());
    }
}
