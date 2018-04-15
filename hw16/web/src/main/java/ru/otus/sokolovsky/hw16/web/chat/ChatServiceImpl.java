package ru.otus.sokolovsky.hw16.web.chat;

import org.json.JSONObject;
import org.json.JSONWriter;
import ru.otus.sokolovsky.hw16.integration.client.Connector;
import ru.otus.sokolovsky.hw16.integration.control.ServiceAction;
import ru.otus.sokolovsky.hw16.integration.message.Message;
import ru.otus.sokolovsky.hw16.integration.message.MessageFactory;
import ru.otus.sokolovsky.hw16.integration.message.ParametrizedMessage;

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
            Message response = msConnector.sendMessageAndWaitResponse(message, 30);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleRequest(String json) {
        System.out.println("To handle request: " + json);
        JSONObject rootJson = new JSONObject(json);
        ParametrizedMessage parametrizedMessage = MessageFactory.createRequestResponseMessage("DB", "register-new-message");
        parametrizedMessage.setParameter("login", rootJson.getString("login"));
        parametrizedMessage.setParameter("text", rootJson.getString("message"));
        msConnector.sendMessage(parametrizedMessage);
    }

    private String messageToJson(ChatMessage message) {
        StringBuilder stringBuilder = new StringBuilder();
        JSONWriter rootBuilder = new JSONWriter(stringBuilder);
        rootBuilder.object();
        rootBuilder.key("login").value(message.getAuthor());
        rootBuilder.key("time").value(message.getTime());
        rootBuilder.key("message").value(message.getText());
        rootBuilder.endObject();
        return rootBuilder.toString();
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
        msConnector.sendMessage(message);
        msConnector.addMessageHandler((m) -> {
            ParametrizedMessage pMessage = (ParametrizedMessage) m;
            Map<String, String> parameters = pMessage.getParameters();
            ChatMessage chatMessage = new ChatMessage(
                    parameters.get("author"),
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
