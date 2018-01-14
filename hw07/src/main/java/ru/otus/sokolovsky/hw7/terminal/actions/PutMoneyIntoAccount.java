package ru.otus.sokolovsky.hw7.terminal.actions;

import ru.otus.sokolovsky.hw7.accounting.Account;
import ru.otus.sokolovsky.hw7.atm.Machine;
import ru.otus.sokolovsky.hw7.atm.Note;
import ru.otus.sokolovsky.hw7.terminal.Terminal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PutMoneyIntoAccount implements Action {

    private Account account;
    private static Map<Integer, Note> notesChoice;

    static {
        notesChoice = new HashMap<>();
        int i = 1;
        for (Note note : Note.values()) {
            notesChoice.put(i++, note);
        }
    }

    public PutMoneyIntoAccount(Account account) {
        this.account = account;
    }

    @Override
    public void run(Terminal terminal) {
        terminal.writeln("Putting money");
        int credit = 0;
        while (true) {
            terminal.writeln("Chose the next notes:");
            for (Map.Entry<Integer, Note> entry : notesChoice.entrySet()) {
                int n = entry.getKey();
                Note note = entry.getValue();
                terminal.writeln(String.format("%,8d - %d", note.getAmount(), n));
            }
            terminal.writeln("Type `q` for exit");

            int numberOfNote;
            Note note;
            try {
                String line = terminal.getLine("enter the number").trim();
                if (line.equals("q")) {
                    break;
                }
                numberOfNote = Integer.parseInt(line);
                note = notesChoice.get(numberOfNote);
                if (note == null) {
                    terminal.writeln("Wrong note number");
                    continue;
                }
            } catch (NumberFormatException e) {
                terminal.writeln("Wrong symbols");
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            int noteCount = 0;
            try {
                String line = terminal.getLine(String.format("enter count of `%,d` notes", note.getAmount()));
                noteCount = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                terminal.writeln("Wrong count number");
                continue;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (noteCount == 0) {
                continue;
            }
            Machine.getInstance().putMoney(note, noteCount);

            int money = noteCount * note.getAmount();
            terminal.writeln(String.format("Put %d", money));
            credit += money;
        }
        if (credit > 0) {
            account.credit(credit);
            terminal.writeln(String.format("Money %d was put into account `%s`", credit, account.getNumber()));
        }
    }

    @Override
    public String help() {
        return "Puts money into account";
    }

    @Override
    public String formula() {
        return "put";
    }
}
