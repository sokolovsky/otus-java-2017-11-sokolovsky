package ru.otus.sokolovsky.hw16.web;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.sokolovsky.hw16.integration.client.Connector;
import ru.otus.sokolovsky.hw16.web.chat.ChatServer;
import ru.otus.sokolovsky.hw16.web.cli.OptionsBuilder;

@Configurable
public class App {

    private static class Params {
        int webPort;
        int msPort;
    }

    private final static App instance;

    static {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/config/app-context.xml");
        instance = (App) context.getBean("app");
    }

    @Autowired
    private ChatServer chatServer;

    @Autowired
    private Connector msConnector;

    public static void main(String[] args) throws Exception {
        Params cliParams = handleParams(args);
        App app = getInstance();
        app.launchWebServer(cliParams.webPort);
        app.connectWithMessageSystem(cliParams.msPort);
        app.launchChatServer();
    }

    public App(ChatServer chatServer, Connector connector) {
        this.chatServer = chatServer;
        msConnector = connector;
    }

    private void launchChatServer() {
        chatServer.listen();
    }

    private void connectWithMessageSystem(int port) {
        msConnector.setPort(port);
        msConnector.connect();
    }

    private void launchWebServer(int webPort) throws Exception {

        Resource xml = Resource.newSystemResource("/META-INF/config/web-server-config.xml");
        XmlConfiguration configuration = new XmlConfiguration(xml.getInputStream());
        Server server = (Server)configuration.configure();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(webPort);
        server.addConnector(connector);
        server.start();
    }

    public static String staticPath() {
        ClassLoader classLoader = App.class.getClassLoader();
        return classLoader.getResource("static/assets").toString();
    }

    private static ApplicationContext getAppContext() {
        return new ClassPathXmlApplicationContext("/META-INF/config/app-context.xml");
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

    public static App getInstance() {
        return instance;
    }

    public static Connector getMSConnector() {
        return instance.msConnector;
    }
}
