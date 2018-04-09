package ru.otus.sokolovsky.hw16.ms.channel;

public class ChannelFactoryImpl implements ChannelFactory {
    @Override
    public PointToPointChannel createPointToPointChannel(String name) {
        return new PointToPointChannelImpl(name);
    }

    @Override
    public PublisherSubscriberChannel createPublisherSubscriberChannel(String name) {
        return new PublisherSubscriberChannelImpl(name);
    }
}
