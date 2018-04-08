package ru.otus.sokolovsky.hw16.integration.message;

import java.util.Arrays;
import java.util.Optional;

public enum MessageTypes {
    EVENT_MESSAGE("event"),
    REQUEST_REPLY_MESSAGE("request-reply"),
    COMMAND_MESSAGE("command");

    private String code;

    MessageTypes(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static MessageTypes byCode(String code) {
        Optional<MessageTypes> first = Arrays.stream(values()).filter(mt -> mt.code.equals(code)).findFirst();
        if (!first.isPresent()) {
            throw new IllegalArgumentException("Code is not present");
        }
        return first.get();
    }
}
