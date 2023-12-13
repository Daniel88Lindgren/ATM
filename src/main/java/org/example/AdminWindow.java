package org.example;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminWindow {

    private JButton settingsAdmin;
    private JPanel AdminWindow;
    private JList AccountSelection;
    private JList UserSelection;
    private JButton AdminMainMenuButton;
    private JTextPane InformationText;
    private JLabel AccountLabel;
    private JLabel InformationLabel;
    private JLabel UsersLabel;
    private String userInfo = "";
    private String accountInfo = "";
    private String transactionInfo = "";
    private JComboBox<String> userComboBox;
    private UserManager selectedUser;
    private UserManager userManager;
    private UserManager.Account selectedAccount;

    public AdminWindow() {
        this.userManager = selectedUser;

        JFrame jFrame = new JFrame("Admin Window");
        jFrame.setVisible(true);
        jFrame.setSize(550, 500);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setContentPane(AdminWindow);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        jFrame.setIconImage(icon.getImage());
        jFrame.setLocationRelativeTo(null);

        userComboBox = new JComboBox<>();
        this.userManager = userManager;


        //Create a list of users
        List<UserManager> users = UserManager.getUsers();

        DefaultListModel<String> userModel = new DefaultListModel<>();
        for (UserManager userManager : users) {
            userModel.addElement(userManager.getUsername());

        }

        UserSelection.setModel(userModel);

        //ListSelection for users
        UserSelection.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedUsername = (String) UserSelection.getSelectedValue();

                    updateAccountList(selectedUsername);

                    UserManager selectedUser = findUserByUsername(selectedUsername);
                    if (selectedUser != null) {
                        displayUserInfo(selectedUser);
                    }
                }
            }
        });

        //listSelection for accounts
        AccountSelection.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && selectedUser != null) {
                    String selectedAccountName = (String) AccountSelection.getSelectedValue();
                    if (selectedAccountName != null) {
                        selectedAccount = findAccountByName(selectedUser, selectedAccountName);
                        if (selectedAccount != null) {
                            displayAccountInfo(selectedAccount);
                            List<String> transactionHistory = selectedAccount.getTransactionHistory();
                            List<String> paymentHistory = selectedAccount.getPaymentHistory();
                            displayTransactionAndPaymentInfo(transactionHistory, paymentHistory);
                        }
                    }
                }
            }
        });


        //Button back to main menu
        AdminMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                new MainMenu();
            }
        });
        //Button to Admin Settings window
        settingsAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                new AdminSettings(selectedUser, userComboBox);
            }
        });

    }

    //Updates accountlist when you select a user
    private void updateAccountList(String username) {
        DefaultListModel<String> accountListModel = new DefaultListModel<>();
        selectedUser = findUserByUsername(username);

        if (selectedUser != null) {
            for (UserManager.Account account : selectedUser.getAccounts()) {
                accountListModel.addElement(account.getAccountName());
            }
        }

        AccountSelection.setModel(accountListModel);

        displayUserInfo(selectedUser);
    }

    //Method to find a user connected to a username
    private UserManager findUserByUsername(String username) {
        for (UserManager userManager : UserManager.getUsers()) {
            if (userManager.getUsername().equals(username)) {
                return userManager;
            }
        }
        return null; //Användare hittades inte
    }

    //Displays the userinformation in the TextPane
    private void displayUserInfo(UserManager selectedUser) {
        //Visar användar informationen
        userInfo = "User Information:\n"
                + "\nUser: " + selectedUser.getUsername()
                + "\nPassword: " + selectedUser.getPassword() + "\n---------------------------------------------------\n";
        //Clears accounts
        accountInfo = "";

        //Displays user and account information
        InformationText.setText(userInfo + accountInfo);
    }

    //Displays account information in JList Account
    private void displayAccountInfo(UserManager.Account selectedAccount) {
        //Visar kontoinformationen
        accountInfo =
                "Account Information:\n"
                + "\nAccount Name: " + selectedAccount.getAccountName()
                + "\nAccount Number: " + selectedAccount.getAccountNr()
                + "\nBalance: " + selectedAccount.getBalance() + "\n---------------------------------------------------\n";

        //Displays the selected user and its account information in Text Pane
        InformationText.setText(userInfo + accountInfo);
    }

    //Displays the Transaction and Payment information from the selected user and account in Text Pane
    private void displayTransactionAndPaymentInfo(List<String> transactionHistory, List<String> paymentHistory) {
        StringBuilder infoBuilder = new StringBuilder();

        // Display transaction history
        infoBuilder.append("Transactions:\n\n");
        if (!transactionHistory.isEmpty()) {
            for (String transactionRecord : transactionHistory) {
                infoBuilder.append(transactionRecord).append("\n");
            }
        } else {
            infoBuilder.append("No Transaction History found...\n");
        }

        // Display payment history
        infoBuilder.append("\n---------------------------------------------------\n");
        infoBuilder.append("Payments:\n");
        if (!paymentHistory.isEmpty()) {
            for (String paymentRecord : paymentHistory) {
                infoBuilder.append(paymentRecord).append("\n");
            }
        } else {
            infoBuilder.append("No Payment History found...\n");
        }

        InformationText.setText(userInfo + accountInfo + infoBuilder.toString());
    }

    //Method to connect account to the right name
    private UserManager.Account findAccountByName(UserManager selectedUser, String accountName) {
        for (UserManager.Account account : selectedUser.getAccounts()) {
            if (account.getAccountName().equals(accountName)) {
                return account;
            }
        }
        return null; // Account not found
    }

}