package com.bigwanggang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShotPlaneDisplayConponent extends JComponent {
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 290;
    private List<Line2D> line2DList = null;
    private Map<Rectangle2D, Color> squares;
    Graphics2D g2;
    private Plane plane;

    public ShotPlaneDisplayConponent(Plane plane) {
        this.plane = plane;
        line2DList = new ArrayList<>();
        squares = new HashMap<>();
        for (int i = 0; i < 15; i++) {
            line2DList.add(new Line2D.Double(10, 10 + i * 20, 290, 10 + i * 20));
            line2DList.add(new Line2D.Double(10 + i * 20, 10, 10 + i * 20, 290));
        }
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++) {
                Rectangle2D rectangle2D = new Rectangle(10 + i * 20, 10 + j * 20, 20, 20);
                squares.put(rectangle2D, new Color(54, 63, 61));
            }
        }
        addMouseListener(new ShotPlaneHandler());
    }

    public void paintComponent(Graphics g) {
        g2 = (Graphics2D) g;

        for (Line2D line : line2DList)
            g2.draw(line);

        for (Rectangle2D rectangle2D : squares.keySet()) {
            g2.setPaint(squares.get(rectangle2D));
            g2.fill(rectangle2D);
            g2.setPaint(Color.BLACK);
            g2.draw(rectangle2D);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    private class ShotPlaneHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent event) {
            Point point = event.getPoint();
            if (event.getClickCount() >= 2) {
                int x = (int) point.getX();
                int y = (int) point.getY();
                if (x > 10 && x < 290 && y > 10 && y < 290) {
                    int xN = (x - 10) / 20;
                    int yN = (y - 10) / 20;
                    System.out.println(xN + "," + yN);
                    Line2D line2D = new Line2D.Double(xN * 20 + 10, yN * 20 + 10, xN * 20 + 30, yN * 20 + 30);
                    Rectangle2D rectangle2D = new Rectangle2D.Double(xN * 20 + 10, yN * 20 + 10, 20, 20);
                    Point p = new Point(xN, yN);
                    if (Util.ifHitDownPlane(plane, p)) {
                        squares.put(rectangle2D, Color.BLUE);
                        System.out.println("hit down the plane");
                    }
                    else if (Util.ifHitPlane(plane, p)) {
                        squares.put(rectangle2D, Color.BLUE);
                        System.out.println("hit the plane, but does not hit down");
                    } else {
                        squares.put(rectangle2D, Color.WHITE);
                        System.out.println("does not hit plane");
                    }
                    line2DList.add(line2D);
                    repaint();
                }
            }
        }
    }
}
