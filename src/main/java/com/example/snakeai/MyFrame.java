package com.example.snakeai;

import java.awt.CardLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MyFrame extends JFrame {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    private JPanel contentPane;
    private GamePanel gamePanel;
    private AStarAIPanel aStarAIPanel;
    private GameOverPanel GOPanel;
    private GOAStarPanel GOAS;
    private MenuPanel MPanel = new MenuPanel(SCREEN_WIDTH, SCREEN_HEIGHT, this);
    CardLayout cardLayout = new CardLayout();

    public MyFrame() {
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

    public void gameOver(GameOverPanel GOPanel) {
        this.GOPanel = GOPanel;
        contentPane.add(GOPanel, "GameOver Panel");
        cardLayout.next(contentPane);
    }

    public void playAgain() {
        cardLayout.next(contentPane);
        gamePanel.restart();
        contentPane.remove(GOPanel);
        gamePanel.requestFocusInWindow();
    }

    public void aStarAI() {
        aStarAIPanel = new AStarAIPanel(this, SCREEN_WIDTH, SCREEN_HEIGHT);
        contentPane.add(aStarAIPanel, "A* AI Panel");
        cardLayout.next(contentPane);
        contentPane.remove(MPanel);
        aStarAIPanel.requestFocusInWindow();
    }

    public void gameOverAStar(GOAStarPanel GOAS) {
        this.GOAS = GOAS;
        contentPane.add(GOAS, "GameOver A* Panel");
        cardLayout.next(contentPane);
    }

    public void playAgainAStar() {
        cardLayout.next(contentPane);
        aStarAIPanel.restart();
        contentPane.remove(GOAS);
        aStarAIPanel.requestFocusInWindow();
    }
}
