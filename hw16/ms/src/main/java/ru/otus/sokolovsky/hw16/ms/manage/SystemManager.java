package ru.otus.sokolovsky.hw16.ms.manage;

import ru.otus.sokolovsky.hw16.ms.channel.*;
import ru.otus.sokolovsky.hw16.ms.message.Message;

import java.util.logging.Logger;

public class SystemManager {
    public final String CONTROL_CHANNEL_NAME = "control";
    private static final Logger logger = Logger.getLogger(SystemManager.class.getName());
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
        logger.info(String.format("Channel %s was added", channel.getName()));
        return channel;
    }

    public PointToPointChannel createPointToPointChannel(String name) {
        PointToPointChannel channel = channelFactory.createPointToPointChannel(name);
        channelContainer.addNamedChannel(channel);
        logger.info(String.format("Channel %s was added", channel.getName()));
        return channel;
    }

    public Channel getNamedChannel(String name) {
        return channelContainer.getNamedChannel(name);
    }

    public boolean hasNamedChannel(String name) {
        return null != channelContainer.getNamedChannel(name);
    }

    public void initService() {
        controlChannel.registerHandler(this::routeMessage);
    }

    public void routeMessage(Message message) {
        String destinationChannelName = message.getDestination();
        if (!hasNamedChannel(destinationChannelName)) {
            throw new IllegalStateException("Channel is not exist " + destinationChannelName);
        }
        getNamedChannel(destinationChannelName).addMessage(message);
    }
}
