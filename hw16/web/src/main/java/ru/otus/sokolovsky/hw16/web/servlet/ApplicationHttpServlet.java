package ru.otus.sokolovsky.hw16.web.servlet;

import ru.otus.sokolovsky.hw16.integration.client.Connector;

import javax.servlet.http.HttpServlet;

public abstract class ApplicationHttpServlet extends HttpServlet {
    private Connector msConnector;

    public void setMessageSystemConnector(Connector connector) {
        msConnector = connector;
    }

    public Connector getMessageSystemConnector() {
        return msConnector;
    }
}
