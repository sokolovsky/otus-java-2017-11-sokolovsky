package ru.otus.sokolovsky.hw15.servlet;

import org.springframework.beans.factory.annotation.Configurable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Configurable
public class ChatInitServlet extends RenderedServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utils.responseOk(resp);
        String login = (String) req.getSession().getAttribute("login");
        render(resp.getWriter(), new HashMap<String, Object>() {{
            put("login", login);
        }});
    }
}
