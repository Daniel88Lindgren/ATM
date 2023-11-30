package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Account {

    private JButton accountInfoButton;
    private JButton changeAccountDetailsButton;
    private JButton backToMenuButton;
    private JPanel accountJPanel;
    private JButton changePasswordButton;
    private JButton createNewAccountButton;


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
                accountFrame.setVisible(false);
                new AccountDisplay();

            }
        });



        backToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountFrame.setVisible(false);
                new MainMenu();
            }
        });
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        createNewAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountFrame.setVisible(false);
                new NewAccountByUser();
            }
        });
    }



    }







