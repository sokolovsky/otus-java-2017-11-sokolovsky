package ru.otus.sokolovsky.hw16.integration.message;

import java.util.List;
import java.util.Map;

public interface ListParametrizedMessage extends Message {
    void setList(List<Map<String, String>> list);

    List<Map<String, String>> getList();
}
