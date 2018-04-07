package ru.otus.sokolovsky.hw16.ms.channel;

public interface ChannelFactory {
    PointToPointChannel createPointToPointChannel(String name);

    PublisherSubscriberChannel createPublisherSubscriberChannel(String name);
}
