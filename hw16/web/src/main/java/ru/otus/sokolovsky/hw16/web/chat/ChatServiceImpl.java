package ru.otus.sokolovsky.hw16.web.chat;

import org.json.JSONObject;
import org.json.JSONWriter;
import ru.otus.sokolovsky.hw16.integration.client.Connector;
import ru.otus.sokolovsky.hw16.integration.control.ChannelType;
import ru.otus.sokolovsky.hw16.integration.control.ServiceAction;
import ru.otus.sokolovsky.hw16.integration.message.ListParametrizedMessage;
import ru.otus.sokolovsky.hw16.integration.message.MessageFactory;
import ru.otus.sokolovsky.hw16.integration.message.ParametrizedMessage;

import java.time.LocalDateTime;
import java.util.Map;

public class ChatServiceImpl implements ChatService {

    private ChatServer chatServer;
    private Connector msConnector;

    public ChatServiceImpl(ChatServer chatServer, Connector connector) {
        this.chatServer = chatServer;
        msConnector = connector;
        chatServer.registerMessageHandler(this::handleRequest);
        chatServer.registerConnectionHandler(this::handleNewConnection);
    }

    private void handleNewConnection(String login) {
        System.out.println("To handle connection with: " + login);
        ParametrizedMessage message = MessageFactory.createRequestResponseMessage("DB", "get-last-messages");
        try {
            ListParametrizedMessage response = (ListParametrizedMessage) msConnector.sendMessageAndWaitResponse(message, 30);
            response.getList().forEach(obj -> {
                ChatMessage chatMessage = new ChatMessage(
                        obj.get("login"),
                        obj.get("time"),
                        obj.get("text")
                );
                pushMessage(chatMessage);
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleRequest(String json) {
        System.out.println("To handle request: " + json);
        JSONObject rootJson = new JSONObject(json);
        ParametrizedMessage toDBMessage = MessageFactory.createRequestResponseMessage("DB", "register-new-message");
        toDBMessage.setParameter("login", rootJson.getString("login"));
        toDBMessage.setParameter("text", rootJson.getString("message"));
        msConnector.sendMessage(toDBMessage);

        ParametrizedMessage toChat = MessageFactory.createRequestResponseMessage("CHAT_BROADCAST", "new-message");
        toChat.setParameter("login", rootJson.getString("login"));
        toChat.setParameter("text", rootJson.getString("message"));
        toChat.setParameter("time", LocalDateTime.now().toString());
        msConnector.sendMessage(toChat);
    }

    private String messageToJson(ChatMessage message) {
        StringBuilder stringBuilder = new StringBuilder();
        JSONWriter rootBuilder = new JSONWriter(stringBuilder);
        rootBuilder.object();
        rootBuilder.key("login").value(message.getAuthor());
        rootBuilder.key("time").value(message.getTime());
        rootBuilder.key("message").value(message.getText());
        rootBuilder.endObject();
        return stringBuilder.toString();
    }

    @Override
    public void pushMessage(ChatMessage message) {
        chatServer.sendAll(messageToJson(message));
    }

    @Override
    public void pushMessage(ChatMessage message, String destination) {
        chatServer.send(messageToJson(message), destination);
    }

    @Override
    public void start() {
        ParametrizedMessage message = MessageFactory.createControlMessage(ServiceAction.SUBSCRIBE_ON_CHANNEL);
        message.setParameter("channel", "CHAT_BROADCAST");
        message.setParameter("channelType", ChannelType.PUBLISHER_SUBSCRIBER.name());
        msConnector.sendMessage(message);
        msConnector.addChannelMessageHandler("CHAT_BROADCAST", (m) -> {
            ParametrizedMessage pMessage = (ParametrizedMessage) m;
            Map<String, String> parameters = pMessage.getParameters();
            ChatMessage chatMessage = new ChatMessage(
                    parameters.get("login"),
                    parameters.get("time"),
                    parameters.get("text")
            );
            pushMessage(chatMessage);
        });
        chatServer.listen();
    }

    @Override
    public void close() {
        chatServer.close();
    }
}
