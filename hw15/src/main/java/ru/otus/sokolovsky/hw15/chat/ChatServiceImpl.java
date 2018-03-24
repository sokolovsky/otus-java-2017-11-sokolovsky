package ru.otus.sokolovsky.hw15.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Lazy;
import ru.otus.l151.messageSystem.Address;
import ru.otus.sokolovsky.hw15.db.ChatMessageDataSet;
import ru.otus.sokolovsky.hw15.db.HandleChatMessage;
import ru.otus.sokolovsky.hw15.domain.ChatService;
import ru.otus.sokolovsky.hw15.domain.MessageSystemContext;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ChatServiceImpl implements ChatService {

    private ChatServer chatServer;

    private Address address = new Address("frontend");
    private MessageSystemContext msContext;

    public ChatServiceImpl(ChatServer chatServer) {
        this.chatServer = chatServer;
        chatServer.registerMessageHandler(this::handleRequest);
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
        Address dbAddress = msContext.getAddress(MessageSystemContext.AddressType.DB);
        String login = (String) map.get("login");
        ChatMessageDataSet chatMessage = new ChatMessageDataSet();
        chatMessage.setText((String) map.get("message"));
        chatMessage.setTime(LocalDateTime.now());
        msContext.send(new HandleChatMessage(getAddress(), dbAddress, login, chatMessage));
    }

    @Override
    public void pushMessage(ChatMessageDataSet message) {
        Gson gson = new GsonBuilder().create();
        Map<String, String> map = new HashMap<String, String>() {{
            put("login", message.getAuthor().getLogin());
            put("time", message.getTime().toString());
            put("message", message.getText());
        }};
        System.out.println("Response: " + gson.toJson(new HashMap<>(map)));
        chatServer.sendAll(gson.toJson(new HashMap<>(map)));
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
