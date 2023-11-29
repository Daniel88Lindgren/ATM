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

    public AdminWindow(List<UserManager> users) {
        this.users = users;
        JFrame jFrame = new JFrame("Admin Window");
        jFrame.setVisible(true);
        jFrame.setSize(500, 500);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setContentPane(AdminWindow);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        jFrame.setIconImage(icon.getImage());
        jFrame.setLocationRelativeTo(null);

        DefaultListModel<String> userModel = new DefaultListModel<>();
        for (UserManager userManager : users) {
            userModel.addElement(UserManager.getUsername());
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

    private void updateAccountList(String username){
        DefaultListModel<Account> accountListModel = (DefaultListModel<Account>) AccountSelection.getModel();
        accountListModel.clear();

        // Find the selected user
        UserManager selectedUser = findUserByUsername(username);

        if (selectedUser != null) {
            // Display the user's accounts in AccountSelection
            for (Account account : selectedUser.getAccounts()) {
                accountListModel.addElement(account.getAccountType());
            }
        }
        AccountSelection.setModel(accountListModel);
    }
    private User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // User not found
    }

}
