package com.bigwanggang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

public class ShotPlaneFrame extends JFrame {
    private JPanel controlPanel;
    private JPanel displayPanel;
    private JPanel playPanel;
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 400;
    private Plane plane = new Plane(6, 5, 6, 8);
    private ShotPlaneDisplayConponent displayComponent;
    private JTextField ipField;
    private JTextField portField;

    public ShotPlaneFrame() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        displayPanel = new JPanel();
        playPanel = new JPanel();
        displayComponent = new ShotPlaneDisplayConponent(plane);

        displayPanel.add(displayComponent);

        controlPanel = new JPanel();
        controlPanelInit(controlPanel);
        add(displayPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
        setResizable(false);
    }

    private void controlPanelInit(JPanel controlPanel) {
        controlPanel.setLayout(new GridLayout(8, 1));
        JPanel movePanel = new JPanel();
        JPanel rotatePanel = new JPanel();
        JButton leftButton = new JButton("Left");
        JButton upButton = new JButton("Up");
        JButton downButton = new JButton("Down");
        JButton rightButton = new JButton("Right");
        JButton okButton = new JButton("OK");
        ActionListener moveListener = new ButtonAction();
        leftButton.addActionListener(moveListener);
        upButton.addActionListener(moveListener);
        downButton.addActionListener(moveListener);
        rightButton.addActionListener(moveListener);
        okButton.addActionListener(moveListener);

        movePanel.add(leftButton);
        movePanel.add(okButton);
        movePanel.add(rightButton);

        JPanel upPanel = new JPanel();
        upPanel.add(upButton);
        controlPanel.add(upPanel);
        controlPanel.add(movePanel);
        JPanel downPanel = new JPanel();
        downPanel.add(downButton);
        controlPanel.add(downPanel);
//        rotatePanel.setLayout(new GridLayout(1, 2));

        JButton rotate1 = new JButton("<<");
        JButton rotate2 = new JButton(">>");
        rotate1.addActionListener(moveListener);
        rotate2.addActionListener(moveListener);
        rotatePanel.add(rotate1);
        rotatePanel.add(rotate2);

        controlPanel.add(rotatePanel);
        ipField = new JTextField(10);
//        ipPoint.setEnabled(false);
        portField = new JTextField(3);
        JPanel ipPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        ipPanel.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 10;
        constraints.weighty = 10;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        JLabel ipLabel = new JLabel("IP:", SwingConstants.RIGHT);
        ipPanel.add(ipLabel, constraints);
        JLabel portLabel = new JLabel("Port:", SwingConstants.RIGHT);
        ipPanel.add(portLabel, constraints);
        ipPanel.add(ipLabel);
        ipPanel.add(ipField);
        ipPanel.add(portLabel);
        ipPanel.add(portField);

        //connect button
        JPanel connectPanel = new JPanel();
        JButton connectButton = new JButton("connect");
        connectButton.addActionListener(new ConnectAction(ipField, portField));

        connectPanel.add(connectButton);
        controlPanel.add(ipPanel);
        controlPanel.add(connectPanel);
    }

    private class ButtonAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String input = event.getActionCommand();
            System.out.println(input);
            if ("Left".equals(input)) {
                Point head = plane.getHead();
                Point tail = plane.getTail();
                boolean e1 = head.getX() == tail.getX() && head.getX() > 2;
                boolean e2 = head.getY() == tail.getY() && head.getX() > 0 && tail.getX() > 0;
                if (e1 | e2) {
                    plane = new Plane(head.getX() - 1, head.getY(), tail.getX() - 1, tail.getY());
                    displayComponent.setPlane(plane);
                    displayComponent.repaint();
                }
            }
            if ("Right".equals(input)) {
                Point head = plane.getHead();
                Point tail = plane.getTail();
                boolean e1 = head.getX() == tail.getX() && head.getX() < 11;
                boolean e2 = head.getY() == tail.getY() && head.getX() < 13 && tail.getX() < 13;
                if (e1 | e2) {
                    plane = new Plane(head.getX() + 1, head.getY(), tail.getX() + 1, tail.getY());
                    displayComponent.setPlane(plane);
                    displayComponent.repaint();
                }
            }
            if ("Up".equals(input)) {
                Point head = plane.getHead();
                Point tail = plane.getTail();
                boolean e1 = head.getX() == tail.getX() && head.getY() > 0 && tail.getY() > 0;
                boolean e2 = head.getY() == tail.getY() && head.getY() > 2;
                if (e1 | e2) {
                    plane = new Plane(head.getX(), head.getY() - 1, tail.getX(), tail.getY() - 1);
                    displayComponent.setPlane(plane);
                    displayComponent.repaint();
                }
            }
            if ("Down".equals(input)) {
                Point head = plane.getHead();
                Point tail = plane.getTail();
                boolean e1 = head.getX() == tail.getX() && head.getY() < 13 && tail.getY() < 13;
                boolean e2 = head.getY() == tail.getY() && head.getY() < 11;
                if (e1 | e2) {
                    plane = new Plane(head.getX(), head.getY() + 1, tail.getX(), tail.getY() + 1);
                    displayComponent.setPlane(plane);
                    displayComponent.repaint();
                }
            }
            if ("<<".equals(input)) {
                Point head = plane.getHead();
                Point tail = plane.getTail();
                if (head.getX() == tail.getX() && head.getY() < tail.getY()) {
                    double x1 = head.getX() - 1;
                    double y = head.getY() + 1;
                    double x2 = tail.getX() + 1;
                    if (y == 1) y++;
                    plane = new Plane(x1, y, x2, y);
                } else if (head.getX() == tail.getX() && head.getY() > tail.getY()) {
                    double x1 = head.getX() + 1;
                    double y = head.getY() - 1;
                    double x2 = tail.getX() - 2;
                    if (y == 12) y--;
                    plane = new Plane(x1, y, x2, y);
                } else if (head.getY() == tail.getY() && head.getX() > tail.getX()) {
                    double x = head.getX() - 1;
                    double y1 = head.getY() - 1;
                    double y2 = tail.getY() + 2;
                    if (x == 12) x--;
                    plane = new Plane(x, y1, x, y2);
                } else if (head.getY() == tail.getY() && head.getX() < tail.getX()) {
                    System.out.println("hh");
                    double x = head.getX() + 1;
                    double y1 = head.getY() + 1;
                    double y2 = tail.getY() - 2;
                    if (x == 1) x++;
                    plane = new Plane(x, y1, x, y2);
                }
                displayComponent.setPlane(plane);
                displayComponent.repaint();
            }
            if (">>".equals(input)) {
                Point head = plane.getHead();
                Point tail = plane.getTail();
                if (head.getX() == tail.getX() && head.getY() > tail.getY()) {
                    double x1 = head.getX() - 1;
                    double y = head.getY() - 1;
                    double x2 = tail.getX() + 2;
                    if (y == 12) y--;
                    plane = new Plane(x1, y, x2, y);
                } else if (head.getX() == tail.getX() && head.getY() < tail.getY()) {
                    double x1 = head.getX() + 1;
                    double y = head.getY() + 1;
                    double x2 = tail.getX() - 2;
                    if (y == 1) y++;
                    plane = new Plane(x1, y, x2, y);
                } else if (head.getY() == tail.getY() && head.getX() < tail.getX()) {
                    double x = head.getX() + 1;
                    double y1 = head.getY() - 1;
                    double y2 = tail.getY() + 2;
                    if (x == 1) x++;
                    plane = new Plane(x, y1, x, y2);
                } else if (head.getY() == tail.getY() && head.getX() > tail.getX()) {
                    double x = head.getX() - 1;
                    double y1 = head.getY() + 1;
                    double y2 = tail.getY() - 2;
                    if (x == 12) x--;
                    plane = new Plane(x, y1, x, y2);
                }
                displayComponent.setPlane(plane);
                displayComponent.repaint();
            }
            if ("OK".equals(input)) {
                for (int i = 0; i < 14; i++) {
                    for (int j = 0; j < 14; j++) {
                        Rectangle2D rectangle2D = new Rectangle(10 + i * 20, 10 + j * 20, 20, 20);
                        displayComponent.addSquare(rectangle2D, new Color(54, 63, 61));
                    }
                }
                displayComponent.disablePlane();
                displayComponent.repaint();
            }
        }
    }
}
