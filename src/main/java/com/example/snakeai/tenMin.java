package com.example.snakeai;

import java.util.Timer;
import java.util.TimerTask;

public class tenMin {
    Timer timer;
    GamePanel astaraipanel;
    public tenMin(GamePanel astaraipanel){
        this.astaraipanel = astaraipanel;
    }

    public void gameStop(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                astaraipanel.running = false;
                astaraipanel.how = "Time's up";
                if(astaraipanel.applesEaten1 > astaraipanel.applesEaten2){
                    astaraipanel.winner = "Snake 1";
                }
                if(astaraipanel.applesEaten2 > astaraipanel.applesEaten1){
                    astaraipanel.winner = "Snake 2";
                }
                if(astaraipanel.applesEaten1 == astaraipanel.applesEaten2){
                    astaraipanel.winner = "No Snake";
                }
            }
        }, 3*60*1000);
    }
}
