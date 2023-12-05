package org.example;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminWindow {

    private JButton settingsAdmin;
    private JPanel AdminWindow;
    private JList AccountSelection;
    private JList UserSelection;
    private JButton AdminMainMenuButton;
    private JTextPane InformationText;
    private JLabel AccountLabel;
    private JLabel InformationLabel;
    private JLabel UsersLabel;
    private UserManager selectedUser;
    private String userInfo = "";
    private String accountInfo = "";
    private String transactionHistory = "";
    private JComboBox<String> userComboBox;
    private UserManager userManager;

    public AdminWindow() {
        JFrame jFrame = new JFrame("Admin Window");
        jFrame.setVisible(true);
        jFrame.setSize(500, 500);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setContentPane(AdminWindow);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        jFrame.setIconImage(icon.getImage());
        jFrame.setLocationRelativeTo(null);

        userComboBox = new JComboBox<>();


        //Knappfunktion för tillbaka till Main Menu
        AdminMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                new MainMenu();
            }
        });

        //Skapar listan med användare, baserat på användarnamnet
        List<UserManager> users = UserManager.getUsers();

        DefaultListModel<String> userModel = new DefaultListModel<>();
        for (UserManager userManager : users) {
            userModel.addElement(userManager.getUsername());

        }

        UserSelection.setModel(userModel);

        //ListSelection för användare
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

        //listSelection till Konton
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

        settingsAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                new AdminSettings(selectedUser, userComboBox);
            }
        });
    }

    //Uppdaterar kontolistan när man väljer en ny användare
    private void updateAccountList(String username) {
        DefaultListModel<String> accountListModel = new DefaultListModel<>();
        selectedUser = findUserByUsername(username);

        if (selectedUser != null) {
            for (UserManager.Account account : selectedUser.getAccounts()) {
                accountListModel.addElement(account.getAccountName());
            }
        }

        AccountSelection.setModel(accountListModel);

        //Visa användar information
        displayUserInfo(selectedUser);
    }

    //Metod för att hitta Användare kopplat till Användarnamn,
    private UserManager findUserByUsername(String username) {
        for (UserManager userManager : UserManager.getUsers()) {
            if (userManager.getUsername().equals(username)) {
                return userManager;
            }
        }
        return null; //Användare hittades inte
    }

    //Visar Användare i TextPane
    private void displayUserInfo(UserManager selectedUser) {
        //Visar användar informationen
        userInfo = "User: " + selectedUser.getUsername() + "\nPassword: " + selectedUser.getPassword();

        //När man väljer ny användare så nollas displayAccountInfo
        accountInfo = "";

        //Visar Användarinformation och kontoinformation
        InformationText.setText(userInfo + accountInfo);
    }

    //Visar kontoinformationen i JList ACCOUNT
    private void displayAccountInfo(UserManager.Account selectedAccount) {
        //Visar kontoinformationen
        accountInfo = "\nAccount Name: " + selectedAccount.getAccountName()
                + "\nAccount Number: " + selectedAccount.getAccountNr()
                + "\nBalance: " + selectedAccount.getBalance();

        //Visar Användarinformation och kontoinformation
        InformationText.setText(userInfo + accountInfo);
    }

    //Metod för att koppla rätt konto till rätt namn
    private UserManager.Account findAccountByName(UserManager selectedUser, String accountName) {
        for (UserManager.Account account : selectedUser.getAccounts()) {
            if (account.getAccountName().equals(accountName)) {
                return account;
            }
        }
        return null; // Konto hittades inte
    }
}