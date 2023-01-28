package com.example.snakeai;

import java.util.Timer;
import java.util.TimerTask;

public class appleTimer {
    Timer timer;
    boolean apple = true;
    GamePanel astaraipanel;
    public appleTimer(GamePanel astaraipanel){
        this.astaraipanel = astaraipanel;
    }

    public void appleThree() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                apple = false;
                System.out.println("apple respawn");
            }
        },  1000);
    }
}
