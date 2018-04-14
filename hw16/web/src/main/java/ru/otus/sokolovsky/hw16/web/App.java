package ru.otus.sokolovsky.hw16.web;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.sokolovsky.hw16.web.cli.OptionsBuilder;
import ru.otus.sokolovsky.hw16.web.servlet.ServletConfigurator;
import javax.servlet.http.HttpServlet;
import java.net.URL;

@Configurable
public class App {

    private static class Params {
        int wsPort;
        int webPort;
        int msPort;
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
        Params cliParams = handleParams(args);
        ApplicationContext appContext = getAppContext();
        appContext.getAutowireCapableBeanFactory().autowireBean(app);
        app.launchWebServer(cliParams.webPort);
        app.connectWithMessageSystem(cliParams.msPort);
        app.launchSocketServer(cliParams.wsPort);
    }

    private void launchSocketServer(int port) {
    }

    private void connectWithMessageSystem(int port) {
        // todo organize connection
    }

    private void launchWebServer(int webPort) throws Exception {

        Resource xml = Resource.newSystemResource("/config/web-server-config.xml");
        XmlConfiguration configuration = new XmlConfiguration(xml.getInputStream());
        Server server = (Server)configuration.configure();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(webPort);
        server.start();
        server.join();
    }

    private static ServletConfigurator forConfigure(HttpServlet servlet) {
        return new ServletConfigurator(servlet);
    }

    private static URL resourcePath(String path) {
        ClassLoader classLoader = App.class.getClassLoader();
        return classLoader.getResource(path);
    }

    private static ApplicationContext getAppContext() {
        return new ClassPathXmlApplicationContext("config/app-context.xml");
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
        String wsPort = cli.getOptionValue(OptionsBuilder.WEB_SOCKET_PORT, null);

        Params params = new Params();
        params.webPort = Integer.parseInt(sListeningPort);
        params.msPort = Integer.parseInt(sMSPort);
        params.wsPort = Integer.parseInt(wsPort);
        return params;
    }
}
