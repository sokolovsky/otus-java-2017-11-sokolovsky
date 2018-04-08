package ru.otus.sokolovsky.hw16.ms.channel;

import ru.otus.sokolovsky.hw16.integration.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PointToPointChannelImpl extends AbstractHandledChannel implements PointToPointChannel {
    private int nowTurn = 0;
    private List<Consumer<Message>> handlers = new ArrayList<>();

    public PointToPointChannelImpl(String name) {
        super(name);
    }

    @Override
    protected void notifyHandlers(Message message) {
        if (handlers.size() == 0) {
            return;
        }
        Consumer<Message> handler = handlers.get(getTurn());
        handler.accept(message);
        setupNextTurn();
    }

    private void setupNextTurn() {
        nowTurn = (nowTurn + 1) % handlers.size();
    }

    private int getTurn() {
        return nowTurn;
    }

    @Override
    public void registerHandler(Consumer<Message> handler) {
        handlers.add(handler);
    }
}
