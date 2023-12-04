package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AccountDisplay {



    private JButton backButton;
    private JPanel accountDisplayJPanel;
    private JLabel displayAccounts;
    private JLabel name;


    public AccountDisplay() {



        JFrame accountDisplayFrame = new JFrame("Your account info");
        accountDisplayFrame.setVisible(true);
        accountDisplayFrame.setSize(450, 300);
        accountDisplayFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        accountDisplayFrame.setContentPane(accountDisplayJPanel);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        accountDisplayFrame.setIconImage(icon.getImage());
        accountDisplayFrame.setLocationRelativeTo(null);


        displayUserAccounts(); // Method call to display accounts






        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountDisplayFrame.setVisible(false);
                new Account();
            }
        });
    }

    // Method to display the current user's accounts
    private void displayUserAccounts() {
        List<UserManager.Account> accounts = UserManager.getCurrentUserAccounts();
        StringBuilder accountsInfo = new StringBuilder("<html>"); // Using HTML for multiline
        for (UserManager.Account account : accounts) {
            accountsInfo.append("").append(account.getAccountName())
                    .append(", Account nr: ").append(account.getAccountNr())
                    .append(", Balance: ").append(String.format("%.2f", account.getBalance()))
                    .append("<br>"); // Line break for each account
        }
        accountsInfo.append("</html>"); // Closing HTML tag

        displayAccounts.setText(accountsInfo.toString()); // Setting text to JLabel

    }



}




