package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LoginWindow {
    private JButton loginButton;
    private JTextField passwordField;
    private JTextField usernameField;
    private JPanel Window1;

    private ArrayList<UserList> users = new ArrayList<>();

    public LoginWindow() {
        JFrame jFrame = new JFrame("Login Window");
        jFrame.setVisible(true);
        jFrame.setSize(300, 150);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setContentPane(Window1);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        jFrame.setIconImage(icon.getImage());
        jFrame.setLocationRelativeTo(null);

        // Lägger till användare i listan.
        users.add(new UserList("lars", "123"));
        users.add(new UserList("arta", "234"));
        users.add(new UserList("daniel", "345"));
        users.add(new UserList("mickey", "456"));
        users.add(new UserList("anders", "567"));

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().toLowerCase();
                String password = passwordField.getText();
                if (authenticate(username, password)) {
                    jFrame.setVisible(false);
                    new MainMenu();
                } else {
                        JOptionPane.showMessageDialog(jFrame,"Wrong password or usename, Please try again!");
                        usernameField.setText("");
                        passwordField.setText("");
                }
            }
        });
    }
    private boolean authenticate(String username, String password) {
        for (UserList user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}

