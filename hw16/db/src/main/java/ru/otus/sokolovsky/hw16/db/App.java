package ru.otus.sokolovsky.hw16.db;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.sokolovsky.hw16.db.cli.OptionsBuilder;
import ru.otus.sokolovsky.hw16.db.provider.DatabaseBuilder;
import ru.otus.sokolovsky.hw16.db.provider.UsersProvider;
import ru.otus.sokolovsky.hw16.integration.client.Connector;
import ru.otus.sokolovsky.hw16.integration.control.ChannelType;
import ru.otus.sokolovsky.hw16.integration.control.ServiceAction;
import ru.otus.sokolovsky.hw16.integration.message.MessageFactory;
import ru.otus.sokolovsky.hw16.integration.message.ParametrizedMessage;

public class App {

    public static void main(String[] args) throws ParseException {
        int msPort = getMessageSystemPort(args);
        System.out.println("Message System port - " + msPort);

        ApplicationContext appContext = getAppContext();

        Connector connector = messageSystemConnect(appContext, msPort);
        createDatabases(appContext);
        registerUsers(appContext);
        subscribeOnMessages(connector);

        System.out.println("DB service has being started");
    }

    private static void subscribeOnMessages(Connector connector) {
        ParametrizedMessage subscribeControlMessage = MessageFactory.createControlMessage(ServiceAction.SUBSCRIBE_ON_CHANNEL);
        subscribeControlMessage.setParameter("channel", "DB");
        subscribeControlMessage.setParameter("channelType", ChannelType.POINT_TO_POINT.name());
        connector.sendMessage(subscribeControlMessage);
    }

    private static void registerUsers(ApplicationContext appContext) {
        UsersProvider usersProvider = (UsersProvider) appContext.getBean("usersProvider");
        usersProvider.registerIntoDatabase();
    }

    private static void createDatabases(ApplicationContext appContext) {
        DatabaseBuilder databaseBuilder = (DatabaseBuilder) appContext.getBean("databaseBuilder");
        databaseBuilder.createTables();
    }

    private static Connector messageSystemConnect(ApplicationContext appContext, int port) {
        Connector connector = (Connector) appContext.getBean("msConnector");
        connector.setPort(port);
        connector.connect();
        return connector;
    }

    private static ApplicationContext getAppContext() {
        return new ClassPathXmlApplicationContext("/META-INF/application-context.xml");
    }

    private static int getMessageSystemPort(String[] args) throws ParseException {
        DefaultParser parser = new DefaultParser();
        CommandLine cli;
        cli = parser.parse(OptionsBuilder.build(), args);
        String sMSPort = cli.getOptionValue(OptionsBuilder.MESSAGE_SYSTEM_CONNECTION_PORT, null);
        return Integer.parseInt(sMSPort);
    }
}
