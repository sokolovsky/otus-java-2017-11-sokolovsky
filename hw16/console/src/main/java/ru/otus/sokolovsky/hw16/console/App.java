package ru.otus.sokolovsky.hw16.console;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.sokolovsky.hw16.console.terminal.Terminal;
import ru.otus.sokolovsky.hw16.console.terminal.contexts.ConsoleContext;
import ru.otus.sokolovsky.hw16.integration.message.IllegalFormatException;
import ru.otus.sokolovsky.hw16.integration.message.MessageTransformer;
import ru.otus.sokolovsky.hw16.integration.message.ParametrizedMessage;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.Executors;

public class App {

    final String DB_INSTANCE_NAME = "db";
    final String WEB_INSTANCE_NAME = "web";
    final String MS_INSTANCE_NAME = "ms";

    public static void main(String[] args) throws IllegalFormatException {
        ParametrizedMessage message = MessageTransformer.fromJson("{\"type\":\"command\",\"destination\":\"service\",\"source\":null,\"name\":\"create-new-named-channel\",\"headers\":{},\"body\":{\"channel\":\"DB\"}}");
        ApplicationContext applicationContext = getApplicationContext();

        ConsoleContext console = (ConsoleContext) applicationContext.getBean("consoleContext");
        // blocks
        console.run();
    }

    public static Terminal createTerminal() {
        return new Terminal(new OutputStreamWriter(System.out), new InputStreamReader(System.in));
    }

    private static org.springframework.context.ApplicationContext getApplicationContext() {
        return new ClassPathXmlApplicationContext("spring/app-context.xml");
    }
}
