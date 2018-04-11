package ru.otus.sokolovsky.hw16.db;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import ru.otus.sokolovsky.hw16.db.cli.OptionsBuilder;

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

        String sMSPort = cli.getOptionValue(OptionsBuilder.MESSAGE_SYSTEM_CONNECTION_PORT, null);

        int lPort;
        int msPort;
        try {
            msPort = Integer.parseInt(sMSPort);
        } catch (NumberFormatException e) {
            System.out.println("Found out usage of wrong port addresses");
            return;
        }
        System.out.println("MS port - " + msPort);
    }
}
