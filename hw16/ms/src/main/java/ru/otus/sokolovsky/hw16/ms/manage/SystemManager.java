package ru.otus.sokolovsky.hw16.ms.manage;

import ru.otus.sokolovsky.hw16.integration.message.Message;
import ru.otus.sokolovsky.hw16.ms.channel.*;

import java.util.logging.Logger;

public class SystemManager {
    private static final Logger logger = Logger.getLogger(SystemManager.class.getName());
    private ChannelContainer channelContainer;
    private ChannelFactory channelFactory;

    public SystemManager(ChannelContainer channelContainer, ChannelFactory channelFactory) {
        this.channelContainer = channelContainer;
        this.channelFactory = channelFactory;
    }

    private void registerChannel(Channel channel) {
        channelContainer.addNamedChannel(channel);
        logger.info(String.format("Channel %s was registered", channel.getName()));
    }

    public PublisherSubscriberChannel createPublisherSubscriberChannel(String name) {
        PublisherSubscriberChannel channel = channelFactory.createPublisherSubscriberChannel(name);
        registerChannel(channel);
        return channel;
    }

    public PointToPointChannel createPointToPointChannel(String name) {
        PointToPointChannel channel = channelFactory.createPointToPointChannel(name);
        registerChannel(channel);
        return channel;
    }

    public Channel getNamedChannel(String name) {
        return channelContainer.getNamedChannel(name);
    }

    public boolean hasNamedChannel(String name) {
        return null != channelContainer.getNamedChannel(name);
    }

    public void routeMessage(Message message) {
        String destinationChannelName = message.getDestination();
        if (!hasNamedChannel(destinationChannelName)) {
            throw new IllegalStateException("Channel is not exist " + destinationChannelName);
        }
        getNamedChannel(destinationChannelName).addMessage(message);
    }
}
