package ru.otus.sokolovsky.hw16.integration.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListParametrizedMessageImpl extends AbstractMessage implements ListParametrizedMessage {

    private List<Map<String, String>> list;

    @Override
    public void setList(List<Map<String, String>> list) {
        this.list = list;
    }

    ListParametrizedMessageImpl(String destination, String name, MessageType type) {
        super(destination, name, type);
    }

    @Override
    public List<Map<String, String>> getList() {
        return list;
    }

    @Override
    public byte[] getBody() {
        return new byte[0];
    }
}
