package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewAccountByUser {


    private JPanel NewAccountByUserJPanel;
    private JTextField EnterAccountName;
    private JTextField EnterAccountNr;
    private JTextField EnterAmount;

    private JButton cancel;
    private JButton createButton;


    public NewAccountByUser() {


        JFrame NewAccountByUserFrame = new JFrame("Create new account");
        NewAccountByUserFrame.setVisible(true);
        NewAccountByUserFrame.setSize(450, 300);
        NewAccountByUserFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        NewAccountByUserFrame.setContentPane(NewAccountByUserJPanel);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        NewAccountByUserFrame.setIconImage(icon.getImage());
        NewAccountByUserFrame.setLocationRelativeTo(null);


        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                NewAccountByUserFrame.setVisible(false);
                new Account();
            }
        });

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                createNewAccount();
                EnterAccountName.setText("");
                EnterAccountNr.setText("");
                EnterAmount.setText("");
            }
        });
    }


    //Method for user to create a new account with name, number and amount
    private void createNewAccount() {
        String inputAccountName = EnterAccountName.getText();
        int inputAccountNr;
        double inputAccountAmount;

        try {
            inputAccountNr = Integer.parseInt(EnterAccountNr.getText());
            inputAccountAmount = Double.parseDouble(EnterAmount.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid account number or amount format.");
            return;
        }

        UserManager currentUser = UserManager.getCurrentUser();
        boolean accountExists = false;
        for (UserManager.Account account : currentUser.getAccounts()) {
            if (account.getAccountNr() == inputAccountNr || account.getAccountName().equals(inputAccountName)) {
                accountExists = true;
                break;
            }
        }

        if (!accountExists) {
            currentUser.addAccount(inputAccountName, inputAccountNr, inputAccountAmount);
            JOptionPane.showMessageDialog(null, "Account created successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "An account with this number or name already exists.");
        }
    }


}
