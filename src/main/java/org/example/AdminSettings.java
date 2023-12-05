package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class AdminSettings {
    private JPanel AdminSettings;
    private JPanel AddAccountPanel;
    private JComboBox<String> UserSettingComboBox; // ComboBox to select user
    private JComboBox<String> AccountSettingsComboBox; // ComboBox to select account
    private JComboBox<String> AdminAddAccountComboBox;
    private JButton deleteUserSettingsButton; // Button to delete selected user
    private JButton deleteAccountSettingsButton; // Button to delete selected account
    private JButton AdminCreateUser; // Button to create user
    private JButton AdminCreateAccount; // Button to create account
    private JButton backSettingsButton;
    private JTextField usernameTextField; // Input for create user button
    private JTextField passwordTextField; // Input for create user button
    private JTextField AdminAddAccountNameText; // Input for create account button
    private JTextField AdminAddBalanceText; // Input for create account button
    private JLabel UserSettingsLabel;
    private JLabel AccountSettingsLabel;
    private JLabel AddUserSettingsLabel;
    private JLabel AddMessage;
    private JLabel RemovedMessage;
    private JButton changePasswordButton;
    private JButton changeAccountNrButton;
    private JTextField NewPasswordText;
    private JTextField AccountNrNewText;
    private String selectedUsername;
    private String selectedAccount;
    private UserManager userManager;
    private JComboBox<String> userComboBox;



    public AdminSettings(UserManager userManager, JComboBox<String> userComboBox) {
        this.userManager = userManager;
        this.userComboBox = userComboBox;


        JFrame jFrame = new JFrame("Admin Settings");
        jFrame.setVisible(true);
        jFrame.setSize(700, 300);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setContentPane(AdminSettings);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        jFrame.setIconImage(icon.getImage());
        jFrame.setLocationRelativeTo(null);



        initComboBoxes();


        deleteUserSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Hitta vald användare
                String selectedUsername = (String) UserSettingComboBox.getSelectedItem();
                // Koppla användare till användarnamn
                UserManager selectedUser = findUserByUsername(selectedUsername);
                // Tabort Användare
                UserManager.removeUser(selectedUser);
                // Uppdatera ComboBox
                updateComboBoxes();
                RemovedMessage.setText("User Removed!");
                RemovedMessage.setForeground(Color.RED);
            }
        });

        deleteAccountSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Hitta Konto
                String selectedAccountName = (String) AccountSettingsComboBox.getSelectedItem();
                // Hitta objektet
                UserManager selectedUser = findUserByUsername(selectedUsername);
                // Hitta kopplade kontot till användare
                UserManager.Account selectedAccount = findAccountForUser(selectedUsername, selectedAccountName);
                // Tabort konto från användare
                selectedUser.removeAccount(selectedAccount);
                // Uppdatera alla ComboBoxes
                updateComboBoxes();
                RemovedMessage.setText("Account Removed!");
                RemovedMessage.setForeground(Color.RED);

            }
        });

        AdminCreateUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();

                //Kolla om användare finns
                if (usernameExists(username)) {
                    System.out.println("Username already exists!");
                }
                else {
                    // Om användaren inte finns, skapas en ny
                    UserManager newUser = new UserManager(username, password);
                    UserManager.addUser(newUser);
                }
                //Skriv Meddelande i fönstret och uppdatera combobox
                AddMessage.setText("User Added!");
                updateComboBoxes();

            }
            //Metod att kolla om användaren finns
            private boolean usernameExists(String username) {
                for (UserManager user : UserManager.getUsers()) {
                    if (user.getUsername().equals(username)) {
                        return true;
                    }
                }
                return false;
            }

        });

        AdminCreateAccount.addActionListener(e -> {
            //Ta vald användare från combobox
            String selectedUsername = (String) AdminAddAccountComboBox.getSelectedItem();
            //Ta inskrivna användarnamnet från textfield
            String accountName = AdminAddAccountNameText.getText();

            //Check så inget fält är tomt.
            if (selectedUsername != null && !selectedUsername.isEmpty() && !accountName.isEmpty()) {
                //Hitta användare
                UserManager selectedUser = findUserByUsername(selectedUsername);
                // Kolla så att användaren finns
                if (selectedUser != null) {
                    // Skapa konto till vald användare. med balance 0.
                    double initialBalance = 0;
                    selectedUser.adminAddAccount(accountName, initialBalance);
                    // Display success message or update UI accordingly
                    AddMessage.setText("Account Added to: " + selectedUsername + "!");
                }

            }
            // Uppdatera combobox
            updateComboBoxes();


        });

        //Klick tar bort text i textfield.
        AdminAddAccountNameText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AdminAddAccountNameText.setText("");
            }
        });
        //Klick tar bort text i textfield.
        AdminAddBalanceText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AdminAddBalanceText.setText("");
            }
        });
        //Klick tar bort text i textfield.
        usernameTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                usernameTextField.setText("");
            }
        });
        //Klick tar bort text i textfield.
        passwordTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                passwordTextField.setText("");
            }
        });
        // Tar bort fönstret när man går tillbaka till Admin Window
        backSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                new AdminWindow();
            }
        });
        changePasswordButton.addActionListener(new ActionListener() {
            Color successGreen = new Color(30, 130, 76);

            @Override
            public void actionPerformed(ActionEvent e) {
                // Välj användare från combobox
                String selectedUsername = (String) UserSettingComboBox.getSelectedItem();


                if (selectedUsername != null && !selectedUsername.isEmpty()) {
                    // Tar det inskrivna lösenordet från text
                    String newPassword = NewPasswordText.getText();

                    // Uppdaterar lösenordet hos användaren
                    updatePasswordForUser(selectedUsername, newPassword);


                    NewPasswordText.setText("");
                    RemovedMessage.setText("Password changed for : " + selectedUsername);
                    RemovedMessage.setForeground(successGreen);

                }
            }

            // Metod för att uppdatera användarens lösenord
            private void updatePasswordForUser(String username, String newPassword) {
                UserManager userToUpdate = findUserByUsername(username);

                if (userToUpdate != null) {
                    userToUpdate.setPassword(newPassword);

                }
            }
        });
        changeAccountNrButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected account name from the AccountComboBox
                String selectedAccountName = (String) AccountSettingsComboBox.getSelectedItem();

                // Get the new account number from the AccountNrNewText field
                String newAccountNr = AccountNrNewText.getText();

                // Call a method to update the account number for the selected account
                updateAccountNrForAccount(selectedUsername, selectedAccountName, newAccountNr);

                // Clear the account number text field
                AccountNrNewText.setText("");
            }
        });
    }

    private void initComboBoxes() {
        // Sätt in användare i "USER" cobobox.
        populateUserComboBox();
        populateAdminAddComboBox();

        // action listener till vald user så kan man välja dess konto i account combobox. (Combobox till vänster)
        UserSettingComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedUsername = (String) UserSettingComboBox.getSelectedItem();

                populateAccountComboBox(selectedUsername);
            }
        });

        // Välja konto i account combobox
        AccountSettingsComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected account name
                String selectedAccountName = (String) AccountSettingsComboBox.getSelectedItem();

                // Display account information based on the selected account name
                displayAccountInfo(selectedUsername, selectedAccountName);
            }
        });
        //ComboBox för att lägga till användare
        AdminAddAccountComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedUsername = (String) AdminAddAccountComboBox.getSelectedItem();
            }
        });
    }
    //Method för att lägga till användare i USER combobox
    private void populateUserComboBox() {
        List<String> usernames = new ArrayList<>();
        for (UserManager user : userManager.getUsers()) {
            usernames.add(user.getUsername());
        }
        DefaultComboBoxModel<String> userModel = new DefaultComboBoxModel<>(usernames.toArray(new String[0]));
        UserSettingComboBox.setModel(userModel);
    }
    //Metod för att lägga till USERS i combobox till höger
    private void populateAdminAddComboBox() {
        List<String> usernames = new ArrayList<>();
        for(UserManager user : userManager.getUsers()) {
            usernames.add(user.getUsername());
        }
        DefaultComboBoxModel<String> userModel = new DefaultComboBoxModel<>(usernames.toArray(new String[0]));
        AdminAddAccountComboBox.setModel(userModel);
    }
    //Metod för att lägga till Konto i Account combobox
    private void populateAccountComboBox(String selectedUsername) {
        if (selectedUsername != null) {
            UserManager selectedUser = findUserByUsername(selectedUsername);
            if (selectedUser != null) {
                List<String> accountNames = new ArrayList<>();
                for (UserManager.Account account : selectedUser.getAccounts()) {
                    accountNames.add(account.getAccountName());
                }
                DefaultComboBoxModel<String> accountModel = new DefaultComboBoxModel<>(accountNames.toArray(new String[0]));
                AccountSettingsComboBox.setModel(accountModel);
            }
        }
    }

    private void displayAccountInfo(String selectedUsername, String selectedAccountName) {
        UserManager.Account selectedAccount = findAccountForUser(selectedUsername, selectedAccountName);
        if (selectedAccount != null) {
            // Update your GUI to display the account information
            // For example, set labels or update a text area with the account details
            // Example:
            // accountInfoTextArea.setText("Account Name: " + selectedAccount.getAccountName() + "\nAccount Number: " + selectedAccount.getAccountNr() + "\nBalance: " + selectedAccount.getBalance());
        }
    }
    //Metod hitta användare genom Username
    private UserManager findUserByUsername(String username) {
        for (UserManager user : userManager.getUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    //Metod hitta konto tillhörande användare
    private UserManager.Account findAccountForUser(String selectedUsername, String selectedAccountName) {
        UserManager selectedUser = findUserByUsername(selectedUsername);
        if (selectedUser != null) {
            for (UserManager.Account account : selectedUser.getAccounts()) {
                if (account.getAccountName().equals(selectedAccountName)) {
                    return account;
                }
            }
        }
        return null;
    }
    //Metod uppdaterar comboboxarna efter ändringar
    public void updateComboBoxes() {
        // Call methods to update ComboBoxes or other components
        populateUserComboBox();
        populateAdminAddComboBox();
        populateAccountComboBox(selectedUsername);
        // Add other update logic as needed
    }
    private void updateAccountNrForAccount(String username, String accountName, String newAccountNr) {
        Color successGreen = new Color(30, 130, 76);
        // Hitta användare
        UserManager userToUpdate = findUserByUsername(username);

        if (userToUpdate != null) {
            UserManager.Account accountToUpdate = findAccountForUser(username, accountName);

            if (accountToUpdate != null) {
                // kolla om kontot redan är taget
                if (!isAccountNrTaken(userToUpdate, newAccountNr)) {
                    RemovedMessage.setText("Account Nr Updated!");
                    RemovedMessage.setForeground(successGreen);
                    // Om inte är taget, uppdatera
                    accountToUpdate.setAccountNr(Integer.parseInt(newAccountNr));
                } else {
                    // Om upptaget, skicka meddelande.
                    RemovedMessage.setText("Account number is already taken. Choose a different one.");
                    RemovedMessage.setForeground(Color.RED);
                }
            }
        }
    }
    private boolean isAccountNrTaken(UserManager user, String newAccountNr) {
        for (UserManager.Account account : user.getAccounts()) {
            if (account.getAccountNr() == Integer.parseInt(newAccountNr)) {
                return true; // Account number is already taken
            }
        }
        return false; // Account number is not taken
    }

}