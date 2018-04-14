package ru.otus.sokolovsky.hw16.web;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import ru.otus.sokolovsky.hw16.web.chat.ChatServer;
import ru.otus.sokolovsky.hw16.web.cli.OptionsBuilder;

@Configurable
public class App {

    private static class Params {
        int webPort;
        int msPort;
    }

    @Autowired
    private ChatServer chatServer;

    public static void main(String[] args) throws Exception {
        Params cliParams = handleParams(args);
        ApplicationContext appContext = getAppContext();
        App app = (App) appContext.getBean("app");
        appContext.getAutowireCapableBeanFactory().autowireBean(app);
        app.launchWebServer(cliParams.webPort);
        app.connectWithMessageSystem(cliParams.msPort);
        app.launchChatServer();
    }

    public App(ChatServer chatServer) {
        this.chatServer = chatServer;
    }

    private void launchChatServer() {
        chatServer.listen();
    }

    private void connectWithMessageSystem(int port) {
        // todo organize connection
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
}
