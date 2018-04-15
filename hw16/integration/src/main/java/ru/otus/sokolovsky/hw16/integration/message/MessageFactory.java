package ru.otus.sokolovsky.hw16.integration.message;

import ru.otus.sokolovsky.hw16.integration.control.ServiceAction;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MessageFactory {
    public static Message generateReplyMessage(Message message) {
        try {
            Constructor<? extends Message> constructor = message.getClass().getDeclaredConstructor(String.class, String.class, MessageType.class);
            Message replyMessage = constructor.newInstance(message.getSource(), message.getName(), message.getType());
            message.getHeaders().forEach(replyMessage::setHeader);
            return replyMessage;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static ParametrizedMessage createControlMessage(ServiceAction action) {
        return new ParametrizedMessageImpl("service", action.getName(), MessageType.COMMAND_MESSAGE);
    }
}
