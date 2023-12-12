package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Account {

    private JButton accountInfoButton;
    private JButton backToMenuButton;
    private JPanel accountJPanel;
    private JButton changePasswordButton;
    private JButton createNewAccountButton;


    //Menu for users account
    public Account() {

        // Initialize GUI components

        JFrame accountFrame = new JFrame("Account");
        accountFrame.setVisible(true);
        accountFrame.setSize(200, 300);
        accountFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        accountFrame.setContentPane(accountJPanel);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        accountFrame.setIconImage(icon.getImage());
        accountFrame.setLocationRelativeTo(null);



        //Button for displaying logged-in user accounts
        accountInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountFrame.setVisible(false);
                new AccountDisplay();

            }
        });

        //Button to get back to main menu
        backToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountFrame.setVisible(false);
                new MainMenu();
            }
        });

        //Button for user to change password
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                accountFrame.setVisible(false);
                new PasswordChangeByUser();
            }
        });

        //Button for user to create new account
        createNewAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountFrame.setVisible(false);
                new NewAccountByUser();
            }
        });
    }



    }







