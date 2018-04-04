package ru.otus.sokolovsky.hw16.ms.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class OptionsBuilder {

    public static final String OPTION_CONTROL_PORT = "controlPort";
    public static final String OPTION_EXCHANGE_PORT = "exchangePort";

    public static Options build() {
        Options options = new Options();
        options.addOption(buildControlPortOption());
        options.addOption(buildExchangePortOption());
        return options;
    }

    private static Option buildControlPortOption() {
        return Option.builder("cport")
                .longOpt(OPTION_CONTROL_PORT)
                .desc("Used for control connection. Value from 1024 to 65536")
                .valueSeparator()
                .hasArg(true)
                .build();
    }

    private static Option buildExchangePortOption() {
        return Option.builder("eport")
                .longOpt(OPTION_EXCHANGE_PORT)
                .valueSeparator()
                .hasArg(true)
                .desc("Used for common exchange connection. Value from 1024 to 65536")
                .build();
    }
}
