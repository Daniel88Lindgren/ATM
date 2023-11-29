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

    public AdminWindow() {
        JFrame jFrame = new JFrame("Admin Window");
        jFrame.setVisible(true);
        jFrame.setSize(500, 500);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setContentPane(AdminWindow);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        jFrame.setIconImage(icon.getImage());
        jFrame.setLocationRelativeTo(null);

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
                    // Get the selected user
                    String selectedUsername = (String) UserSelection.getSelectedValue();

                    // Update the account list based on the selected user
                    updateAccountList(selectedUsername);
                }
            }
        });


        AdminMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu();
            }
        });
    }

    private void updateAccountList(String username) {
        DefaultListModel<String> accountListModel = new DefaultListModel<>();
        UserManager selectedUser = findUserByUsername(username);

        if (selectedUser != null) {
            for (UserManager.Account account : selectedUser.getAccounts()) {
                accountListModel.addElement(account.getAccountName());
            }
        }
        AccountSelection.setModel(accountListModel);
    }

    private UserManager findUserByUsername(String username) {
        for (UserManager userManager : UserManager.getUsers()) {
            if (userManager.getUsername().equals(username)) {
                return userManager;
            }
        }
        return null; // User not found
    }
}
