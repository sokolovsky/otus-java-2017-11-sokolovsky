package ru.otus.sokolovsky.hw16.integration.message;

import java.util.HashMap;
import java.util.Map;

public class ParametrizedMessageImpl extends AbstractMessage implements ParametrizedMessage {
    private Map<String, String> parameters = new HashMap<>();

    ParametrizedMessageImpl(String destination, String name, MessageTypes type) {
        super(destination, name, type);
    }

    @Override
    public byte[] getBody() {
        return new byte[0];
    }

    public void setParameter(String name, String value) {
        parameters.put(name, value);
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }
}
