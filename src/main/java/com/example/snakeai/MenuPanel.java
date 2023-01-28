package com.example.snakeai;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {

    int width;
    int height;
    JFrame frame;

    public MenuPanel(int width, int height, JFrame frame) {
        this.width = width;
        this.height = height;
        this.frame = frame;

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setLayout(null);


        JButton Start = new JButton("START");
        Start.setLayout(null);
        Start.setBounds((width / 2) - 100, (height / 4) + 325 - 30, 200, 60);
        //Start.setBackground(Color.GREEN);
        Start.addActionListener(new GamePanelListener(frame));
        add(Start);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("SNAKE AI", (width - metrics.stringWidth("SNAKE AI"))/2, height / 4);
    }
}
