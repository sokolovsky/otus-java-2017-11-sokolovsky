package ru.otus.sokolovsky.main;

import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.sokolovsky.servlet.RenderedServlet;

import java.util.function.Consumer;

public class ServletConfigurator {
    private RenderedServlet servlet;

    public ServletConfigurator(RenderedServlet servlet) {
        this.servlet = servlet;
    }

    public ServletConfigurator toDo(Consumer<RenderedServlet> action) {
        action.accept(servlet);
        return this;
    }

    public RenderedServlet getServlet() {
        return servlet;
    }

    public ServletHolder createHolder() {
        return new ServletHolder(servlet);
    }
}
