package ru.otus.sokolovsky.hw16.integration.message;

import org.json.JSONObject;
import org.json.JSONWriter;

import java.util.Iterator;

public class MessageTransformer {
    public static ParametrizedMessage fromJson(String json) throws IllegalFormatException {
        JSONObject rootJson = new JSONObject(json);
        JSONObject headers = rootJson.getJSONObject("headers");
        JSONObject body = rootJson.getJSONObject("body");
        String destination = rootJson.optString("destination");
        String source = rootJson.optString("source");
        String name = rootJson.optString("name");
        ParametrizedMessage message;
        try {
            String type = rootJson.getString("type");
            MessageType messageType = MessageType.byCode(type);
            message = new ParametrizedMessageImpl(destination, name, messageType);
            message.setSource(source);
        } catch (IllegalArgumentException e) {
            throw new IllegalFormatException(e);
        }

        for (Iterator<String> it = headers.keys(); it.hasNext(); ) {
            String key = it.next();
            message.setHeader(key, headers.getString(key));
        }

        for (Iterator<String> it = body.keys(); it.hasNext(); ) {
            String key = it.next();
            message.setParameter(key, body.getString(key));
        }
        return message;
    }

    public static String toJson(ParametrizedMessage message) {
        StringBuilder stringBuilder = new StringBuilder();
        JSONWriter rootBuilder = new JSONWriter(stringBuilder);
        rootBuilder.object();
        rootBuilder.key("type").value(message.getType().getCode());
        rootBuilder.key("destination").value(message.getDestination());
        rootBuilder.key("source").value(message.getSource());
        rootBuilder.key("name").value(message.getName());
        JSONWriter headers = rootBuilder.key("headers").object();
        message.getHeaders().forEach((k, v) -> {
            headers.key(k).value(v);
        });
        headers.endObject();

        JSONWriter body = rootBuilder.key("body").object();
        message.getParameters().forEach((n, v) -> {
            body.key(n).value(v);
        });
        body.endObject();
        rootBuilder.endObject();
        return stringBuilder.toString();
    }
}
