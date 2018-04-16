package ru.otus.sokolovsky.hw16.integration.message;

import org.json.JSONObject;
import org.json.JSONWriter;

import java.util.*;

public class MessageTransformer {

    public static Message fromJson(String json) {
        JSONObject rootJson = new JSONObject(json);
        JSONObject headers = rootJson.getJSONObject("headers");
        String destination = rootJson.optString("destination");
        String source = rootJson.optString("source");
        String name = rootJson.optString("name");
        String type = rootJson.getString("type");
        MessageType messageType = MessageType.byCode(type);

        Message message = new TextMessageImpl(destination, name, messageType);
        ((TextMessageImpl) message).setBody(rootJson.optString("body"));

        if (rootJson.optJSONObject("body") != null) {
            message = new ParametrizedMessageImpl(destination, name, messageType);
            JSONObject body = rootJson.optJSONObject("body");
            for (Iterator<String> it = body.keys(); it.hasNext(); ) {
                String key = it.next();
                ((ParametrizedMessageImpl) message).setParameter(key, body.getString(key));
            }
        }
        if (rootJson.optJSONArray("body") != null) {
            message = new ListParametrizedMessageImpl(destination, name, messageType);
            List<Map<String, String>> list = new ArrayList<>();
            for (Object obj : rootJson.optJSONArray("body")) {
                Map<String, String> map = new HashMap<>();
                list.add(map);
                for (Iterator<String> it =((JSONObject) obj).keys(); it.hasNext(); ) {
                    String key = it.next();
                    map.put(key, ((JSONObject) obj).getString(key));
                }
            }
            ((ListParametrizedMessageImpl) message).setList(list);
        }

        message.setSource(source);
        for (Iterator<String> it = headers.keys(); it.hasNext(); ) {
            String key = it.next();
            message.setHeader(key, headers.getString(key));
        }
        return message;
    }

    public static String toJson(ParametrizedMessage message) {
        StringBuilder stringBuilder = new StringBuilder();
        JSONWriter rootBuilder = new JSONWriter(stringBuilder);

        rootBuilder.object();
        setMetaData(rootBuilder, message);

        JSONWriter body = rootBuilder.key("body").object();
        message.getParameters().forEach((n, v) -> body.key(n).value(v));
        body.endObject();
        rootBuilder.endObject();
        return stringBuilder.toString();
    }

    private static void setMetaData(JSONWriter json, Message message) {
        json.key("type").value(message.getType().getCode());
        json.key("destination").value(message.getDestination());
        json.key("source").value(message.getSource());
        json.key("name").value(message.getName());
        JSONWriter headers = json.key("headers").object();
        message.getHeaders().forEach((k, v) -> headers.key(k).value(v));
        headers.endObject();
    }

    public static String toJson(ListParametrizedMessage message) {
        StringBuilder stringBuilder = new StringBuilder();
        JSONWriter rootBuilder = new JSONWriter(stringBuilder);

        rootBuilder.object();
        setMetaData(rootBuilder, message);

        JSONWriter body = rootBuilder.key("body").array();
        List<Map<String, String>> list = message.getList();
        list.forEach((obj) -> {
            body.object();
            obj.forEach((k, v) -> body.key(k).value(v));
            body.endObject();
        });
        body.endArray();
        rootBuilder.endObject();
        return stringBuilder.toString();
    }
}
