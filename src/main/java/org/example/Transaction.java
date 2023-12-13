package org.example;// Import necessary Java Swing and time-related classes

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Main class for user transactions
public class Transaction {
    // Declaration of Swing components
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton confirmTransferButton;
    private JLabel confirmLabel;
    private JList<String> list1;
    private JPanel Transaction1;
    private JButton backToMenuButton;

    // ComboBox components for selecting source and destination for transfer
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;

    // Model for transaction history
    private DefaultListModel<String> transactionHistoryListModel = new DefaultListModel<>();

    // JFrame for the main window and list for user accounts
    private JFrame transactionFrame;
    private List<UserManager.Account> userAccounts;

    // Main method called when creating a new Transaction object
    public Transaction() {
        // Method to set up the user interface
        setupUI();

        // Method to populate ComboBoxes with user accounts
        populateComboBoxes();

        // Method to attach event handlers to the components
        attachEventHandlers();

        // Method to load transaction history
        loadTransactionHistory();
    }

    // Method to set up the user interface
    private void setupUI() {
        // Create the main window
        transactionFrame = new JFrame("Transaction window");
        transactionFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        transactionFrame.setContentPane(Transaction1);
        transactionFrame.setSize(600, 500);
        transactionFrame.setLocationRelativeTo(null);
        transactionFrame.setVisible(true);

        // Set icon for the window
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        transactionFrame.setIconImage(icon.getImage());

        // Set model for transaction history to JList
        list1.setModel(transactionHistoryListModel);
    }

    // Method to populate ComboBoxes with user accounts
    private void populateComboBoxes() {
        // Get user accounts for the current user
        List<UserManager.Account> userAccounts = UserManager.getCurrentUserAccounts();

        // Clear ComboBoxes
        comboBox1.removeAllItems();
        comboBox2.removeAllItems();

        // Populate ComboBox1
        for (UserManager.Account account : userAccounts) {
            String accountRepresentation = getAccountRepresentation(account);
            comboBox1.addItem(accountRepresentation);
        }

        // Add ActionListener for ComboBox1
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear ComboBox2
                comboBox2.removeAllItems();

                // Get the selected account from ComboBox1
                String selectedAccountString = (String) comboBox1.getSelectedItem();
                UserManager.Account selectedAccount = getAccountFromString(selectedAccountString);

                // Populate ComboBox2 excluding the selected account from ComboBox1
                for (UserManager.Account account : userAccounts) {
                    if (account != selectedAccount) {
                        String accountRepresentation = getAccountRepresentation(account);
                        comboBox2.addItem(accountRepresentation);
                    }
                }
            }
        });
    }

    // Method to attach event handlers to buttons and text fields
    private void attachEventHandlers() {
        // Event handler for the transfer button
        confirmTransferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the transfer
                performTransfer();
            }
        });

        // Event handler for the back-to-menu button
        backToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hide the window and create a new main menu
                transactionFrame.setVisible(false);
                new MainMenu();
            }
        });

        // Event handler for the third text field (amount)
        textField3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the transfer
                performTransfer();
            }
        });
    }

    // Method to perform the transfer
    private void performTransfer() {
        // Define color for success
        Color successGreen = new Color(30, 130, 76);

        try {
            // Get selected accounts and transfer amount
            String sourceAccountString = (String) comboBox1.getSelectedItem();
            String destinationAccountString = (String) comboBox2.getSelectedItem();
            UserManager.Account sourceAccount = getAccountFromString(sourceAccountString);
            UserManager.Account destinationAccount = getAccountFromString(destinationAccountString);
            double amountToTransfer = Double.parseDouble(textField3.getText());

            // Verify valid accounts
            if (sourceAccount == null || destinationAccount == null) {
                confirmLabel.setText("Invalid account selection, please try again");
                confirmLabel.setForeground(Color.RED);
                return;
            }

            // Perform the transfer if enough money is available in the source account
            if (sourceAccount.getBalance() >= amountToTransfer) {
                sourceAccount.withdraw(amountToTransfer);
                destinationAccount.deposit(amountToTransfer);

                // Add transaction history
                addTransactionToHistory(sourceAccount, destinationAccount, amountToTransfer);

                // Show transfer confirmation
                confirmLabel.setText("You successfully made a transfer");
                confirmLabel.setForeground(successGreen);

                // Update ComboBoxes
                comboBox1.removeAllItems();
                comboBox2.removeAllItems();
                populateComboBoxes();
            } else {
                // Show error message for insufficient balance
                confirmLabel.setText("You don't have enough money, check your balance!");
                confirmLabel.setForeground(Color.RED);
            }
        } catch (NumberFormatException ex) {
            // Show error message for invalid input
            confirmLabel.setText("Invalid input! Please enter valid numbers.");
            confirmLabel.setForeground(Color.RED);
        }
    }

    // Method to add transaction history
    private void addTransactionToHistory(UserManager.Account sourceAccount, UserManager.Account destinationAccount, double amount) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);

        // Create a transaction record and add it to the JList model
        String record = formattedDate + " - Transferred " + amount +
                " from account " + sourceAccount.getAccountName() + " to " + destinationAccount.getAccountName();
        transactionHistoryListModel.addElement(record);

        // Create a combined record for both source and destination accounts
        String combinedRecord = getTransactionRecord(sourceAccount, destinationAccount, amount);

        // Add the combined transaction record to both accounts
        sourceAccount.addTransactionRecord(combinedRecord);
        destinationAccount.addTransactionRecord(combinedRecord);
    }

    // Method to get a transaction record
    private String getTransactionRecord(UserManager.Account sourceAccount, UserManager.Account destinationAccount, double amount) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        return formattedDate + " - Transferred " + amount +
                " from account " + sourceAccount.getAccountNr() + " to " + destinationAccount.getAccountNr();
    }

    // Method to load transaction history
    private void loadTransactionHistory() {
        // Clear existing history
        transactionHistoryListModel.clear();
        Set<String> addedRecords = new HashSet<>();

        // Get the current user
        UserManager currentUser = UserManager.getCurrentUser();

        // Iterate through accounts and history for the user
        if (currentUser != null) {
            for (UserManager.Account account : currentUser.getAccounts()) {
                for (String transactionRecord : account.getTransactionHistory()) {
                    if (!addedRecords.contains(transactionRecord)) {
                        transactionHistoryListModel.addElement(transactionRecord);
                        addedRecords.add(transactionRecord);
                    }
                }
            }
        }
    }

    // Method to get a user account from user-friendly representation
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

    // Method to get user-friendly representation of an account
    private String getAccountRepresentation(UserManager.Account account) {
        return account.getAccountName() + " - " + account.getAccountNr() + " (" + account.getBalance() + "kr)";
    }
}

