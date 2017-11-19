
package ru.otus.sokolovsky.hw1;

import org.apache.commons.cli.*;

/**
 * The cli application that is doing nothing but print the version pointed in pom.xml.
 *
 * usage:
 *  mvn clean
 *  mvn package
 *
 *  java -jar target/ru.otus.sokolovsky-hw1\(1.0-SNAPSHOT\)-jar-with-dependencies.jar
 *  java -jar java -jar target/ru.otus.sokolovsky-hw1\(1.0-SNAPSHOT\)-jar-with-dependencies.jar -version
 *
 */
public class App {

    /**
     * runs the process
     */
    static public void main(String[] args) {
        DefaultParser parser = new DefaultParser();
        CommandLine cli;
        // Test instance of SecondClass
        new SecondClass();
        try {
            cli = parser.parse(createOptions(), args);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        if (cli.hasOption("version")) {
            System.out.format("%s version \"%s\"\n", getProjectName(), getProjectVersion());
        } else {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(getProjectName(), createOptions());
        }
    }

    /**
     * Creates demonstrated options
     */
    private static Options createOptions() {
        Options options = new Options();
        options.addOption(buildVersionOption());
        return options;
    }

    private static Option buildVersionOption() {
        return Option.builder("version")
                .desc("Prints version of the project")
                .build();
    }

    private static String getProjectVersion() {
        return App.class.getPackage().getImplementationVersion();
    }

    private static String getProjectName() {
        return App.class.getPackage().getImplementationTitle();
    }
}
