package ru.otus.sokolovsky.hw16.ms.channel;

import java.util.HashMap;
import java.util.Map;

public class ChannelContainerImpl implements ChannelContainer {
    private Map<String, Channel> namedChannels = new HashMap<>();
    private Map<Object, Channel> privateChannels = new HashMap<>();

    @Override

    public void addNamedChannel(Channel channel) {
        namedChannels.put(channel.getName(), channel);
    }

    @Override
    public void addPrivateChannel(Channel channel, Object owner) {
        privateChannels.put(owner, channel);
    }

    @Override
    public Channel getPrivateChannel(Object owner) {
        return privateChannels.get(owner);
    }

    @Override
    public Channel getNamedChannel(String name) {
        return namedChannels.get(name);
    }
}
