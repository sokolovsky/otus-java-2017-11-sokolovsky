package ru.otus.sokolovsky.hw12.servlet;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

abstract public class Utils {
    static void responseOk(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public static String generateHash(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest((pass + "some salt").getBytes());
            return DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
