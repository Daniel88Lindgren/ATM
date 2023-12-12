package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Transaction {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton confirmTransferButton;
    private JLabel confirmLabel;
    private JList<String> list1;
    private JPanel Transaction1;
    private JButton backToMenuButton;

    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JSpinner spinner1;

    private DefaultListModel<String> transactionHistoryListModel = new DefaultListModel<>();
    private JFrame transactionFrame;
    private List<UserManager.Account> userAccounts;

    public Transaction() {
        setupUI();
        populateComboBoxes();
        attachEventHandlers();
        loadTransactionHistory();
    }

    private void setupUI() {
        transactionFrame = new JFrame("Transaction window");
        transactionFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        transactionFrame.setContentPane(Transaction1);
        transactionFrame.setSize(550, 500);
        transactionFrame.setLocationRelativeTo(null);
        transactionFrame.setVisible(true);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        transactionFrame.setIconImage(icon.getImage());
        list1.setModel(transactionHistoryListModel);

    }
    private void populateComboBoxes() {
        List<UserManager.Account> userAccounts = UserManager.getCurrentUserAccounts();
        comboBox1.removeAllItems(); // Clear comboBox1
        comboBox2.removeAllItems(); // Clear comboBox2

        // Populate comboBox1
        for (UserManager.Account account : userAccounts) {
            String accountRepresentation = getAccountRepresentation(account);
            comboBox1.addItem(accountRepresentation);
        }

        // Add action listener to comboBox1
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear comboBox2
                comboBox2.removeAllItems();

                // Get the selected account from comboBox1
                String selectedAccountString = (String) comboBox1.getSelectedItem();
                UserManager.Account selectedAccount = getAccountFromString(selectedAccountString);

                // Populate comboBox2 excluding the selected account from comboBox1
                for (UserManager.Account account : userAccounts) {
                    if (account != selectedAccount) {
                        String accountRepresentation = getAccountRepresentation(account);
                        comboBox2.addItem(accountRepresentation);
                    }
                }
            }
        });
    }


    private void attachEventHandlers() {
        // befintliga händelselyssnare...

        confirmTransferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performTransfer();
            }
        });

        backToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transactionFrame.setVisible(false);
                new MainMenu();
            }
        });


    }


    private void performTransfer() {
        Color successGreen = new Color(30, 130, 76); // En mörkare, dämpad grön

        try {
            String sourceAccountString = (String) comboBox1.getSelectedItem();
            String destinationAccountString = (String) comboBox2.getSelectedItem();

            UserManager.Account sourceAccount = getAccountFromString(sourceAccountString);
            UserManager.Account destinationAccount = getAccountFromString(destinationAccountString);
            double amountToTransfer = Double.parseDouble(textField3.getText());

            if (sourceAccount == null || destinationAccount == null) {
                confirmLabel.setText("Invalid account selection, please try again");
                confirmLabel.setForeground(Color.RED);
                return;
            }

            if (sourceAccount.getBalance() >= amountToTransfer) {
                sourceAccount.withdraw(amountToTransfer);
                destinationAccount.deposit(amountToTransfer);

                addTransactionToHistory(sourceAccount, destinationAccount, amountToTransfer);

                confirmLabel.setText("You successfully made a transfer");
                confirmLabel.setForeground(successGreen);

                // Uppdatera comboBox1 och comboBox2
                comboBox1.removeAllItems();
                comboBox2.removeAllItems();
                populateComboBoxes();
            } else {
                confirmLabel.setText("You don't have enough money, check your balance!");
                confirmLabel.setForeground(Color.RED);
            }
        } catch (NumberFormatException ex) {
            confirmLabel.setText("Invalid input! Please enter valid numbers.");
            confirmLabel.setForeground(Color.RED);
        }
    }

    private void addTransactionToHistory(UserManager.Account sourceAccount, UserManager.Account destinationAccount, double amount) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        String record = formattedDate + " - Transferred " + amount +
                " from account " + sourceAccount.getAccountName() + " to " + destinationAccount.getAccountName();
        transactionHistoryListModel.addElement(record);
        // Add transaction record to source account
        sourceAccount.addTransactionRecord(getTransactionRecord(sourceAccount, destinationAccount, amount));

        // Add transaction record to destination account
        destinationAccount.addTransactionRecord(getTransactionRecord(sourceAccount, destinationAccount, amount));
    }
    private String getTransactionRecord(UserManager.Account sourceAccount, UserManager.Account destinationAccount, double amount) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        return formattedDate + " - Transferred " + amount +
                " from account " + sourceAccount.getAccountNr() + " to " + destinationAccount.getAccountNr();
    }


    private void loadTransactionHistory() {
        transactionHistoryListModel.clear(); // Rensa befintlig historik
        UserManager currentUser = UserManager.getCurrentUser();
        if (currentUser != null) {
            for (String record : currentUser.getTransactionHistory()) {
                transactionHistoryListModel.addElement(record);
            }
        }
    }
    private UserManager.Account getAccountFromString(String accountRepresentation) {
        List<UserManager.Account> userAccounts = UserManager.getCurrentUserAccounts();
        for (UserManager.Account account : userAccounts) {
            if ((account.getAccountName() + " - " + account.getAccountNr() + " (" + account.getBalance() + "kr)")
                    .equals(accountRepresentation)) {
                return account;
            }
        }
        return null; // No matching account found
    }
    private String getAccountRepresentation(UserManager.Account account) {
        return account.getAccountName() + " - " + account.getAccountNr() + " (" + account.getBalance() + "kr)";
    }
}

