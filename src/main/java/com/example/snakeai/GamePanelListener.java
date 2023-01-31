package com.example.snakeai;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class GamePanelListener implements ActionListener {

    JFrame frame;

    public GamePanelListener(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Thread thread = new Thread(() -> ((Frame) frame).GameP());

        thread.start();
    }

}
