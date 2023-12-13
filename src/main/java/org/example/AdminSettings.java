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
    private JComboBox<String> UserSettingComboBox;
    private JComboBox<String> AccountSettingsComboBox;
    private JComboBox<String> AdminAddAccountComboBox;
    private JButton deleteUserSettingsButton;
    private JButton deleteAccountSettingsButton;
    private JButton AdminCreateUser;
    private JButton AdminCreateAccount;
    private JButton backSettingsButton;
    private JTextField usernameTextField;
    private JTextField passwordTextField;
    private JTextField AdminAddAccountNameText;
    private JTextField AdminAddBalanceText;
    private JLabel UserSettingsLabel;
    private JLabel AccountSettingsLabel;
    private JLabel AddUserSettingsLabel;
    private JLabel AddMessageUser;
    private JLabel RemovedMessage;
    private JButton changePasswordButton;
    private JButton changeAccountNrButton;
    private JTextField NewPasswordText;
    private JTextField AccountNrNewText;
    private JLabel AddMessageAccount;
    private String selectedUsername;
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

        //Delete user button
        deleteUserSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Find selected user
                String selectedUsername = (String) UserSettingComboBox.getSelectedItem();
                //Connect the selected user to username
                UserManager selectedUser = findUserByUsername(selectedUsername);
                //Delete User
                UserManager.removeUser(selectedUser);
                //Update Combobox
                updateComboBoxes();
                RemovedMessage.setText("User Removed!");
                RemovedMessage.setForeground(Color.RED);
            }
        });

        //Delete Account button
        deleteAccountSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Find Account
                String selectedAccountName = (String) AccountSettingsComboBox.getSelectedItem();
                //Connect selected user to username
                UserManager selectedUser = findUserByUsername(selectedUsername);
                //Connect selected account to user
                UserManager.Account selectedAccount = findAccountForUser(selectedUsername, selectedAccountName);
                //Delete account from user
                selectedUser.removeAccount(selectedAccount);
                //Update Combobox
                updateComboBoxes();
                RemovedMessage.setText("Account Removed: " + selectedUsername + " " + selectedAccountName);
                RemovedMessage.setForeground(Color.RED);

            }
        });

        //Create User button
        AdminCreateUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();

                //Check if user exists
                if (usernameExists(username)) {
                    System.out.println("Username already exists!");
                }
                else {
                    //If user doesnt exists, create a new user
                    UserManager newUser = new UserManager(username, password);
                    UserManager.addUser(newUser);
                }
                //Display message and update combobox
                AddMessageUser.setText("User Added!");
                updateComboBoxes();
            }
            //Method to check if user exists
            private boolean usernameExists(String username) {
                for (UserManager user : UserManager.getUsers()) {
                    if (user.getUsername().equals(username)) {
                        return true;
                    }
                }
                return false;
            }

        });

        //Create Account
        AdminCreateAccount.addActionListener(e -> {
            //Get selected user from combobox
            String selectedUsername = (String) AdminAddAccountComboBox.getSelectedItem();
            //Fetch text input from textfield
            String accountName = AdminAddAccountNameText.getText();

            //Check fields not empty
            if (selectedUsername != null && !selectedUsername.isEmpty() && !accountName.isEmpty()) {
                try {
                    //Get balance from textfield
                    double initialBalance = Double.parseDouble(AdminAddBalanceText.getText());

                    //Find user and if it exists
                    UserManager selectedUser = findUserByUsername(selectedUsername);

                    if (selectedUser != null) {
                        // Skapa konto för vald användare och inskriven balance
                        selectedUser.adminAddAccount(accountName, initialBalance);

                        //If exists display message
                        AddMessageAccount.setText("Account Added to: " + selectedUsername + "!");
                    }
                } catch (NumberFormatException ex) {
                    //Check if balance is input correctly
                    AddMessageAccount.setText("Please enter a valid initial balance!");
                }
            }
        });

        //Clicking textfield clears the text in all textfields
        AdminAddAccountNameText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AdminAddAccountNameText.setText("");
            }
        });

        AdminAddBalanceText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AdminAddBalanceText.setText("");
            }
        });

        usernameTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                usernameTextField.setText("");
            }
        });

        passwordTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                passwordTextField.setText("");
            }
        });

        //Back to admin window button
        backSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                new AdminWindow();
            }
        });

        //Change password for user
        changePasswordButton.addActionListener(new ActionListener() {
            Color successGreen = new Color(30, 130, 76);

            @Override
            public void actionPerformed(ActionEvent e) {
                //Select user from Combobox
                String selectedUsername = (String) UserSettingComboBox.getSelectedItem();


                if (selectedUsername != null && !selectedUsername.isEmpty()) {
                    //Fetch input for password
                    String newPassword = NewPasswordText.getText();

                    //Update password for user
                    updatePasswordForUser(selectedUsername, newPassword);


                    NewPasswordText.setText("");
                    RemovedMessage.setText("Password changed for : " + selectedUsername);
                    RemovedMessage.setForeground(successGreen);
                }
            }
            //Method to update user password
            private void updatePasswordForUser(String username, String newPassword) {
                UserManager userToUpdate = findUserByUsername(username);

                if (userToUpdate != null) {
                    userToUpdate.setPassword(newPassword);

                }
            }
        });

        //Change Account Nr
        changeAccountNrButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Get seleceted account from combobox
                String selectedAccountName = (String) AccountSettingsComboBox.getSelectedItem();

                //Fetch input for new account number
                String newAccountNr = AccountNrNewText.getText();

                //Calls method to uppdate account nr
                updateAccountNrForAccount(selectedUsername, selectedAccountName, newAccountNr);

                AccountNrNewText.setText("");
            }
        });
    }

    private void initComboBoxes() {
        //Populates combobox
        populateUserComboBox();
        populateAdminAddComboBox();

        //Combobox User (Left)
        UserSettingComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedUsername = (String) UserSettingComboBox.getSelectedItem();

                populateAccountComboBox(selectedUsername);
            }
        });

        // Combobox Account (Left)
        AccountSettingsComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hämta valt konto namn
                String selectedAccountName = (String) AccountSettingsComboBox.getSelectedItem();
                // Visa vilket konto som finns i combobox för vald användare
                displayAccountName(selectedUsername, selectedAccountName);
            }
        });

        //ComboBox add account
        AdminAddAccountComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedUsername = (String) AdminAddAccountComboBox.getSelectedItem();
            }
        });
    }

    //Method to populate combobox
    private void populateUserComboBox() {
        List<String> usernames = new ArrayList<>();
        for (UserManager user : userManager.getUsers()) {
            usernames.add(user.getUsername());
        }
        DefaultComboBoxModel<String> userModel = new DefaultComboBoxModel<>(usernames.toArray(new String[0]));
        UserSettingComboBox.setModel(userModel);
    }

    //Method populate combobox
    private void populateAdminAddComboBox() {
        List<String> usernames = new ArrayList<>();
        for(UserManager user : userManager.getUsers()) {
            usernames.add(user.getUsername());
        }
        DefaultComboBoxModel<String> userModel = new DefaultComboBoxModel<>(usernames.toArray(new String[0]));
        AdminAddAccountComboBox.setModel(userModel);
    }

    //Method populate combobox
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
    //Method to display selected users account
    private void displayAccountName(String selectedUsername, String selectedAccountName) {
        UserManager.Account selectedAccount = findAccountForUser(selectedUsername, selectedAccountName);
        if (selectedAccount != null) {

        }
    }

    //Method to find user based of username
    private UserManager findUserByUsername(String username) {
        for (UserManager user : userManager.getUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    //Method find account from selected user
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

    //Metod update combobox after changes
    public void updateComboBoxes() {
        // Kalla metoder som uppdatera comboboxes
        populateUserComboBox();
        populateAdminAddComboBox();
        populateAccountComboBox(selectedUsername);
    }

    //Metod update account number
    private void updateAccountNrForAccount(String username, String accountName, String newAccountNr) {
        Color successGreen = new Color(30, 130, 76);
        // Hitta användare
        UserManager userToUpdate = findUserByUsername(username);

        if (userToUpdate != null) {
            UserManager.Account accountToUpdate = findAccountForUser(username, accountName);

            // kolla om kontot redan är taget
            if (accountToUpdate != null) {
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
    //Method to check if account number is taken
    private boolean isAccountNrTaken(UserManager user, String newAccountNr) {
        for (UserManager.Account account : user.getAccounts()) {
            if (account.getAccountNr() == Integer.parseInt(newAccountNr)) {
                // Kontot är upptaget
                return true;
            }
        }
        return false; //Account number is not taken
    }
}