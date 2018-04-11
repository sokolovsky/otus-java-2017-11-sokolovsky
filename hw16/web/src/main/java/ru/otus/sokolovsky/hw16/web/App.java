package ru.otus.sokolovsky.hw16.web;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.StdErrLog;
import org.eclipse.jetty.util.resource.Resource;
import ru.otus.sokolovsky.hw16.web.cli.OptionsBuilder;
import ru.otus.sokolovsky.hw16.web.filters.AuthFilter;
import ru.otus.sokolovsky.hw16.web.renderer.Rendered;
import ru.otus.sokolovsky.hw16.web.renderer.Renderer;
import ru.otus.sokolovsky.hw16.web.renderer.RendererImpl;
import ru.otus.sokolovsky.hw16.web.servlet.ChatInitServlet;
import ru.otus.sokolovsky.hw16.web.servlet.LoginServlet;
import ru.otus.sokolovsky.hw16.web.servlet.ServletConfigurator;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class App {

    private static class Params {
        int webPort;
        int msPort;
    }

    // todo DI
    private static final Renderer renderer = new RendererImpl("templates/layout.ftl");

    private final static Map<String, ServletHolder> siteMap = new HashMap<>() {{
        put("/login", forConfigure(new LoginServlet()).toDo((s) -> {
            Rendered r = (Rendered) s;
            r.setRenderer(renderer);
            r.setTemplate("/templates/login.ftl".replace("/", File.separator));
        }).getHolder());

        put("/static/*", forConfigure(new DefaultServlet()).withHolder(h -> {
            h.setName("static");
            h.setInitParameter("resourceBase", resourcePath("assets").toExternalForm());
            h.setInitParameter("dirAllowed","true");
            h.setInitParameter("pathInfoOnly","true");
        }).getHolder());

        put("/", forConfigure(new ChatInitServlet()).toDo(s -> {
            Rendered r = (Rendered) s;
            r.setRenderer(renderer);
            r.setTemplate("templates/cache_view.ftl".replace("/", File.separator));
        }).withHolder(h -> {
            h.setInitParameter("dirAllowed","true");
        }).getHolder());
    }};

    public static void main(String[] args) throws Exception {
        // init web service with port and message system port

        Params cliParams = handleParams(args);
        launchWebServer(cliParams.webPort);
        connectWithMessageSystem(cliParams.msPort);
    }

    private static void connectWithMessageSystem(int msPort) {
        // todo organize connection
    }

    private static void launchWebServer(int webPort) throws Exception {

        Resource assets = Resource.newResource(resourcePath("assets").toExternalForm());
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setBaseResource(assets);
        siteMap.forEach((k, v) -> context.addServlet(v, k));
        context.addFilter(AuthFilter.class, "/", EnumSet.of(DispatcherType.REQUEST));

        Server server = new Server(webPort);
        Log.setLog(new StdErrLog());
        server.setHandler(context);

        server.start();
    }

    private static ServletConfigurator forConfigure(HttpServlet servlet) {
        return new ServletConfigurator(servlet);
    }

    private static URL resourcePath(String path) {
        ClassLoader classLoader = App.class.getClassLoader();
        return classLoader.getResource(path);
    }

    private static Params handleParams(String ...cliArgs) {
        DefaultParser parser = new DefaultParser();
        CommandLine cli;
        try {
            cli = parser.parse(OptionsBuilder.build(), cliArgs);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        String sListeningPort = cli.getOptionValue(OptionsBuilder.LISTENING_PORT, null);
        String sMSPort = cli.getOptionValue(OptionsBuilder.MESSAGE_SYSTEM_CONNECTION_PORT, null);

        Params params = new Params();
        params.webPort = Integer.parseInt(sListeningPort);
        params.msPort = Integer.parseInt(sMSPort);
        return params;
    }

}
