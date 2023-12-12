package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentWindow {
    private JPanel Payment1;
    private JTextField ocrNumberField;
    private JButton PaymentButton;
    private JButton backToMenuButton;
    private JTable Paymenthistorytable;
    private JList usersAccounts;
    private JScrollPane paymentHistoryScroll;
    private JList list1;
    private JLabel paymentMessage;
    private DefaultTableModel paymentHistoryModel;
    private JFrame frame;
    // List to store generated bills
    private List<Bill> bills;
    //List history for paid bills
    private final DefaultListModel<String> paymentHistoryListModel = new DefaultListModel<>();



    public PaymentWindow() {


        // Initialize GUI components
        frame = new JFrame("Payment Window");
        frame.setContentPane(Payment1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setVisible(true);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        frame.setIconImage(icon.getImage());
        frame.setLocationRelativeTo(null);
        list1.setModel(paymentHistoryListModel);


        // Initialize the table model
        paymentHistoryModel = new DefaultTableModel();
        paymentHistoryModel.addColumn("OCR Number");
        paymentHistoryModel.addColumn("Amount");
        Paymenthistorytable.setModel(paymentHistoryModel);


        //Calling method
        populateAccountList();
        loadPaymentHistory();


        // Generate and display bills
        bills = generateBills();
        for (Bill bill : bills) {
            paymentHistoryModel.addRow(new Object[]{bill.getOcrNumber(), String.format("%.2f SEK", bill.getAmount())});
        }

        //Button to accept bill payment
        PaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePayment();
            }
        });

        //Button to get back to main menu
        backToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new MainMenu();
            }
        });

    }



    //Pre-added bills with OSC number and amount
    public List<Bill> generateBills() {
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


    public static class Bill {
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
                String formattedBalance = String.format("%.0f SEK", account.getBalance()); // Format balance with no decimal places and add the SEK sign
                listModel.addElement(account.getAccountNr() + " (" + account.getAccountName() + "): " + formattedBalance);
                // listModel.addElement(account.getAccountNr() + " (" + account.getAccountName() + ")");
            }
        }

        usersAccounts.setModel(listModel);
    }



    //Payment method
    private void handlePayment() {
        // Get the selected account from the usersAccounts list
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

        // Find the selected bill from the PaymenthistoryTable
        int selectedRowIndex = Paymenthistorytable.getSelectedRow();
        if (selectedRowIndex == -1) {
            JOptionPane.showMessageDialog(frame, "No bill selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selectedBillOcr = (String) Paymenthistorytable.getValueAt(selectedRowIndex, 0);

        // Replace comma with dot and then parse as Double
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        Number parsedNumber = null;
        try {
            parsedNumber = format.parse(Paymenthistorytable.getValueAt(selectedRowIndex, 1).toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        double selectedBillAmount = parsedNumber.doubleValue();


        Bill bill = findBillByOCRNumber(selectedBillOcr);

        // Conditions to not proceed with payment
        if (bill == null) {
            JOptionPane.showMessageDialog(frame, "The selected bill does not exist. Please check the selection and try again.", "Invalid Bill Selection", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double currentBalance = account.getBalance();
        if (currentBalance < selectedBillAmount) {
            JOptionPane.showMessageDialog(frame, "You do not have sufficient funds in your account to complete this payment.", "Insufficient Funds", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Accepted payment below

        // Deduct the bill amount from the account balance
        account.setBalance(currentBalance - selectedBillAmount);

        //Update balance
        populateAccountList();

        // Set the current date as the payment date
        bill.setPaymentDate(new Date());

        // Remove the paid bill from the list
        bills.remove(bill);

        // Refresh the table to reflect the bill removal
        refreshPaymentHistoryTable();

        // Display a success message with detailed information
        JOptionPane.showMessageDialog(frame, "Payment of " + String.format("%.2f SEK", selectedBillAmount) + " was successful to OCR: " + bill.getOcrNumber(), "Payment Successful", JOptionPane.INFORMATION_MESSAGE);

        // Record the payment
        addPaymentToHistory(bill, account);
    }

    //Method to add users payment history
    private void addPaymentToHistory(Bill bill, UserManager.Account userAccount) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        String record = formattedDate  + " - Paid: " + bill.getAmount()+ "SEK" + " to OCR: " + bill.getOcrNumber();
        paymentHistoryListModel.addElement(record);

        if (userAccount != null) {
            // Add payment record to the specified user account
            userAccount.addPaymentRecord(record);
        } else {
            // Handle the case where the user account is not provided
            System.out.println("No user account found.");
        }
    }

    private void loadPaymentHistory() {
        UserManager currentUser = UserManager.getCurrentUser();
        if (currentUser != null) {
            List<String> paymentHistory = currentUser.getPaymentHistory();
            for (String paymentRecord : paymentHistory) {
                paymentHistoryListModel.addElement(paymentRecord);
            }
        }
    }


    //Method to extract the account number from the selected string
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

    //Method to refresh payment history in the table.
    //Clears existing rows in the payment history table and repopulates it with the current set of bills
    private void refreshPaymentHistoryTable() {
        paymentHistoryModel.setRowCount(0);
        for (Bill bill : bills) {
            paymentHistoryModel.addRow(new Object[]{bill.getOcrNumber(), bill.getAmount()});
        }
    }



}
