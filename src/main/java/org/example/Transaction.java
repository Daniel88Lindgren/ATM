package org.example;

import javax.swing.*;
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
    private DefaultListModel<String> transactionHistoryListModel = new DefaultListModel<>();
    private JFrame transactionFrame;

    public Transaction() {
        setupUI();
        attachEventHandlers();
        loadTransactionHistory(); // Ladda historiken när fönstret öppnas
    }

    private void setupUI() {
        transactionFrame = new JFrame("Transaction window");
        transactionFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        transactionFrame.setContentPane(Transaction1);
        transactionFrame.setSize(500, 500);
        transactionFrame.setLocationRelativeTo(null);
        transactionFrame.setVisible(true);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        transactionFrame.setIconImage(icon.getImage());
        list1.setModel(transactionHistoryListModel);
    }

    private void attachEventHandlers() {
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
        try {
            int sourceAccountNumber = Integer.parseInt(textField1.getText());
            int destinationAccountNumber = Integer.parseInt(textField2.getText());
            double amountToTransfer = Double.parseDouble(textField3.getText());

            List<UserManager.Account> accounts = UserManager.getCurrentUserAccounts();

            UserManager.Account sourceAccount = null, destinationAccount = null;
            for (UserManager.Account account : accounts) {
                if (account.getAccountNr() == sourceAccountNumber) {
                    sourceAccount = account;
                } else if (account.getAccountNr() == destinationAccountNumber) {
                    destinationAccount = account;
                }
            }

            if (sourceAccount == null || destinationAccount == null) {
                confirmLabel.setText("Invalid account number(s), please try again");
                return;
            }

            if (sourceAccount.getBalance() >= amountToTransfer) {
                sourceAccount.withdraw(amountToTransfer);
                destinationAccount.deposit(amountToTransfer);
                addTransactionToHistory(sourceAccountNumber, destinationAccountNumber, amountToTransfer);
                confirmLabel.setText("You successfully made a transfer");
            } else {
                confirmLabel.setText("You don't have enough money, check your balance!");
            }
        } catch (NumberFormatException ex) {
            confirmLabel.setText("Invalid input! Please enter valid numbers.");
        }
    }

    private void addTransactionToHistory(int sourceAccount, int destinationAccount, double amount) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        String record = formattedDate + " - Transferred " + amount +
                " from account " + sourceAccount + " to " + destinationAccount;
        transactionHistoryListModel.addElement(record);
        UserManager.getCurrentUser().addTransactionRecord(record); // Spara transaktionshistoriken i UserManager
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
    // Lägg till metoden för att rensa transaktionshistoriken om användaren loggar ut
    public void clearTransactionHistory() {
        transactionHistoryListModel.clear();
    }
}

