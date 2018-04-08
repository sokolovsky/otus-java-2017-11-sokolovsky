package ru.otus.sokolovsky.hw16.integration.message;

import org.json.JSONObject;
import org.json.JSONWriter;

import java.util.Iterator;

public class MessageTransformer {
    public static ParametrizedMessage fromJson(String json) throws IllegalFormatException {
        JSONObject rootJson = new JSONObject(json);
        JSONObject headers = rootJson.optJSONObject("headers");
        JSONObject body = rootJson.optJSONObject("body");
        String destination = rootJson.getString("destination");
        String name = rootJson.getString("name");
        ParametrizedMessage message;
        try {
            String type = rootJson.getString("type");
            MessageTypes messageType = MessageTypes.byCode(type);
            message = new ParametrizedMessageImpl(destination, name, messageType);
        } catch (IllegalArgumentException e) {
            throw new IllegalFormatException(e);
        }

        for (Iterator<String> it = headers.keys(); it.hasNext(); ) {
            String key = it.next();
            message.setHeader(key, headers.getString(key));
        }

        for (Iterator<String> it = body.keys(); it.hasNext(); ) {
            String key = it.next();
            message.setParameter(key, headers.getString(key));
        }

        return message;
    }

    public static String toJson(ParametrizedMessage message) {
        StringBuilder stringBuilder = new StringBuilder();
        JSONWriter rootBuilder = new JSONWriter(stringBuilder);
        rootBuilder.key("type").value(message.getType().getCode());
        rootBuilder.key("destination").value(message.getDestination());
        rootBuilder.key("source").value(message.getSource());
        JSONWriter headers = rootBuilder.key("headers");
        message.getHeaders().forEach((k, v) -> {
            headers.key(k).value(v);
        });
        JSONWriter body = rootBuilder.key("body");
        message.getParameters().forEach((n, v) -> {
            body.key(n).value(v);
        });
        return rootBuilder.toString();
    }
}
