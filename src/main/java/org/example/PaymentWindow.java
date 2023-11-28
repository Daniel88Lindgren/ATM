package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentWindow {
    private JPanel Payment1;
    private JTextField FromAccountField;
    private JTextField ToAccountField;
    private JTextField AmountField;
    private JTextField ocrNumberField;
    private JButton PaymentButton;
    private JFrame frame;

    public PaymentWindow() {
        JFrame frame = new JFrame("Payment Window");
        frame.setContentPane(Payment1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        PaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processPayment();
            }
        });
    }
    /**
     * Processar en betalningsförfrågan baserad på användarinmatningen.
     * Hämtar text från användargränssnittet och skapar ett Payment-objekt.
     * Validerar betalningsuppgifterna och ger användaren feedback via dialogrutor.
     * Visar ett framgångsmeddelande om betalningen är giltig eller ett felmeddelande annars.
     * Hanterar NumberFormatException för att säkerställa att beloppet är en giltig siffra.
     */
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