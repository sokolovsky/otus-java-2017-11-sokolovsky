package ru.otus.sokolovsky.hw16.ms.channel;

import ru.otus.sokolovsky.hw16.ms.message.Message;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractHandledChannel implements Channel {
    private Queue<Message> queue = new LinkedBlockingQueue<>();
    private Set<Message> polled = new HashSet<>();
    private String name;

    public AbstractHandledChannel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addMessage(Message message) {
        queue.add(message);
        notifyHandlers(message);
    }

    public boolean hasMessages() {
        return queue.size() > 0;
    }

    public Message poll() {
        Message message = queue.poll();
        polled.add(message);
        return message;
    }

    public void confirm(Message message) {
        polled.remove(message);
    }

    public int confirmWaited() {
        return polled.size();
    }

    protected abstract void notifyHandlers(Message message);
}
