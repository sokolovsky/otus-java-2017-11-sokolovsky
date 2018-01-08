package ru.otus.sokolovsky.hw6.terminal.actions;

import ru.otus.sokolovsky.hw6.accounting.Account;
import ru.otus.sokolovsky.hw6.accounting.WrongOperationException;
import ru.otus.sokolovsky.hw6.atm.Machine;
import ru.otus.sokolovsky.hw6.atm.Note;
import ru.otus.sokolovsky.hw6.terminal.Terminal;

import java.io.IOException;
import java.util.Map;

public class GettingMoneyFromAccount implements Action {

    private Account account;

    public GettingMoneyFromAccount(Account account) {
        this.account = account;
    }

    @Override
    public void run(Terminal terminal) {
        terminal.writeln("Getting money");
        int sum = 0;
        do {
            try {
                Note minNote = Machine.getInstance().getMinNote();
                if (minNote == null) {
                    terminal.writeln("There are no money in ATM");
                    return;
                }
                String line = terminal.getLine(String.format("Type sum of money, min sum is `%,d`", minNote.getAmount()));
                sum = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if (account.getBalance() < sum) {
                terminal.writeln("Amount sum your account is not enough, try to type another one");
                sum = 0;
                continue;
            }
            if (!Machine.getInstance().canGetSum(sum)) {
                terminal.writeln(String.format("It is impossible to get sum `%,d`, try to type another one", sum));
                sum = 0;
            }
        } while (sum == 0);

        try {
            account.withdraw(sum);
        } catch (WrongOperationException e) {
            e.printStackTrace();
            return;
        }

        Map<Note, Integer> money;
        try {
            money = Machine.getInstance().getMoney(sum);
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
}
