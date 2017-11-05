package com.bigwanggang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ShotPlaneClientFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 350;
    private Plane plane = new Plane(6, 5, 6, 8);
    private ShotPlaneDisplayConponent gameDisplayComponent;
    private JPanel controlPanel;
    private JTextField ipField;
    private JTextField portField;
    private JTextArea chatDisplayArea;
    private JTextArea chatInputArea;
    private GridBagConstraints constraints;
    private PrintWriter pw;
    private boolean clientIsOn = false;
    private JButton connectButton;

    public ShotPlaneClientFrame() {
        controlPanel = new JPanel();
        chatDisplayArea = new JTextArea(6, 6);
        chatInputArea = new JTextArea(6, 3);
        chatInputArea.setLineWrap(true);
        chatDisplayArea.setLineWrap(true);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        gameDisplayComponent = new ShotPlaneDisplayConponent(plane);
        constraints = new GridBagConstraints();
        ipField = new JTextField("127.0.0.1", 10);
        portField = new JTextField(10);

        //separator of game display and infomation display
        JSplitPane pane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        pane1.setDividerSize(1);
        pane1.setDividerLocation(300);
        pane1.setEnabled(false);
        getContentPane().add(pane1, BorderLayout.CENTER);

        //separator of information diaplay and control panel
        JSplitPane dis_control_split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        dis_control_split.setDividerLocation(200);
        dis_control_split.setEnabled(false);
        //left of separator is game diaplay panel
        pane1.setLeftComponent(gameDisplayComponent);
        pane1.setRightComponent(dis_control_split);

        //separator of information diaplay and information input palen
        JSplitPane dis_input_split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        dis_input_split.setDividerLocation(220);
        dis_control_split.setLeftComponent(dis_input_split);
        dis_control_split.setRightComponent(controlPanel);
        dis_input_split.setLeftComponent(chatDisplayArea);

        //separator of information input and sent information
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(150);
        dis_input_split.setRightComponent(split);
        split.setLeftComponent(chatInputArea);
        initSendButton(split);

        controlPanelInit();
        setTitle("Client");
//        setResizable(false);
    }

    private void initSendButton(JSplitPane split) {
        JButton sendButton = new JButton("SEND");
        split.setRightComponent(sendButton);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String info = chatInputArea.getText();
                chatDisplayArea.append("client:" + info + "\r\n");
                pw.println(info);
                chatInputArea.setText("");
            }
        });
    }

    private void controlPanelInit() {
        controlPanel.setLayout(new GridLayout(2, 1));

        initIpPortInputPanel();

        initDirectPanel();
        setResizable(false);
    }

    private void initIpPortInputPanel() {
        GridBagLayout layout = new GridBagLayout();

        JPanel ipPortPanel = new JPanel();
        ipPortPanel.setLayout(layout);

        JLabel ipLabel = new JLabel("IP:");
        JLabel portLabel = new JLabel("Port:");
        connectButton = new JButton("connect");
        connectButton.addActionListener(new ConnectAction());

        constraints.anchor = GridBagConstraints.SOUTH;
        add(ipPortPanel, ipLabel, constraints, 0, 0, 1, 1);
        add(ipPortPanel, ipField, constraints, 1, 0, 5, 1);
        add(ipPortPanel, portLabel, constraints, 0, 1, 1, 1);
        add(ipPortPanel, portField, constraints, 1, 1, 5, 1);
        add(ipPortPanel, connectButton, constraints, 2, 2, 1, 1);

        controlPanel.add(ipPortPanel);
    }

    private void initDirectPanel() {
        JPanel directionPanel = new JPanel();
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

        JButton rotate1 = new JButton("<<");
        JButton rotate2 = new JButton(">>");
        rotate1.addActionListener(moveListener);
        rotate2.addActionListener(moveListener);

        GridBagLayout layout = new GridBagLayout();

        directionPanel.setLayout(layout);
        constraints.anchor = GridBagConstraints.NORTH;
        add(directionPanel, leftButton, constraints, 0, 1, 1, 1);
        add(directionPanel, upButton, constraints, 1, 0, 1, 1);
        add(directionPanel, downButton, constraints, 1, 2, 1, 1);
        add(directionPanel, rightButton, constraints, 2, 1, 1, 1);
        add(directionPanel, okButton, constraints, 1, 1, 1, 1);

        add(directionPanel, rotate1, constraints, 4, 0, 1, 1);
        add(directionPanel, rotate2, constraints, 4, 2, 1, 1);
        controlPanel.add(directionPanel);
    }

    public void add(JPanel panel, Component c, GridBagConstraints constraints, int x, int y, int w, int h) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = w;
        constraints.gridheight = h;
        panel.add(c, constraints);
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
                    gameDisplayComponent.setPlane(plane);
                    gameDisplayComponent.repaint();
                }
            }
            if ("Right".equals(input)) {
                Point head = plane.getHead();
                Point tail = plane.getTail();
                boolean e1 = head.getX() == tail.getX() && head.getX() < 11;
                boolean e2 = head.getY() == tail.getY() && head.getX() < 13 && tail.getX() < 13;
                if (e1 | e2) {
                    plane = new Plane(head.getX() + 1, head.getY(), tail.getX() + 1, tail.getY());
                    gameDisplayComponent.setPlane(plane);
                    gameDisplayComponent.repaint();
                }
            }
            if ("Up".equals(input)) {
                Point head = plane.getHead();
                Point tail = plane.getTail();
                boolean e1 = head.getX() == tail.getX() && head.getY() > 0 && tail.getY() > 0;
                boolean e2 = head.getY() == tail.getY() && head.getY() > 2;
                if (e1 | e2) {
                    plane = new Plane(head.getX(), head.getY() - 1, tail.getX(), tail.getY() - 1);
                    gameDisplayComponent.setPlane(plane);
                    gameDisplayComponent.repaint();
                }
            }
            if ("Down".equals(input)) {
                Point head = plane.getHead();
                Point tail = plane.getTail();
                boolean e1 = head.getX() == tail.getX() && head.getY() < 13 && tail.getY() < 13;
                boolean e2 = head.getY() == tail.getY() && head.getY() < 11;
                if (e1 | e2) {
                    plane = new Plane(head.getX(), head.getY() + 1, tail.getX(), tail.getY() + 1);
                    gameDisplayComponent.setPlane(plane);
                    gameDisplayComponent.repaint();
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
                gameDisplayComponent.setPlane(plane);
                gameDisplayComponent.repaint();
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
                gameDisplayComponent.setPlane(plane);
                gameDisplayComponent.repaint();
            }
            if ("OK".equals(input)) {
                for (int i = 0; i < 14; i++) {
                    for (int j = 0; j < 14; j++) {
                        Rectangle2D rectangle2D = new Rectangle(10 + i * 20, 10 + j * 20, 20, 20);
                        gameDisplayComponent.addSquare(rectangle2D, new Color(54, 63, 61));
                    }
                }
                gameDisplayComponent.disablePlane();
                gameDisplayComponent.repaint();
                System.out.println(GameControl.SERVERREADY);
                if (GameControl.SERVERREADY == 1) {
                    pw.println("game begin");
                    chatDisplayArea.append("game begin");
                } else {
                    GameControl.CLIENTREADY = 1;
                    System.out.println("Client ready");
                    pw.println("Client ready");
                }
            }
        }
    }

    private class ConnectAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            System.out.println(event.getActionCommand());
            System.out.println("ip:" + ipField.getText());
            System.out.println("port:" + portField.getText());
            String ip = ipField.getText().trim();

            int port = Integer.parseInt(portField.getText().trim());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket s = new Socket(ip, port);
                        InputStreamReader isr = new InputStreamReader(s.getInputStream());
                        BufferedReader br = new BufferedReader(isr);
                        pw = new PrintWriter(s.getOutputStream(), true);
                        chatDisplayArea.append("client has connected to server\n");
                        chatDisplayArea.append("put the plane and press OK Button to begin game\n");
                        while (true) {
                            String info = br.readLine();
                            chatDisplayArea.append("server:  " + info + "\r\n");
                        }
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            connectButton.setEnabled(false);
            System.out.println("connect button disable");
        }
    }
}
