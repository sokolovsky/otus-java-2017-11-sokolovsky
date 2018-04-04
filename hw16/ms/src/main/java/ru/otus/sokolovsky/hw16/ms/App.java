package ru.otus.sokolovsky.hw16.ms;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import ru.otus.sokolovsky.hw16.ms.cli.OptionsBuilder;
import ru.otus.sokolovsky.hw16.ms.controller.ControllerListener;
import ru.otus.sokolovsky.hw16.ms.controller.ExchangeListener;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        DefaultParser parser = new DefaultParser();
        CommandLine cli;
        try {
            cli = parser.parse(OptionsBuilder.build(), args);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        String exchangePortArg = cli.getOptionValue(OptionsBuilder.OPTION_EXCHANGE_PORT, null);
        String controlPortArg = cli.getOptionValue(OptionsBuilder.OPTION_CONTROL_PORT, null);

        if (exchangePortArg == null || controlPortArg == null) {
            printHelp();
            return;
        }

        int exchangePort = Integer.parseInt(exchangePortArg);
        int controlPort = Integer.parseInt(controlPortArg);

        try {
            new ControllerListener(controlPort).start();
            new ExchangeListener(exchangePort).start();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Message System", OptionsBuilder.build());
    }
}
