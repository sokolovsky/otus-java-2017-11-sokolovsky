package ru.otus.sokolovsky.hw16.integration.message;

import ru.otus.sokolovsky.hw16.integration.control.ServiceAction;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MessageFactory {
    public static Message generateReplyMessage(Message message) {
        return generateReplyMessage(message, message.getClass());
    }

    public static Message generateReplyMessage(Message message, Class<? extends Message> clazz) {
        try {
            Constructor<? extends Message> constructor = clazz.getDeclaredConstructor(String.class, String.class, MessageType.class);
            Message replyMessage = constructor.newInstance(message.getSource(), message.getName(), message.getType());
            replyMessage.setSource(message.getDestination());
            message.getHeaders().forEach(replyMessage::setHeader);
            return replyMessage;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static ListParametrizedMessage genarateListReplyMessage(Message message) {
        return (ListParametrizedMessage) generateReplyMessage(message, ListParametrizedMessageImpl.class);
    }

    public static ParametrizedMessage createControlMessage(ServiceAction action) {
        return new ParametrizedMessageImpl("service", action.getName(), MessageType.COMMAND_MESSAGE);
    }

    public static ParametrizedMessage createRequestResponseMessage(String dest, String name) {
        return new ParametrizedMessageImpl(dest, name, MessageType.REQUEST_REPLY_MESSAGE);
    }
}
