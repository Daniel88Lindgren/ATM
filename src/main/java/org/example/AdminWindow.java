package org.example;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminWindow {


    private JPanel AdminWindow;
    private JList AccountSelection;
    private JList UserSelection;
    private JButton AdminMainMenuButton;
    private JTextPane InformationText;
    private JLabel AccountLabel;
    private JLabel InformationLabel;
    private JLabel UsersLabel;
    private UserManager selectedUser;

    public AdminWindow() {
        JFrame jFrame = new JFrame("Admin Window");
        jFrame.setVisible(true);
        jFrame.setSize(500, 500);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setContentPane(AdminWindow);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        jFrame.setIconImage(icon.getImage());
        jFrame.setLocationRelativeTo(null);

        AdminMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu();
            }
        });

        List<UserManager> users = UserManager.getUsers();

        DefaultListModel<String> userModel = new DefaultListModel<>();
        for (UserManager userManager : users) {
            userModel.addElement(userManager.getUsername());

        }

        UserSelection.setModel(userModel);

        UserSelection.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedUsername = (String) UserSelection.getSelectedValue();

                    updateAccountList(selectedUsername);

                    UserManager selectedUser = findUserByUsername(selectedUsername);
                    if (selectedUser != null) {
                        displayUserInfo(selectedUser);
                    }
                }
            }
        });


        AccountSelection.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && selectedUser != null) {

                    String selectedAccountName = (String) AccountSelection.getSelectedValue();
                    if (selectedAccountName != null) {
                        UserManager.Account selectedAccount = findAccountByName(selectedUser, selectedAccountName);
                        if (selectedAccount != null) {
                            displayAccountInfo(selectedAccount);
                        }
                    }
                }
            }
        });
    }

    private void updateAccountList(String username) {
        DefaultListModel<String> accountListModel = new DefaultListModel<>();
        selectedUser = findUserByUsername(username);

        if (selectedUser != null) {
            for (UserManager.Account account : selectedUser.getAccounts()) {
                accountListModel.addElement(account.getAccountName());
            }
        }

        AccountSelection.setModel(accountListModel);

        // Display user information
        displayUserInfo(selectedUser);
    }

    private UserManager findUserByUsername(String username) {
        for (UserManager userManager : UserManager.getUsers()) {
            if (userManager.getUsername().equals(username)) {
                return userManager;
            }
        }
        return null; // User not found
    }

    private void displayUserInfo(UserManager selectedUser){
        InformationText.setText(("User: " + selectedUser.getUsername() + "\nPassword: " + selectedUser.getPassword()));
    }
    private void displayAccountInfo(UserManager.Account selectedAccount){
        InformationText.setText(("Account Name: " + selectedAccount.getAccountName()
                + "\nAccount Number: " + selectedAccount.getAccountNr()
                + "\nBalance: " + selectedAccount.getBalance()));
    }
    private UserManager.Account findAccountByName(UserManager selectedUser, String accountName) {
        for (UserManager.Account account : selectedUser.getAccounts()) {
            if (account.getAccountName().equals(accountName)) {
                return account;
            }
        }
        return null; // Account not found
    }
}
