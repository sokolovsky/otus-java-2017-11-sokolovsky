package ru.otus.sokolovsky.hw15.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.otus.l151.messageSystem.Address;
import ru.otus.l151.messageSystem.MessageSystemContext;
import ru.otus.sokolovsky.hw15.domain.AddressTypes;
import ru.otus.sokolovsky.hw15.domain.ChatMessage;
import ru.otus.sokolovsky.hw15.domain.ChatService;
import ru.otus.sokolovsky.hw15.domain.ServiceMessageFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public abstract class ChatServiceImpl implements ChatService {

    private ChatServer chatServer;
    private ServiceMessageFactory serviceMessageFactory;

    private Address address = new Address("frontend");
    private MessageSystemContext msContext;

    protected abstract ChatMessage createChatMessage();

    public ChatServiceImpl(ChatServer chatServer, ServiceMessageFactory serviceMessageFactory) {
        this.chatServer = chatServer;
        this.serviceMessageFactory = serviceMessageFactory;
        chatServer.registerMessageHandler(this::handleRequest);
        chatServer.registerConnectionHandler(this::handleNewConnection);
    }

    private void handleNewConnection(String login) {
        System.out.println("To handle connection with: " + login);
        Address dbAddress = msContext.getAddress(AddressTypes.DB.getName());
        msContext.send(serviceMessageFactory.createGetLastMessagesMessage(getAddress(), dbAddress, login));
    }

    @Override
    public void setMessageSystemContext(MessageSystemContext msContext) {
        this.msContext = msContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleRequest(String json) {
        System.out.println("To handle request: " + json);
        Gson gson = new GsonBuilder().create();
        Map<String, Object> map = gson.fromJson(json, Map.class);
        Address dbAddress = msContext.getAddress(AddressTypes.DB.getName());
        String login = (String) map.get("login");
        ChatMessage chatMessage = createChatMessage();
        chatMessage.setText((String) map.get("message"));
        chatMessage.setTime(LocalDateTime.now());
        msContext.send(serviceMessageFactory.createReceiveChatMessage(getAddress(), dbAddress, login, chatMessage));
    }

    private String messageToJson(ChatMessage message) {
        Gson gson = new GsonBuilder().create();
        Map<String, String> map = new HashMap<String, String>() {{
            put("login", message.getAuthor());
            put("time", message.getTime().toString());
            put("message", message.getText());
        }};
        // Gson has one bug with serialize of maps
        return gson.toJson(new HashMap<>(map));
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
    public Address getAddress() {
        return address;
    }
}
