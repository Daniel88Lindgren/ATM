package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PaymentWindow {
    private JPanel Payment1;
    private JTextField FromAccountField;
    private JTextField ToAccountField;
    private JTextField AmountField;
    private JTextField ocrNumberField;
    private JButton PaymentButton;
    private JTextPane displayPaymentHistoryTextPane;
    private JButton backToMenuButton;
    private JTable Paymenthistorytable;
    private DefaultTableModel paymentHistoryModel;
    private JFrame frame;

    public PaymentWindow() {
        JFrame frame = new JFrame("Payment Window");
        frame.setContentPane(Payment1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize( 400, 250);
        frame.pack();
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        frame.setIconImage(icon.getImage());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        initializePaymentHistory();

        PaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processPayment();
            }
        });

        backToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new MainMenu();
            }
        });
    }

    private void initializePaymentHistory() {

        // Skapa kolumnnamn för JTable
        String[] columnNames = {"Date", "From Account", "To Account", "Amount", "OCR Number"};
        paymentHistoryModel = new DefaultTableModel(columnNames, 0);
        Paymenthistorytable.setModel(paymentHistoryModel);
    }


    private List<Payment> paymentHistoryList = new ArrayList<>();

    private void processPayment() {
        String fromAccount = FromAccountField.getText();
        String toAccount = ToAccountField.getText();
        String amountText = AmountField.getText();
        String ocr = ocrNumberField.getText();

        try {
            int amount = Integer.parseInt(amountText);
            Payment payment = new Payment(fromAccount, toAccount, amount, ocr);
            if (payment.isValid()) {
                // Om valideringen är ok, visa ett framgångsmeddelande
                JOptionPane.showMessageDialog(frame, "Payment successful from " + fromAccount + " to " + toAccount + " for " + amount + " with OCR " + ocr, "Payment Processed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Om valideringen misslyckas, visa ett felmeddelande
                JOptionPane.showMessageDialog(frame, "Invalid payment details provided", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number for amount", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Inre klass för att hantera betalningsinformation och validering.
    private class Payment {
        private String fromAccount;
        private String toAccount;
        private int amount;
        private String ocrNumber;

        public Payment(String fromAccount, String toAccount, int amount, String ocrNumber) {
            this.fromAccount = fromAccount;
            this.toAccount = toAccount;
            this.amount = amount;
            this.ocrNumber = ocrNumber;
        }

        public boolean isValid() {
            // Kontrollerar att kontonumren endast innehåller siffror och att beloppet är större än noll.
            if (!fromAccount.matches("\\d+") || !toAccount.matches("\\d+") || amount <= 0) {
                return false;
            }
            // tänker lägga till ytterligare validering för OCR-numret.
            return true;
        }
    }


    public static void main(String[] args) {
        new PaymentWindow();
    }
}