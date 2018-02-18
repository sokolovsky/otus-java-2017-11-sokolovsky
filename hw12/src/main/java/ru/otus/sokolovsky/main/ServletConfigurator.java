package ru.otus.sokolovsky.main;

import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import java.util.function.Consumer;

public class ServletConfigurator {
    private HttpServlet servlet;
    private ServletHolder holder;

    public ServletConfigurator(HttpServlet servlet) {
        this.servlet = servlet;
    }

    public ServletConfigurator toDo(Consumer<HttpServlet> action) {
        action.accept(servlet);
        return this;
    }

    public ServletConfigurator withHolder(Consumer<ServletHolder> action) {
        action.accept(getHolder());
        return this;
    }

    public HttpServlet getServlet() {
        return servlet;
    }

    public ServletHolder getHolder() {
        if (holder == null) {
            holder = new ServletHolder(servlet);
        }
        return holder;
    }
}
