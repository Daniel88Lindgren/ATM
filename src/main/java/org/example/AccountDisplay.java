package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountDisplay {


    private JButton backButton;
    private JPanel accountDisplayJPanel;
    private JLabel displayInfoTextfield;

    public AccountDisplay() {


        JFrame accountDisplayFrame = new JFrame("Your account info");
        accountDisplayFrame.setVisible(true);
        accountDisplayFrame.setSize(450, 300);
        accountDisplayFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        accountDisplayFrame.setContentPane(accountDisplayJPanel);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        accountDisplayFrame.setIconImage(icon.getImage());
        accountDisplayFrame.setLocationRelativeTo(null);






        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountDisplayFrame.setVisible(false);
                new Account();

            }
        });
    }
}
