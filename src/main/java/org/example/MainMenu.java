package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {
    private JButton paymentButton;
    private JButton accountButton;
    private JButton adminButton;
    private JButton transactionButton;
    private JPanel Window2;
    private JLabel Mainmenu;
    private JButton logoutButton1;
    private JButton BLbutton;

    public MainMenu() {
        JFrame jFrame = new JFrame("Main menu");
        jFrame.setVisible(true);
        jFrame.setSize(450, 300);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setContentPane(Window2);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        jFrame.setIconImage(icon.getImage());
        jFrame.setLocationRelativeTo(null);


        paymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PaymentWindow();

            }
        });

        accountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                new Account();


            }
        });

        transactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                new Transaction();
            }
        });
        logoutButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                LoginWindow loginWindow = new LoginWindow();
            }


        });
        BLbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Framtida överraskning från "Björnligan"!
                try {
                    jFrame.setVisible(false);
                    new BeagleBoys();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kollar ifall nuvarande användare är "admin". Annars har man inte tillgång till Admin Window.
                if (UserManager.getCurrentUser() != null &&
                        "admin".equals(UserManager.getCurrentUser().getUsername())) {
                    // Open the AdminWindow
                    jFrame.setVisible(false);
                    new AdminWindow();
                } else {
                    JOptionPane.showMessageDialog(null, "Access Denied! Only for Admin!");
                }
            }
        });
    }
}
