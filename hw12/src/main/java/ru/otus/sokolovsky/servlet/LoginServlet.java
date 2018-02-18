package ru.otus.sokolovsky.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class LoginServlet extends RenderedServlet {

    private Map<String, String> accounts;

    public void useAccounts(Map<String, String> accounts) {
        this.accounts = accounts;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utils.responseOk(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Utils.responseOk(resp);
    }

    String generateHash(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] digest = md.digest((pass + "some salt").getBytes());
            return new String(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
