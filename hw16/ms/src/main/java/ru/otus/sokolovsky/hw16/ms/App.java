package ru.otus.sokolovsky.hw16.ms;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.sokolovsky.hw16.ms.channel.PointToPointChannel;
import ru.otus.sokolovsky.hw16.ms.cli.OptionsBuilder;
import ru.otus.sokolovsky.hw16.ms.manage.SystemManager;
import ru.otus.sokolovsky.hw16.ms.server.ServerListener;
import ru.otus.sokolovsky.hw16.ms.service.ServiceHandler;

import java.util.logging.Logger;

public class App {

    private static final Logger logger = Logger.getLogger("Message System");

    public static void main(String[] args) {
        DefaultParser parser = new DefaultParser();
        CommandLine cli;
        try {
            cli = parser.parse(OptionsBuilder.build(), args);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        String sPort = cli.getOptionValue(OptionsBuilder.OPTION_CONTROL_PORT, null);
        int port = Integer.parseInt(sPort);

        ApplicationContext ctx = getAppContext();

        SystemManager systemManager = (SystemManager) ctx.getBean("systemManager");

        ServerListener listener = (ServerListener) ctx.getBean("serverListener");
        listener.startListening(port);

        System.out.println(ctx.getBean("connectionHandlers"));

        PointToPointChannel serviceChannel = systemManager.createPointToPointChannel("service");
        ServiceHandler serviceHandler = new ServiceHandler(serviceChannel, systemManager);
        serviceHandler.init();

        System.out.println("Message System was started");
    }

    private static ApplicationContext getAppContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/app-context.xml");
    }

    private static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Message System", OptionsBuilder.build());
    }
}
