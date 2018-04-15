package ru.otus.sokolovsky.hw16.db;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.sokolovsky.hw16.db.cli.OptionsBuilder;
import ru.otus.sokolovsky.hw16.integration.client.Connector;
import ru.otus.sokolovsky.hw16.integration.control.ServiceAction;
import ru.otus.sokolovsky.hw16.integration.message.MessageFactory;
import ru.otus.sokolovsky.hw16.integration.message.ParametrizedMessage;

public class App {

    public static void main(String[] args) throws ParseException {
        int msPort = getMessageSystemPort(args);
        System.out.println("MS port - " + msPort);

        ApplicationContext appContext = getAppContext();
        Connector connector = (Connector) appContext.getBean("msConnector");
        connector.setPort(msPort);

        connector.connect();

        ParametrizedMessage subscribeControlMessage = MessageFactory.createControlMessage(ServiceAction.SUBSCRIBE_ON_CHANNEL);
        subscribeControlMessage.setParameter("name", "DB");
        connector.sendMessage(subscribeControlMessage);
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
