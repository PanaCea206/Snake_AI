package com.example.snakeai;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameOverPanel extends JPanel {

    int applesEaten1;
    int applesEaten2;
    int width;
    int height;
    JFrame frame;
    String winner;

    public GameOverPanel(int applesEaten1, int applesEaten2, int width, int height, JFrame frame, String winner) {

        this.applesEaten1 = applesEaten1;
        this.applesEaten2 = applesEaten2;
        this.width = width;
        this.height = height;
        this.frame = frame;
        this.winner = winner;

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setLayout(null);

        JButton again = new JButton("Play Again");
        again.setLayout(null);
        again.setBounds((width / 2) - 100, (height - (height / 4)) - 30, 200, 60);
        again.addActionListener(new AStarListener(frame));
        add(again);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.setColor(Color.green);
        g.setFont(new Font("ARIAL", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("SNAKE1: " + applesEaten1 + "    SNAKE2: " + applesEaten2, (width - metrics1.stringWidth("SNAKE1: " + applesEaten1 + "    P2 Score: " + applesEaten2))/2, g.getFont().getSize());


        g.setColor(Color.GREEN);
        g.setFont(new Font("ARIAL", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString(winner + " Won", (width - metrics2.stringWidth(winner + " Won"))/2, height / 2);
    }

}
