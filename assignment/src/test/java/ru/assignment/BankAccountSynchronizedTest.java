package ru.assignment;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Андрей on 30.03.2015.
 */
@RunWith(JUnitParamsRunner.class)
public class BankAccountSynchronizedTest {
    List<Thread> threadList;
    List<BankAccountNewGeneration> bankAccountsList;

    @Before
    public void init() {
        threadList = new ArrayList<>();
        bankAccountsList = new ArrayList<>();
    }

    private class BankAccount {
        private int balance;

        public BankAccount(int balance) {
            this.balance = balance;
        }

        public synchronized void deposit(int amount) {
            balance += amount;
        }

        public synchronized void withdraw(int amount) {
            balance -= amount;
        }

        public int getBalance() {
            return balance;
        }
    }



    @Test
    @Parameters
    public void checkAccountAmount(int balance, int threadCount, final int amount) {
        final BankAccount bankAccount = new BankAccount(balance);
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread() {
                public void run() {
                    bankAccount.withdraw(amount);
                    bankAccount.deposit(amount);
                }
            };
            threadList.add(thread);
        }
        for (Thread thread : threadList) {
            thread.start();
        }
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                fail(e.toString());
            }
        }
        assertTrue(bankAccount.getBalance() == balance);
    }

    private Object[] parametersForCheckAccountAmount() {
        return $($(50, 1, 10),
                $(100, 3, 10),
                $(1000, 7, 10));
    }

    private class BankAccountNewGeneration {
        private int balance;

        public BankAccountNewGeneration(int balance) {
            this.balance = balance;
        }

        public synchronized void deposit(int amount) {
            balance += amount;
            notifyAll();
        }

        public synchronized void withdraw(int amount) {
           while (balance < 10) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    fail(e.toString());
                }
            }
            balance -= amount;
        }

        public int getBalance() {
            return balance;
        }
    }

    private class BankThread extends Thread {


        public void run() {
            ThreadLocalRandom generator = ThreadLocalRandom.current();
            int accountFromIdentifier = generator.nextInt(0, bankAccountsList.size());
            int accountToIdentifier = generator.nextInt(0, bankAccountsList.size());
            int randomAmount = generator.nextInt(0, 11);
            BankAccountNewGeneration accountFrom = bankAccountsList.get(accountFromIdentifier);
            BankAccountNewGeneration accountTo = bankAccountsList.get(accountToIdentifier);
            accountFrom.withdraw(randomAmount);
            accountTo.deposit(randomAmount);
        }
    }

    @Test
    @Parameters
    public void checkListAccountAmount(int bankAccountCount, int threadCount, int balance) {
        for (int i = 0; i < bankAccountCount; i++) {
            bankAccountsList.add(new BankAccountNewGeneration(balance));
        }
        for (int i = 0; i < threadCount; i++) {
            BankThread thread = new BankThread();
            threadList.add(thread);
        }
        for (Thread thread : threadList) {
            thread.start();
        }
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                fail(e.toString());
            }
        }
        int sum = 0;
        for (BankAccountNewGeneration account : bankAccountsList) {
            sum += account.getBalance();
        }

        assertTrue(bankAccountCount * balance + " g " + sum, (bankAccountCount * balance) == sum);
    }

    private Object[] parametersForCheckListAccountAmount() {
        return $($(50, 5, 500),
                $(100, 10, 1000),
                $(200, 20, 2000));
    }
}
