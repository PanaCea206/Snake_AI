package com.example.snakeai;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame {

    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    private JPanel contentPane;
    private GamePanel gamePanel;
    //private GameOverPanel GameOP;
    private MenuPanel MPanel = new MenuPanel(WIDTH, HEIGHT, this);
    CardLayout cardLayout = new CardLayout();

    public Frame() {
        setTitle("Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = new JPanel();
        contentPane.setLayout(cardLayout);
        contentPane.add(MPanel, "Menu Panel");
        setResizable(false);
        setContentPane(contentPane);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public void GameP() {
        gamePanel = new GamePanel(this, WIDTH, HEIGHT);
        contentPane.add(gamePanel, "Game Panel");
        cardLayout.next(contentPane);
        contentPane.remove(MPanel);
        gamePanel.requestFocusInWindow();
    }

    public void gameOver(GameOverPanel GameOP) {
        //this.GameOP = GameOP;
        contentPane.add(GameOP, "GameOver");
        cardLayout.next(contentPane);
    }
}
