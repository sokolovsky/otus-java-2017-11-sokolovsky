package ru.otus.sokolovsky.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.sokolovsky.renderer.Renderer;
import ru.otus.sokolovsky.servlet.CacheViewServlet;
import ru.otus.sokolovsky.servlet.LoginServlet;
import ru.otus.sokolovsky.servlet.RenderedServlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class App {
    private static final Renderer renderer = new Renderer(resourcePath("/templates/layout.html"));

    private final static Map<String, ServletHolder> siteMap = new HashMap<String, ServletHolder>() {{
        put("/login", forConfigure(new LoginServlet()).toDo((s) -> {
                s.setRenderer(renderer);
                s.setTemplate(resourcePath("/templates/login.html"));
            }).createHolder());

        put("/", forConfigure(new CacheViewServlet()).toDo(s -> {
                s.setRenderer(renderer);
                s.setTemplate(resourcePath("/templates/cache_view.html"));
            }).createHolder());
    }};

    public static void main(String[] args) throws Exception {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(resourcePath("assets").getPath());

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        siteMap.forEach((k, v) -> context.addServlet(v, k));

        Server server = new Server(Integer.parseInt(property("port")));
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();
    }

    private static String property(String name) {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(resourcePath("project.properties").getFile())));
            return (String) properties.get(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static URL resourcePath(String path) {
        ClassLoader classLoader = App.class.getClassLoader();
        return classLoader.getResource(path);
    }

    private static ServletConfigurator forConfigure(RenderedServlet servlet) {
        return new ServletConfigurator(servlet);
    }
}
