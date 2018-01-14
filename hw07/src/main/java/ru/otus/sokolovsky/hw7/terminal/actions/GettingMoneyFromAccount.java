package ru.otus.sokolovsky.hw7.terminal.actions;

import ru.otus.sokolovsky.hw7.accounting.Account;
import ru.otus.sokolovsky.hw7.accounting.WrongOperationException;
import ru.otus.sokolovsky.hw7.atm.Machine;
import ru.otus.sokolovsky.hw7.atm.Note;
import ru.otus.sokolovsky.hw7.terminal.Terminal;

import java.io.IOException;
import java.util.Map;

public class GettingMoneyFromAccount implements Action {

    private Account account;
    private Machine machine;

    public GettingMoneyFromAccount(Account account, Machine machine) {
        this.account = account;
        this.machine = machine;
    }

    @Override
    public void execute(Terminal terminal) {
        terminal.writeln("Getting account money");
        int sum = handleUserSum(terminal);
        try {
            account.withdraw(sum);
        } catch (WrongOperationException e) {
            e.printStackTrace();
            return;
        }

        Map<Note, Integer> money;
        try {
            money = machine.getMoney(sum);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        terminal.writeln(String.format("Getting money: %,d", sum));
        for (Map.Entry<Note, Integer> entry : money.entrySet()) {
            Note note = entry.getKey();
            terminal.writeln(String.format("%,8d - %d", note.getAmount(), entry.getValue()));
        }
    }

    @Override
    public String help() {
        return "Withdraws money from account";
    }

    @Override
    public String formula() {
        return "get";
    }

    private int handleUserSum(Terminal terminal) {
        int sum = 0;
        do {
            try {
                Note minNote = machine.getMinNote();
                if (minNote == null) {
                    terminal.writeln("There are no money in ATM");
                    return 0;
                }
                String line = terminal.getLine(String.format("Type sum of money, min sum is `%,d`", minNote.getAmount()));
                sum = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
            if (account.getBalance() < sum) {
                terminal.writeln("Amount sum yours account is not enough, try to type another one");
                sum = 0;
                continue;
            }
            if (!machine.canGetSum(sum)) {
                terminal.writeln(String.format("It is impossible to get sum `%,d`, try to type another one", sum));
                sum = 0;
            }
        } while (sum == 0);
        return sum;
    }
}
