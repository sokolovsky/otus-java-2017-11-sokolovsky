package ru.otus.sokolovsky.hw16.ms.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class OptionsBuilder {

    public static final String OPTION_CONTROL_PORT = "port";

    public static Options build() {
        Options options = new Options();
        options.addOption(buildPortOption());
        return options;
    }

    private static Option buildPortOption() {
        return Option.builder("port")
                .longOpt(OPTION_CONTROL_PORT)
                .desc("Used for control connection. Value from 1024 to 65536")
                .valueSeparator()
                .hasArg(true)
                .build();
    }
}
