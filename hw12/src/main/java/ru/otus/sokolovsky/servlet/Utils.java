package ru.otus.sokolovsky.servlet;

import javax.servlet.http.HttpServletResponse;

abstract public class Utils {
    static void responseOk(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
