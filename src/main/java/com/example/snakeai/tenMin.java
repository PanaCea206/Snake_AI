package com.example.snakeai;

import java.util.Timer;
import java.util.TimerTask;

public class tenMin {
    //Timer timer;
    GamePanel gamepanel;
    public tenMin(GamePanel astaraipanel){
        this.gamepanel = astaraipanel;
    }

    public void gameStop(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                gamepanel.running = false;
                gamepanel.how = "Time's up";
                if(gamepanel.applesEaten1 > gamepanel.applesEaten2){
                    gamepanel.winner = "Snake 1";
                }
                if(gamepanel.applesEaten2 > gamepanel.applesEaten1){
                    gamepanel.winner = "Snake 2";
                }
                if(gamepanel.applesEaten1 == gamepanel.applesEaten2){
                    gamepanel.winner = "No Snake";
                }
            }
        }, 3*60*1000);
    }
}
