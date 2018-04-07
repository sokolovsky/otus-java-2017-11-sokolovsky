package ru.otus.sokolovsky.hw16.ms.channel;

public interface ChannelContainer {
    void addNamedChannel(ChannelImpl channel);

    void addPrivateChannel(ChannelImpl channel, Object owner);

    Channel getPrivateChannel(Object owner);

    Channel getNamedChannel(String name);
}
