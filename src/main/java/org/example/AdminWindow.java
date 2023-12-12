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


        //Skapar listan med användare, baserat på användarnamnet
        List<UserManager> users = UserManager.getUsers();

        DefaultListModel<String> userModel = new DefaultListModel<>();
        for (UserManager userManager : users) {
            userModel.addElement(userManager.getUsername());

        }

        UserSelection.setModel(userModel);

        //ListSelection för användare
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

        //listSelection till Konton
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



        AdminMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                new MainMenu();
            }
        });

        settingsAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                new AdminSettings(selectedUser, userComboBox);
            }
        });

    }

    //Uppdaterar kontolistan när man väljer en ny användare
    private void updateAccountList(String username) {
        DefaultListModel<String> accountListModel = new DefaultListModel<>();
        selectedUser = findUserByUsername(username);

        if (selectedUser != null) {
            for (UserManager.Account account : selectedUser.getAccounts()) {
                accountListModel.addElement(account.getAccountName());
            }
        }

        AccountSelection.setModel(accountListModel);

        //Visa användar information
        displayUserInfo(selectedUser);
    }

    //Metod för att hitta Användare kopplat till Användarnamn,
    private UserManager findUserByUsername(String username) {
        for (UserManager userManager : UserManager.getUsers()) {
            if (userManager.getUsername().equals(username)) {
                return userManager;
            }
        }
        return null; //Användare hittades inte
    }

    //Visar Användare i TextPane
    private void displayUserInfo(UserManager selectedUser) {
        //Visar användar informationen
        userInfo = "User Information:\n"
                + "\nUser: " + selectedUser.getUsername()
                + "\nPassword: " + selectedUser.getPassword() + "\n---------------------------------------------------\n";

        //När man väljer ny användare så nollas displayAccountInfo
        accountInfo = "";

        //Visar Användarinformation och kontoinformation
        InformationText.setText(userInfo + accountInfo);
    }

    //Visar kontoinformationen i JList ACCOUNT
    private void displayAccountInfo(UserManager.Account selectedAccount) {
        //Visar kontoinformationen
        accountInfo =
                "Account Information:\n"
                + "\nAccount Name: " + selectedAccount.getAccountName()
                + "\nAccount Number: " + selectedAccount.getAccountNr()
                + "\nBalance: " + selectedAccount.getBalance() + "\n---------------------------------------------------\n";

        //Visar Användarinformation och kontoinformation
        InformationText.setText(userInfo + accountInfo);
    }

    private void displayTransactionAndPaymentInfo(List<String> transactionHistory, List<String> paymentHistory) {
        StringBuilder infoBuilder = new StringBuilder();

        // Display transaction history
        infoBuilder.append("Transactions:\n\n");
        if (!transactionHistory.isEmpty()) {
            for (String transactionRecord : transactionHistory) {
                infoBuilder.append(transactionRecord).append("\n");
            }
        } else {
            infoBuilder.append("No Transaction History found...\n").append("\n---------------------------------------------------\n");
        }

// Display payment history
        infoBuilder.append("\n---------------------------------------------------\n");
        infoBuilder.append("Payments:\n");
        if (!paymentHistory.isEmpty()) {
            for (String paymentRecord : paymentHistory) {
                infoBuilder.append(paymentRecord).append("\n");
            }
        } else {
            infoBuilder.append("No Payment History found...\n").append("\n---------------------------------------------------\n");
        }

        InformationText.setText(userInfo + accountInfo + infoBuilder.toString());
    }

    //Metod för att koppla rätt konto till rätt namn
    private UserManager.Account findAccountByName(UserManager selectedUser, String accountName) {
        for (UserManager.Account account : selectedUser.getAccounts()) {
            if (account.getAccountName().equals(accountName)) {
                return account;
            }
        }
        return null; // Konto hittades inte
    }

}