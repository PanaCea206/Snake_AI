package com.example.snakeai;

public class winCount {
    private int win1;
    private int win2;
    String winner;
    public void winCount(String winner){
        this.winner = winner;

        if (winner == "Snake1"){
            win1++;
        }
        if (winner == "Snake2"){
            win2++;
        }
    }

    public int getWin1(){
        return this.win1;
    }
    public int getWin2(){
        return this.win2;
    }
}