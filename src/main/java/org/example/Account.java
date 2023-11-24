package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Account {

    private String accountName;
    private double balance;
    private int pinCode;
    private JButton accountInfoButton;
    private JButton changeAccountDetailsButton;
    private JButton backToMenuButton;
    private JButton somethingElseButtonButton;
    private JPanel accountJPanel;


    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }


    public Account() {

        JFrame accountFrame = new JFrame("Account");
        accountFrame.setVisible(true);
        accountFrame.setSize(450, 300);
        accountFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        accountFrame.setContentPane(accountJPanel);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        accountFrame.setIconImage(icon.getImage());
        accountFrame.setLocationRelativeTo(null);


        accountInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        changeAccountDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        backToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountFrame.setVisible(false);
                new MainMenu();
            }
        });
    }
}