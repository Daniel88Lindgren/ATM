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

    public MainMenu() {
        JFrame jFrame = new JFrame("Main menu");
        jFrame.setVisible(true);
        jFrame.setSize(450, 200);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setContentPane(Window2);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        jFrame.setIconImage(icon.getImage());
        jFrame.setLocationRelativeTo(null);


        transactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Transaction();
            }
        });
    }
}
