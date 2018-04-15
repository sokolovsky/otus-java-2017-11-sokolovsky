package ru.otus.sokolovsky.hw16.web.servlet;

import ru.otus.sokolovsky.hw16.integration.message.MessageFactory;
import ru.otus.sokolovsky.hw16.integration.message.ParametrizedMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class LoginServlet extends RenderedServlet {

    private static final String LOGIN_PARAM = "login";
    private static final String PASSWORD_PARAM = "password";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utils.responseOk(resp);
        render(resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter(LOGIN_PARAM);
        String pass = Utils.generateHash(req.getParameter(PASSWORD_PARAM));
        if (Objects.isNull(login)) {
            render(resp.getWriter(), new HashMap<String, Object>() {{
                put("error", "Login is needed not empty");
            }});
            Utils.responseOk(resp);
            return;
        }

        if (hasPassword(login, pass)) {
            req.getSession().setAttribute("login", login);
            resp.sendRedirect("/chat");
            return;
        }

        render(resp.getWriter(), new HashMap<String, Object>() {{
            put("error", "User with sent data doesn't exist");
        }});
        Utils.responseOk(resp);
    }

    private boolean hasPassword(String login, String pass) {
        ParametrizedMessage message = MessageFactory.createRequestResponseMessage("DB", "check-user");
        message.setParameter("login", login);
        message.setParameter("password", pass);
        ParametrizedMessage response = null;
        try {
            response = (ParametrizedMessage) getMessageSystemConnector()
                    .sendMessageAndWaitResponse(message, 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        if (response == null) {
            return false;
        }
        String exist = response.getParameters().get("exist");
        return exist.equals("1");
    }
}
