package ru.otus.sokolovsky.hw16.ms.channel;

public interface ChannelContainer {
    void addNamedChannel(Channel channel);

    void addPrivateChannel(Channel channel, Object owner);

    Channel getPrivateChannel(Object owner);

    Channel getNamedChannel(String name);
}
