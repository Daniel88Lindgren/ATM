package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LoginWindow {
    private JButton loginButton;
    private JTextField passwordField;
    private JTextField usernameField;
    private JPanel Window1;



    public LoginWindow() {


        JFrame jFrame = new JFrame("Login Window");
        jFrame.setVisible(true);
        jFrame.setSize(300, 150);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setContentPane(Window1);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        jFrame.setIconImage(icon.getImage());
        jFrame.setLocationRelativeTo(null);



        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                if (authenticate(username, password)) {
                    jFrame.setVisible(false);
                    new MainMenu();
                    // Proceed to next window or operation
                } else {
                    // Login failed
                    JOptionPane.showMessageDialog(null, "Login Failed");
                    // Show error message or clear fields
                }
            }
        });
    }

    private boolean authenticate(String username, String password) {
        return UserManager.authenticate(username, password);
    }



}

