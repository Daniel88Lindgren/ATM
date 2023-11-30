
package org.example;

import java.util.ArrayList;
import java.util.List;


    public class UserManager {
        private String username;
        private String password;
        private List<Account> accounts = new ArrayList<>();
        private static List<UserManager> users = new ArrayList<>();

        //currentUser to track the current logged in user in system
        private static UserManager currentUser;

        public static UserManager getCurrentUser() {
            return currentUser;
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

        public static List<UserManager> getUsers() {
            return users;
        }

        public static void addUser(UserManager user) {
            users.add(user);
        }

        //Method for the user to change password
        public static void updateCurrentUserPassword(String newPassword) {//Kontrollera så att den verkligen ändrar användarens lösenord. currentUser.setPassword(newPassword); borde ha en equal to username och sedan ändra lösenord.
            if (currentUser != null) {
                currentUser.setPassword(newPassword);


            }
        }





        // Account inner class
        public class Account {
            private String accountName;
            private int accountNr;
            private double balance;

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
        }

        // Static block to initialize users and their accounts
        static {
            UserManager userLars = new UserManager("lars", "111");
            UserManager userArta = new UserManager("arta", "222");
            UserManager userMickey = new UserManager("mickey", "333");
            UserManager userDaniel = new UserManager("daniel", "444");
            UserManager userAnders = new UserManager("anders", "555");

            userLars.addAccount("Checking", 1, 5000);
            userLars.addAccount("Savings", 2, 6000);
            userLars.addAccount("Investment", 3, 7000);
            userLars.addAccount("Retirement", 4, 10000);

            userArta.addAccount("Checking Account", 5, 3000);
            userArta.addAccount("Savings Account", 6, 8000);
            userArta.addAccount("Investment Account", 7, 7500);
            userArta.addAccount("Retirement Account", 8, 100000);

            userMickey.addAccount("Checking Account", 9, 1000);
            userMickey.addAccount("Savings Account", 10, 3000);
            userMickey.addAccount("Investment Account", 11, 4500);
            userMickey.addAccount("Retirement Account", 12, 3300);

            userDaniel.addAccount("Checking Account", 13, 9300);
            userDaniel.addAccount("Savings Account", 14, 27000);
            userDaniel.addAccount("Investment Account", 15, 77000);
            userDaniel.addAccount("Retirement Account", 16, 1000000);

            userAnders.addAccount("Checking Account", 17, 2000);
            userAnders.addAccount("Savings Account", 18, 99000);
            userAnders.addAccount("Investment Account", 19, 4500);
            userAnders.addAccount("Retirement Account", 20, 500);


            addUser(userLars);
            addUser(userArta);
            addUser(userMickey);
            addUser(userDaniel);
            addUser(userAnders);
        }
    }

