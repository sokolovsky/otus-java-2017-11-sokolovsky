package ru.otus.sokolovsky.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import ru.otus.sokolovsky.db.Accounts;
import ru.otus.sokolovsky.filters.AuthFilter;
import ru.otus.sokolovsky.renderer.Rendered;
import ru.otus.sokolovsky.renderer.Renderer;
import ru.otus.sokolovsky.servlet.CacheViewServlet;
import ru.otus.sokolovsky.servlet.LoginServlet;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


/**
     ДЗ-12: Веб сервер
     Встроить веб сервер в приложение из ДЗ-11.
     Сделать админскую страницу, на которой админ должен авторизоваться и получить доступ к параметрам и состоянию кэша.
 */
public class App {
    private static final Renderer renderer = new Renderer("templates/layout.ftl");

    private final static Map<String, ServletHolder> siteMap = new HashMap<String, ServletHolder>() {{
        put("/login", forConfigure(new LoginServlet()).toDo((s) -> {
                Rendered r = (Rendered) s;
                r.setRenderer(renderer);
                r.setTemplate("/templates/login.ftl".replace("/", File.separator));
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
                r.setTemplate("templates/cache_view.ftl".replace("/", File.separator));
            }).withHolder(h -> {
                h.setInitParameter("dirAllowed","true");
            }).getHolder());
    }};

    public static void main(String[] args) throws Exception {
        initAccounts();

        Resource assets = Resource.newResource(resourcePath("assets"));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setBaseResource(assets);
        siteMap.forEach((k, v) -> context.addServlet(v, k));
        context.addFilter(AuthFilter.class, "/", EnumSet.of(DispatcherType.REQUEST));

        Server server = new Server(Integer.parseInt(property("port")));
        server.setHandler(context);

        server.start();
        server.join();
    }

    private static void initAccounts() {
        String users = property("users");
        String[] usersArray = users.split(",\\s*");
        Arrays.stream(usersArray)
                .map(s -> s
                        .replaceAll("]", "")
                        .replaceAll("\\[", ""))
                .forEach(s -> {
                    List<String> user = Arrays.stream(s.split("\\|")).map(String::trim).collect(Collectors.toList());
                    Accounts.instance.add(user.get(0), user.get(1));
                });
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
