package ru.otus.sokolovsky.hw16.ms.service;

import ru.otus.sokolovsky.hw16.integration.control.ServiceAction;
import ru.otus.sokolovsky.hw16.ms.channel.Channel;
import ru.otus.sokolovsky.hw16.ms.manage.SystemManager;
import ru.otus.sokolovsky.hw16.integration.message.Message;
import ru.otus.sokolovsky.hw16.integration.message.MessageType;
import ru.otus.sokolovsky.hw16.integration.message.ParametrizedMessage;

public class ServiceHandler {
    private Channel channel;
    private SystemManager systemManager;

    public ServiceHandler(Channel channel, SystemManager systemManager) {
        this.channel = channel;
        this.systemManager = systemManager;
    }

    public void init() {
        channel.registerHandler(this::handleMessage);
    }

    private void handleMessage(Message message) {
        if (message.getType() != MessageType.COMMAND_MESSAGE) {
            return;
        }
        ParametrizedMessage pMessage = (ParametrizedMessage) message;

        ServiceAction action = ServiceAction.getByName(message.getName());
        if (action == null) {
            throw new IllegalStateException("Message needs to have right name");
        }

        String channelName = pMessage.getParameters().get("channel");
        String source = pMessage.getSource();
        switch (action) {
            case SUBSCRIBE_ON_CHANNEL:
                Channel channel = systemManager.getNamedChannel(channelName);
                Channel sourceChannel = systemManager.getNamedChannel(source);
                channel.registerHandler(sourceChannel::addMessage);
                break;
            case CREATE_NEW_NAMED_CHANNEL:
                if (!systemManager.hasNamedChannel(channelName)) {
                    systemManager.createPointToPointChannel(channelName);
                }
                break;
        }
    }
}
