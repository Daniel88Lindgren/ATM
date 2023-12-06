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
                RemovedMessage.setText("Account Removed: " + selectedUsername + " " + selectedAccountName);
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
                AddMessageUser.setText("User Added!");
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
            //hämta vald användare från combobox
            String selectedUsername = (String) AdminAddAccountComboBox.getSelectedItem();
            // hämta text för kontonamn
            String accountName = AdminAddAccountNameText.getText();

            // Kolla så inte fälten är tomma
            if (selectedUsername != null && !selectedUsername.isEmpty() && !accountName.isEmpty()) {
                try {
                    // Hämta balance från text
                    double initialBalance = Double.parseDouble(AdminAddBalanceText.getText());

                    // Hitta användare
                    UserManager selectedUser = findUserByUsername(selectedUsername);

                    // Kolla så användaren finns
                    if (selectedUser != null) {
                        // Skapa konto för vald användare och inskriven balance
                        selectedUser.adminAddAccount(accountName, initialBalance);

                        // Visa meddelande
                        AddMessageAccount.setText("Account Added to: " + selectedUsername + "!");
                    }
                } catch (NumberFormatException ex) {
                    // Hanterar att balance är korrekt inskrivet
                    AddMessageAccount.setText("Please enter a valid initial balance!");
                }
            }
        });

        //Klick tar bort text i textfield vid klick.
        AdminAddAccountNameText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AdminAddAccountNameText.setText("");
            }
        });
        //Klick tar bort text i textfield vid klick.
        AdminAddBalanceText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                AdminAddBalanceText.setText("");
            }
        });
        //Klick tar bort text i textfield vid klick.
        usernameTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                usernameTextField.setText("");
            }
        });
        //Klick tar bort text i textfield vid klick.
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

        //Byta lösenord hos användare
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

        //Knapp byta konto
        changeAccountNrButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hämta valt konto från combobox
                String selectedAccountName = (String) AccountSettingsComboBox.getSelectedItem();

                // Hämta nya konto numret från text
                String newAccountNr = AccountNrNewText.getText();

                // Kallar på metod för att uppdatera konto nr hos valt konto
                updateAccountNrForAccount(selectedUsername, selectedAccountName, newAccountNr);

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
                // Hämta valt konto namn
                String selectedAccountName = (String) AccountSettingsComboBox.getSelectedItem();
                // Visa vilket konto som finns i combobox för vald användare
                displayAccountName(selectedUsername, selectedAccountName);
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
    //Metod för att visa vald användares konto
    private void displayAccountName(String selectedUsername, String selectedAccountName) {
        UserManager.Account selectedAccount = findAccountForUser(selectedUsername, selectedAccountName);
        if (selectedAccount != null) {

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
        // Kalla metoder som uppdatera comboboxes
        populateUserComboBox();
        populateAdminAddComboBox();
        populateAccountComboBox(selectedUsername);
    }

    //Metod för att uppdatera konto nr
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
    //Metod för att se om konto nr är upptaget
    private boolean isAccountNrTaken(UserManager user, String newAccountNr) {
        for (UserManager.Account account : user.getAccounts()) {
            if (account.getAccountNr() == Integer.parseInt(newAccountNr)) {
                // Kontot är upptaget
                return true;
            }
        }
        return false; // Konto nummer ej Upptaget
    }
}