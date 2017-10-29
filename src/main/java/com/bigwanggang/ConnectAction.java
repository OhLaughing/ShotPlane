package com.bigwanggang;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectAction implements ActionListener {
    private JTextField ipField;
    private JTextField portField;

    public ConnectAction(JTextField ipField, JTextField portField) {
        this.ipField = ipField;
        this.portField = portField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        System.out.println("ip:" + ipField.getText());
        System.out.println("port:" + portField.getText());
    }
}
