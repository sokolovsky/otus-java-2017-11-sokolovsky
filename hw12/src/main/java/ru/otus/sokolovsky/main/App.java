package ru.otus.sokolovsky.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import ru.otus.sokolovsky.renderer.Rendered;
import ru.otus.sokolovsky.renderer.Renderer;
import ru.otus.sokolovsky.servlet.CacheViewServlet;
import ru.otus.sokolovsky.servlet.LoginServlet;
import ru.otus.sokolovsky.servlet.RenderedServlet;

import javax.servlet.http.HttpServlet;
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
                Rendered r = (Rendered) s;
                r.setRenderer(renderer);
                r.setTemplate(resourcePath("/templates/login.html".replace("/", File.separator)));
            }).getHolder());

        put("/static/*", forConfigure(new DefaultServlet()).withHolder(h -> {
            h.setName("static");
            h.setInitParameter("resourceBase", resourcePath("assets").getPath());
            h.setInitParameter("dirAllowed","true");
            h.setInitParameter("pathInfoOnly","true");
        }).getHolder());

        put("/", forConfigure(new CacheViewServlet()).toDo(s -> {
                Rendered r = (Rendered) s;
                r.setRenderer(renderer);
                r.setTemplate(resourcePath("/templates/cache_view.html".replace("/", File.separator)));
            }).withHolder(h -> {
                h.setInitParameter("dirAllowed","true");
            }).getHolder());
    }};

    public static void main(String[] args) throws Exception {
        Resource assets = Resource.newResource(resourcePath("assets"));

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setBaseResource(assets);
        siteMap.forEach((k, v) -> context.addServlet(v, k));

        Server server = new Server(Integer.parseInt(property("port")));
        server.setHandler(context);

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

    private static ServletConfigurator forConfigure(HttpServlet servlet) {
        return new ServletConfigurator(servlet);
    }
}
