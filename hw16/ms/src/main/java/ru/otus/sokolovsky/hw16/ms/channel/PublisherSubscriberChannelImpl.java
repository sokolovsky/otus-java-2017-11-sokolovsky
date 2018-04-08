package ru.otus.sokolovsky.hw16.ms.channel;

import ru.otus.sokolovsky.hw16.integration.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PublisherSubscriberChannelImpl extends AbstractHandledChannel implements PublisherSubscriberChannel {
    private List<Consumer<Message>> handlers = new ArrayList<>();

    public PublisherSubscriberChannelImpl(String name) {
        super(name);
    }

    @Override
    protected void notifyHandlers(Message message) {
        handlers.forEach(handler -> handler.accept(message));
    }

    @Override
    public void registerHandler(Consumer<Message> handler) {
        handlers.add(handler);
    }
}
