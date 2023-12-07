package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordChangeByUser {
    private JPanel PasswordChangeByUserJPanel;
    private JTextField enterNewPasswordTextField;
    private JButton cancelButton;
    private JButton applyButton;


    public PasswordChangeByUser() {

        JFrame PasswordChangeByUserFrame = new JFrame("Login Window");
        PasswordChangeByUserFrame.setVisible(true);
        PasswordChangeByUserFrame.setSize(300, 150);
        PasswordChangeByUserFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        PasswordChangeByUserFrame.setContentPane(PasswordChangeByUserJPanel);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        PasswordChangeByUserFrame.setIconImage(icon.getImage());
        PasswordChangeByUserFrame.setLocationRelativeTo(null);


        //Back to account menu
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PasswordChangeByUserFrame.setVisible(false);
                new Account();

            }
        });

        //User enters new password and press Ok
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePassword();
                enterNewPasswordTextField.setText("");

            }
        });
    }


    //Method to change password for the current logged-in user
    public void changePassword() {
        String newPassword = enterNewPasswordTextField.getText();
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            UserManager.updateCurrentUserPassword(newPassword);
            JOptionPane.showMessageDialog(PasswordChangeByUserJPanel, "Password updated successfully.");
        } else {
            JOptionPane.showMessageDialog(PasswordChangeByUserJPanel, "Please enter a valid new password.");
        }
    }


}
