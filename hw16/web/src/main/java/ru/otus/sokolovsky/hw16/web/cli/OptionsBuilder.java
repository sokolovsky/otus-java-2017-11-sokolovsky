package ru.otus.sokolovsky.hw16.web.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class OptionsBuilder {

    public static final String LISTENING_PORT = "port";
    public static final String MESSAGE_SYSTEM_CONNECTION_PORT = "msport";
    public static final String WEB_SOCKET_PORT = "wsport";

    public static Options build() {
        Options options = new Options();
        options.addOption(buildPortOption());
        options.addOption(buildMSPortOption());
        options.addOption(buildWebSocketPortOption());
        return options;
    }

    private static Option buildPortOption() {
        return Option.builder(LISTENING_PORT)
                .longOpt(LISTENING_PORT)
                .desc("Used for control connection. Integer value with range from 1024 to 65536")
                .valueSeparator()
                .hasArg(true)
                .build();
    }

    private static Option buildWebSocketPortOption() {
        return Option.builder(WEB_SOCKET_PORT)
                .longOpt(WEB_SOCKET_PORT)
                .desc("Used for web socket connection. Integer value with range from 1024 to 65536")
                .valueSeparator()
                .hasArg(true)
                .build();
    }

    private static Option buildMSPortOption() {
        return Option.builder(MESSAGE_SYSTEM_CONNECTION_PORT)
                .longOpt(MESSAGE_SYSTEM_CONNECTION_PORT)
                .desc("Used for connection to message system . Integer value with range from 1024 to 65536")
                .valueSeparator()
                .hasArg(true)
                .build();
    }
}
