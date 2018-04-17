package ru.otus.sokolovsky.hw16.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class ChatInitServlet extends RenderedServlet {
    private int webSocketPort;

    public void setWebSocketPort(int port) {
        webSocketPort = port;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utils.responseOk(resp);
        String login = (String) req.getSession().getAttribute("login");
        render(resp.getWriter(), new HashMap<String, Object>() {{
            put("login", login);
            put("wsPort", "" + webSocketPort);
        }});
    }
}
