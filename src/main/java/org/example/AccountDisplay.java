package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AccountDisplay {



    private JButton backButton;
    private JPanel accountDisplayJPanel;
    private JLabel name;
    private JList accountDisplayList;
    private JScrollPane accountDisplayScroll;


    public AccountDisplay() {


        // Initialize GUI components

        JFrame accountDisplayFrame = new JFrame("Your account info");
        accountDisplayFrame.setVisible(true);
        accountDisplayFrame.setSize(400, 250);
        accountDisplayFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        accountDisplayFrame.setContentPane(accountDisplayJPanel);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        accountDisplayFrame.setIconImage(icon.getImage());
        accountDisplayFrame.setLocationRelativeTo(null);



        //Call method to display accounts
        displayUserAccounts();





        //Back to account menu
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
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (UserManager.Account account : accounts) {
            String accountInfo = account.getAccountName() +
                    ", Account nr: " + account.getAccountNr() +
                    ", Balance: " + String.format("%.2f", account.getBalance());
            listModel.addElement(accountInfo);
        }

        // Set the model for the existing JList
        accountDisplayList.setModel(listModel);

        // Set the JList to be the view for the JScrollPane
        accountDisplayScroll.setViewportView(accountDisplayList);
    }



}




