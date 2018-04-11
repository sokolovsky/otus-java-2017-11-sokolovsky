package ru.otus.sokolovsky.hw16.db.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class OptionsBuilder {

    public static final String MESSAGE_SYSTEM_CONNECTION_PORT = "msport";

    public static Options build() {
        Options options = new Options();
        options.addOption(buildPortOption());
        return options;
    }

    private static Option buildPortOption() {
        return Option.builder(MESSAGE_SYSTEM_CONNECTION_PORT)
                .longOpt(MESSAGE_SYSTEM_CONNECTION_PORT)
                .desc("Used for interact with message system. Integer value with range from 1024 to 65536")
                .valueSeparator()
                .hasArg(true)
                .build();
    }
}
