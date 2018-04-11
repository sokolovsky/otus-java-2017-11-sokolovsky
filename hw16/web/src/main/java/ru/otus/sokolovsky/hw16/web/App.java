package ru.otus.sokolovsky.hw16.web;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import ru.otus.sokolovsky.hw16.web.cli.OptionsBuilder;

public class App {

    public static void main(String[] args) {
        // init web service with port and message system port
        DefaultParser parser = new DefaultParser();
        CommandLine cli;
        try {
            cli = parser.parse(OptionsBuilder.build(), args);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        String sListeningPort = cli.getOptionValue(OptionsBuilder.LISTENING_PORT, null);
        String sMSPort = cli.getOptionValue(OptionsBuilder.MESSAGE_SYSTEM_CONNECTION_PORT, null);

        int lPort;
        int msPort;
        try {
            lPort = Integer.parseInt(sListeningPort);
            msPort = Integer.parseInt(sMSPort);
        } catch (NumberFormatException e) {
            System.out.println("Found out usage of wrong port addresses");
            return;
        }
        System.out.println(lPort + " - " + msPort);
    }
}
