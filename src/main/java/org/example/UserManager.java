package org.example;

import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;



public class UserManager {
    private String username;
    private String password;
    private List<Account> accounts = new ArrayList<>();
    private List<String> transactionHistory = new ArrayList<>(); // Lägg till en lista för att spara transaktionshistorik
    private static List<UserManager> users = new ArrayList<>();
    private static UserManager currentUser;


    //List for paid bills
    private List<String> paymentHistory = new ArrayList<>();

    // Adds a payment record to the payment history
    public void addPaymentRecord(String record) {
        paymentHistory.add(record);
    }

    // Gets payment history
    public List<String> getPaymentHistory() {
        return paymentHistory;
    }



        public static void setCurrentUser(UserManager currentUser) {
            UserManager.currentUser = currentUser;
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
            return new ArrayList<>(); // Return an empty list if no user is logged in. Denna kan nog tas bort då någon alltid är inloggad
        }
    }


    public UserManager(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public static void clearCurrentUserAccounts() {
        if (currentUser != null) {
            currentUser.getAccounts().clear();
        }
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


    public static List<UserManager> getUsers() {
        return users;
    }

    public static void addUser(UserManager user) {
        users.add(user);
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


    public static UserManager getCurrentUser() {
        return currentUser;
    }

    public void addTransactionRecord(String record) {
        transactionHistory.add(record); // Lägg till transaktionshistorik
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
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
        UserManager userLars = new UserManager("lars", "111");
        UserManager userArta = new UserManager("arta", "222");
        UserManager userMickey = new UserManager("mickey", "333");
        UserManager userDaniel = new UserManager("daniel", "444");
        UserManager userAnders = new UserManager("anders", "555");
        UserManager userAdmin = new UserManager("admin", "admin");

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

        userAnders.addAccount("Checking", 17, 2000);
        userAnders.addAccount("Savings", 18, 99000);
        userAnders.addAccount("Investment", 19, 4500);
        userAnders.addAccount("Retirement", 20, 500);

        userAdmin.addAccount("Admin", 99, 10000000);

        addUser(userLars);
        addUser(userArta);
        addUser(userMickey);
        addUser(userDaniel);
        addUser(userAnders);

        addUser(userAdmin);
    }
}






