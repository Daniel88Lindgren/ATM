package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentWindow {
    private JPanel Payment1;
    private JTextField FromAccountField;
    private JTextField ocrNumberField;
    private JButton PaymentButton;
    private JButton backToMenuButton;
    private JTable Paymenthistorytable;
    private JList usersAccounts;
    private JLabel labelToFill1;
    private JLabel labelToFill2;
    private DefaultTableModel paymentHistoryModel;
    private JFrame frame;


    private UserManager currentUserManager;

    // List to store generated bills
    private List<Bill> bills;

    // List to store paid bills
    private List<Bill> paidBills;


    public List<Bill> getPaidBills() {
        return paidBills;
    }

    public void setPaidBills(List<Bill> paidBills) {
        this.paidBills = paidBills;
    }

    public PaymentWindow() {



        // Initialize the paid bills list
        paidBills = new ArrayList<>();

        // Set the currentUserManager
        this.currentUserManager = getCurrentUserManager();



        frame = new JFrame("Payment Window");
        frame.setContentPane(Payment1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setVisible(true);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        frame.setIconImage(icon.getImage());
        frame.setLocationRelativeTo(null);


        // Initialize the table model
        paymentHistoryModel = new DefaultTableModel();
        paymentHistoryModel.addColumn("OCR Number");
        paymentHistoryModel.addColumn("Amount");
        Paymenthistorytable.setModel(paymentHistoryModel);


        // Generate and display bills
        bills = generateBills(); // Generate and store bills
        for (Bill bill : bills) {
            paymentHistoryModel.addRow(new Object[]{bill.getOcrNumber(), bill.getAmount()});
        }




        PaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePayment();
            }
        });

        backToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new MainMenu();
            }
        });

        populateAccountList();



    }


    // Method to retrieve the current user's UserManager instance
    private static UserManager getCurrentUserManager() {
        return UserManager.getCurrentUser(); // Assuming there is a method to get the current user
    }


    //Pre-added bills with OSC number and amount
    private List<Bill> generateBills() {
        List<Bill> bills = new ArrayList<>();
        bills.add(new Bill("1122", 5999));
        bills.add(new Bill("3322", 8999));
        bills.add(new Bill("3344", 12000));
        bills.add(new Bill("4422", 550));
        bills.add(new Bill("5522", 299));
        bills.add(new Bill("6644", 120000));
        bills.add(new Bill("4477", 3300));
        bills.add(new Bill("7755", 45000));
        // ... Add the rest of the bills
        return bills;
    }


    private class Bill {
        private String ocrNumber;
        private double amount;
        private Date paymentDate;

        public Bill(String ocrNumber, double amount) {
            this.ocrNumber = ocrNumber;
            this.amount = amount;
            this.paymentDate = null;
        }

        public String getOcrNumber() {
            return ocrNumber;
        }

        public double getAmount() {
            return amount;
        }



        public Date getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(Date paymentDate) {
            this.paymentDate = paymentDate;
        }
    }




    // Method to find a bill by OCR number
    private Bill findBillByOCRNumber(String ocrNumber) {
        for (Bill bill : bills) {
            if (bill.getOcrNumber().equals(ocrNumber)) {
                return bill;
            }
        }
        return null; // Return null if no matching bill is found
    }



    // Method to populate the usersAccounts JList
    private void populateAccountList() {
        List<UserManager.Account> accounts = UserManager.getCurrentUserAccounts();
        DefaultListModel<String> listModel = new DefaultListModel<>();

        if (accounts.isEmpty()) {
            listModel.addElement("No accounts available");
        } else {
            for (UserManager.Account account : accounts) {
                listModel.addElement(account.getAccountNr() + " (" + account.getAccountName() + ")");
            }
        }

        usersAccounts.setModel(listModel);
    }



    //Payment method
    private void handlePayment() {
        String ocrNumber = ocrNumberField.getText();
        Bill bill = findBillByOCRNumber(ocrNumber);

        if (bill == null) {
            JOptionPane.showMessageDialog(frame, "Invalid OCR Number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selectedAccountStr = (String) usersAccounts.getSelectedValue();
        if (selectedAccountStr == null || selectedAccountStr.equals("No accounts available")) {
            JOptionPane.showMessageDialog(frame, "No account selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int accountNumber = extractAccountNumber(selectedAccountStr);
        UserManager.Account account = findAccountByNumber(accountNumber);

        if (account == null) {
            JOptionPane.showMessageDialog(frame, "Account not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double currentBalance = account.getBalance();
        if (currentBalance < bill.getAmount()) {
            JOptionPane.showMessageDialog(frame, "Insufficient funds", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }




        // Deduct the bill amount from the account balance
        account.setBalance(currentBalance - bill.getAmount());

        // Set the current date as the payment date
        bill.setPaymentDate(new Date());

        // Add the paid bill to the paid bills list
        paidBills.add(bill);

        // Remove the paid bill from the list
        bills.remove(bill);

        // Refresh the table to reflect the bill removal
        refreshPaymentHistoryTable();

        // Display a success message
        JOptionPane.showMessageDialog(frame, "Payment of " + bill.getAmount() + " was successful", "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
        ocrNumberField.setText("");
    }




    // Helper method to extract the account number from the selected string
    private int extractAccountNumber(String accountStr) {
        try {
            return Integer.parseInt(accountStr.split(" ")[0]);
        } catch (NumberFormatException e) {
            return -1; // Invalid format
        }
    }


    // Method to find an account by its number
    private UserManager.Account findAccountByNumber(int accountNumber) {
        List<UserManager.Account> accounts = UserManager.getCurrentUserAccounts();
        for (UserManager.Account account : accounts) {
            if (account.getAccountNr() == accountNumber) {
                return account;
            }
        }
        return null; // Return null if no matching account is found
    }

    private void refreshPaymentHistoryTable() {
        paymentHistoryModel.setRowCount(0); // Clear the existing rows
        for (Bill bill : bills) {
            paymentHistoryModel.addRow(new Object[]{bill.getOcrNumber(), bill.getAmount()});
        }
    }



}
