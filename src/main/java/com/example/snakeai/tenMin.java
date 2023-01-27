package com.example.snakeai;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class tenMin {
    Timer timer;
    AStarAIPanel astaraipanel;
    public tenMin(AStarAIPanel astaraipanel){
        this.astaraipanel = astaraipanel;
    }

    public void gameStop(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                astaraipanel.running = false;
            }
        }, 3*60*1000);
        astaraipanel.how = "Time's up";
        astaraipanel.winner = "No Snake";
    }

    public void appleStop(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                astaraipanel.newApple();
            }
        }, 2*1000);

    }


}
