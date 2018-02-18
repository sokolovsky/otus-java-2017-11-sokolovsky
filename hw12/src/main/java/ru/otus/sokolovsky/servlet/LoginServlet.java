package ru.otus.sokolovsky.servlet;

import ru.otus.sokolovsky.db.Accounts;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends RenderedServlet {

    private Map<String, String> accounts;

    private static final String LOGIN_PARAM = "login";
    private static final String PASSWORD_PARAM = "pass";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utils.responseOk(resp);
        render(resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(LOGIN_PARAM);
        String pass = generateHash(req.getParameter(PASSWORD_PARAM));

        if (Accounts.instance.hasPassword(login, pass)) {
            req.getSession().setAttribute("login", login);
            resp.sendRedirect("/");
            return;
        }

        render(resp.getWriter(), new HashMap<String, Object>() {{
            put("error", "User with sent data doesn't exist");
        }});
        Utils.responseOk(resp);
    }

    private String generateHash(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest((pass + "some salt").getBytes());
            return DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
