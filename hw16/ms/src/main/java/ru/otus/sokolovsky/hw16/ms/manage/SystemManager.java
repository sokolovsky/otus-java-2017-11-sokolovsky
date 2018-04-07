package ru.otus.sokolovsky.hw16.ms.manage;

import ru.otus.sokolovsky.hw16.ms.channel.*;
import ru.otus.sokolovsky.hw16.ms.message.Message;

public class SystemManager {
    public final String CONTROL_CHANNEL_NAME = "control";
    private final PointToPointChannel controlChannel;
    private ChannelContainer channelContainer;
    private ChannelFactory channelFactory;

    public SystemManager(ChannelContainer channelContainer, ChannelFactory channelFactory) {
        this.channelContainer = channelContainer;
        this.channelFactory = channelFactory;
        controlChannel = createPointToPointChannel(CONTROL_CHANNEL_NAME);
        initService();
    }

    public PublisherSubscriberChannel createPublisherSubscriberChannel(String name) {
        PublisherSubscriberChannel channel = channelFactory.createPublisherSubscriberChannel(name);
        channelContainer.addNamedChannel(channel);
        return channel;
    }

    public PointToPointChannel createPointToPointChannel(String name) {
        PointToPointChannel channel = channelFactory.createPointToPointChannel(name);
        channelContainer.addNamedChannel(channel);
        return channel;
    }

    public Channel getNamedChannel(String name) {
        return channelContainer.getNamedChannel(name);
    }

    public boolean hasNamedChannel(String name) {
        return null != channelContainer.getNamedChannel(name);
    }

    public void initService() {
        controlChannel.registerHandler(this::receiveMessage);
    }

    private void receiveMessage(Message message) {
    }
}
