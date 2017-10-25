package com.bigwanggang;

import javax.swing.*;
import java.awt.*;

/**
 * Created by gustaov on 2017/10/25.
 */
public class ShotPlaneFrame extends JFrame {
    private JPanel controlPanel;
    private JPanel displayPanel;
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 400;

    public ShotPlaneFrame() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        displayPanel = new JPanel();
        displayPanel.add(new ShotPlaneDisplayConponent());
        controlPanel = new JPanel();

        Button b = new Button("Test");
        controlPanel.add(b);
        add(displayPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
    }
}
