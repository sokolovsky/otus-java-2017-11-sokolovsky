package ru.otus.sokolovsky.hw16.console.terminal;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Scanner;

public class Terminal {

    private final Writer out;
    private final Reader in;
    private String prompt = "";

    public Terminal(Writer out, Reader in) {
        this.out = out;
        this.in = in;
    }

    public String getLine() throws IOException {
        return getLine(prompt);
    }

    public String getLine(String prompt) throws IOException {
        out.write(prompt + " /> ");
        out.flush();
        return readLine();
    }

    private String readLine() {
        Scanner scanner = new Scanner(in);
        return scanner.nextLine();
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void writeln(String line) {
        try {
            out.write(line);
            out.write("\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logLine(String line) {
        writeln(line);
    }
}
