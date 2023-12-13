package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BeagleBoys {

    private JPanel panel1;
    private JLabel imageLabel;
    private JButton BackToLoginWindow;
    private JFrame frame;
    private ImageIcon bIcon;

    // Initialize the GUI components
    public BeagleBoys() {
        frame = new JFrame("The Money is gone!!");
        frame.setSize(600, 357);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        panel1 = new JPanel();
        bIcon = new ImageIcon(getClass().getResource("/BL.jpg"));
        imageLabel.setSize(450, 257);
        imageLabel = new JLabel(bIcon);
        panel1.add(imageLabel);
        frame.setContentPane(panel1);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        BackToLoginWindow = new JButton("Back to Main Menu"); // Initialize the button
        panel1.add(BackToLoginWindow);

        BackToLoginWindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    frame.setVisible(false);
                    // Calls the next row to steal all the money and close the accounts
                    UserManager.clearCurrentUserAccounts();
                    new MainMenu();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
