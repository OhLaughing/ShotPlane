package com.bigwanggang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

public class ShotPlaneFrame extends JFrame {
    private JPanel controlPanel;
    private JPanel displayPanel;
    private JPanel playPanel;
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 400;
    private Plane plane = new Plane(3, 3, 3, 6);
    private JComponent displayComponent;
    private JComponent play;


    public ShotPlaneFrame() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        displayPanel = new JPanel();
        playPanel = new JPanel();
        displayComponent = new PutPlaneConponent();

//        ShotPlaneDisplayConponent conponent = new ShotPlaneDisplayConponent(plane);
        displayPanel.add(displayComponent);

        controlPanel = new JPanel();
        controlPanelInit(controlPanel);
        add(displayPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
//        setResizable(false);
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
        ActionListener moveListener = new MoveAction();
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
    }

    private class PutPlaneConponent extends JComponent {
        private static final int DEFAULT_WIDTH = 300;
        private static final int DEFAULT_HEIGHT = 291;
        private List<Line2D> line2DList = null;
        Graphics2D g2;

        public PutPlaneConponent() {
            line2DList = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                line2DList.add(new Line2D.Double(10, 10 + i * 20, 290, 10 + i * 20));
                line2DList.add(new Line2D.Double(10 + i * 20, 10, 10 + i * 20, 290));
            }
        }

        public void paintComponent(Graphics g) {
            g2 = (Graphics2D) g;

            for (Line2D line : line2DList)
                g2.draw(line);
            drawPlane(plane);
        }

        private void drawPlane(Plane plane) {
            List<Rectangle2D> list = new ArrayList<>();
            Point head = plane.getHead();
            Point tail = plane.getTail();
            if (head.getX() == tail.getX()) {
                list.add(new Rectangle2D.Double(20 * head.getX() + 10, 20 * head.getY() + 10, 20, 20));
                for (int j = (int) (head.getX() - 2); j <= head.getX() + 2; j++) {
                    list.add(new Rectangle2D.Double(20 * j + 10, 20 * head.getY() + 30, 20, 20));
                }
            }
            if (head.getY() == tail.getY()) {
                list.add(new Rectangle2D.Double(20 * head.getX(), 20 * head.getY(), 20, 20));
            }
            for (Rectangle2D r : list) {
                g2.setPaint(Color.BLUE);
                g2.fill(r);
                g2.setPaint(Color.WHITE);
            }
        }

        public Dimension getPreferredSize() {
            return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
    }

    private class MoveAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String input = event.getActionCommand();
            System.out.println(input);
            if ("Left".equals(input)) {
                Point head = plane.getHead();
                Point tail = plane.getTail();
                plane = new Plane(head.getX() - 1, head.getY(), tail.getX() - 1, tail.getY());
                displayComponent.repaint();
            }
            if ("OK".equals(input)) {
                play = new ShotPlaneDisplayConponent(plane);
                playPanel.add(play);
                add(playPanel, BorderLayout.CENTER);
                System.out.println("come");
            }
        }
    }
}
