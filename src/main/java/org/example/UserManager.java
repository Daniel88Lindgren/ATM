package org.example;

import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private String username;
    private String password;

    //List for users accounts
    private List<Account> accounts = new ArrayList<>();
    private List<String> transactionHistory = new ArrayList<>(); // Lägg till en lista för att spara transaktionshistorik

    //List for all users
    private static List<UserManager> users = new ArrayList<>();
    private static UserManager currentUser;

    //List for paid bills
    private List<String> paymentHistory = new ArrayList<>();

    // Gets payment history
    public List<String> getPaymentHistory() {
        return paymentHistory;
    }

    public static void setCurrentUser(UserManager currentUser) {
            UserManager.currentUser = currentUser;
    }

    public static List<UserManager> getUsers() {
        return users;
    }

    public static void addUser(UserManager user) {
        users.add(user);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Account> getAccounts() {
        return accounts;
    }
    public static List<Account> getOtherAccounts(Account excludedAccount) {
        List<Account> otherAccounts = new ArrayList<>();
        if (currentUser != null) {
            for (Account account : currentUser.getAccounts()) {
                if (!account.equals(excludedAccount)) {
                    otherAccounts.add(account);
                }
            }
        }
        return otherAccounts;
    }
    public static UserManager getCurrentUser() {
        return currentUser;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    // Adds a payment record to the payment history
    public void addPaymentRecord(String record) {
        paymentHistory.add(record);
    }

    // Method to authenticate a user
        public static boolean authenticate(String username, String password) {
            for (UserManager user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    currentUser = user; // Set the current user
                    return true; // User found and authenticated
                }
            }
            return false; // No matching user found
        }

    // Method to get the current user's accounts
    public static List<Account> getCurrentUserAccounts() {
        if (currentUser != null) {
            return currentUser.getAccounts();
        } else {
            return new ArrayList<>();
        }
    }

    public UserManager(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Sorry guy´s, your money is gone!!
    public static void clearCurrentUserAccounts() {
        if (currentUser != null) {
            currentUser.getAccounts().clear();
        }
    }

    public void addAccount(String accountName, int accountNr, double balance) {
        Account newAccount = new Account(accountName, accountNr, balance);
        accounts.add(newAccount);
    }

    public void removeAccount(Account accountToRemove) {
        accounts.remove(accountToRemove);
    }

    public int getNextAccountNumber() {
        int maxAccountNr = 0;

        // Iterate over all users and their accounts
        for (UserManager user : users) {
            for (Account account : user.getAccounts()) {
                int currentAccountNr = account.getAccountNr();
                if (currentAccountNr > maxAccountNr) {
                    maxAccountNr = currentAccountNr;
                }
            }
        }

        return maxAccountNr + 1;
    }

    public String adminAddAccount(String accountName, double balance) {
        int nextAccountNr = getNextAccountNumber();
        Account newAccount = new Account(accountName, nextAccountNr, balance);
        accounts.add(newAccount);
        return null;
    }

    //Method for the user to change password
    public static void updateCurrentUserPassword(String newPassword) {
        if (currentUser != null) {
            currentUser.setPassword(newPassword);
        }
    }

    public static void removeUser(UserManager user) {
        users.remove(user);
    }

    public void addTransactionRecord(String record) {
        transactionHistory.add(record); // Lägg till transaktionshistorik
    }

    public class Account {
        private String accountName;
        private int accountNr;
        private double balance;

        public void setAccountNr(int accountNr) {
            this.accountNr = accountNr;
        }

        public Account(String accountName, int accountNr, double balance) {
            this.accountName = accountName;
            this.accountNr = accountNr;
            this.balance = balance;
        }

        private List<String> transactionHistory = new ArrayList<>();

        public List<String> getTransactionHistory() {
            return transactionHistory;
        }

        public void addTransactionRecord(String transactionRecord) {
            transactionHistory.add(transactionRecord);
        }

        private List<String> paymentHistory = new ArrayList<>();
        public List<String> getPaymentHistory() {
            return paymentHistory;
        }

        public void addPaymentRecord(String paymentRecord) {
            paymentHistory.add(paymentRecord);
        }

        public String getAccountName() {
            return accountName;
        }

        public int getAccountNr() {
            return accountNr;
        }

        public double getBalance() {
            return balance;
        }

        public void withdraw(double amount) {
            if (this.balance >= amount) {
                this.balance -= amount;
            } else {
                throw new IllegalArgumentException("Insufficient funds");
            }
        }
        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public void deposit(double amount) {
            this.balance += amount;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }
    }

    // Static block to initialize users and their accounts
    static {
        UserManager userLars = new UserManager("Lars", "111");
        UserManager userArta = new UserManager("Arta", "222");
        UserManager userMickey = new UserManager("Mickey", "333");
        UserManager userDaniel = new UserManager("Daniel", "444");
        UserManager userAnders = new UserManager("Anders", "555");
        UserManager userAdmin = new UserManager("Admin", "admin");

        userLars.addAccount("Checking", 1, 5000);
        userLars.addAccount("Savings", 2, 6000);
        userLars.addAccount("Investment", 3, 7000);
        userLars.addAccount("Retirement", 4, 10000);

        userArta.addAccount("Checking", 5, 3000);
        userArta.addAccount("Savings", 6, 8000);
        userArta.addAccount("Investment", 7, 7500);
        userArta.addAccount("Retirement", 8, 100000);

        userMickey.addAccount("Checking", 9, 1000);
        userMickey.addAccount("Savings", 10, 3000);
        userMickey.addAccount("Investment", 11, 4500);
        userMickey.addAccount("Retirement", 12, 3300);

        userDaniel.addAccount("Checking", 13, 9300);
        userDaniel.addAccount("Savings", 14, 27000);
        userDaniel.addAccount("Investment", 15, 77000);
        userDaniel.addAccount("Retirement", 16, 1000000);

        userAnders.addAccount("Checking", 17, 100000);
        userAnders.addAccount("Savings", 18, 999000);
        userAnders.addAccount("Investment", 19, 450000);
        userAnders.addAccount("Retirement", 20, 50000000);

        userAdmin.addAccount("Admin", 99, 10000000);

        addUser(userLars);
        addUser(userArta);
        addUser(userMickey);
        addUser(userDaniel);
        addUser(userAnders);
        addUser(userAdmin);
    }
}






