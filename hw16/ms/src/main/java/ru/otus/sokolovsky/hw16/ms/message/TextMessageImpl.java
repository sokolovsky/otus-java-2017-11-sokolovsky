package ru.otus.sokolovsky.hw16.ms.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TextMessageImpl implements TextMessage {
    private Map<String, String> headers = new HashMap<>();
    private String body;
    private String destination;

    TextMessageImpl(String destination) {
        this.destination = destination;
    }

    @Override
    public String getDestination() {
        return destination;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    @Override
    public byte[] getBody() {
        String body = this.body;
        if (body == null) {
            return new byte[0];
        }
        return body.getBytes();
    }

    public void setBody(byte[] bytes) {
        body = new String(bytes);
    }

    public String getBodyAsString() {
        return body;
    }
}
