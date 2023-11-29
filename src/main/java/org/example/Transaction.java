package org.example;

import javax.swing.*;

public class Transaction {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton confirmTransferButton;
    private JLabel confirmLabel;
    private JList list1;
    private JButton yesButton;
    private JPanel Transaction1;

    public Transaction(){
        JFrame jFrame = new JFrame("Transaction window");
        jFrame.setVisible(true);
        jFrame.setSize(400,300);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setContentPane(Transaction1);
        ImageIcon icon = new ImageIcon(getClass().getResource("/dollarSymbol.jpg"));
        jFrame.setIconImage(icon.getImage());
        jFrame.setLocationRelativeTo(null);
    }
}

