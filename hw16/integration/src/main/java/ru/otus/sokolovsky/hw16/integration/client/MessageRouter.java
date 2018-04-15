package ru.otus.sokolovsky.hw16.integration.client;

import ru.otus.sokolovsky.hw16.integration.message.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MessageRouter implements Consumer<Message> {

    private Map<String, Consumer<Message>> handlers = new HashMap<>();

    @Override
    public void accept(Message message) {
        String action = message.getName();
        if (!handlers.containsKey(action)) {
            return;
        }
        handlers.get(action).accept(message);
    }

    public void registerActionHandler(String action, Consumer<Message> handler) {
        handlers.put(action, handler);
    }

    public void setActionHandlers(Map<String, Consumer<Message>> handlers) {
        this.handlers = handlers;
    }
}
