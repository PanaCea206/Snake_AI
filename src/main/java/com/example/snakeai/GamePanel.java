package com.example.snakeai;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener  {

    final int WIDTH;
    final int HEIGHT;
    static final int UNIT_SIZE = 25;
    final int GAME_UNITS;
    static final int DELAY = 100;
    final int x1[];
    final int y1[];
    final int x2[];
    final int y2[];
    int body1 = 3;
    int body2 = 3;
    int applesEaten1;
    int applesEaten2;
    int appleX;
    int appleY;
    char direction1 = 'R';
    char direction2 = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    Random random1;
    Random random2;
    JFrame frame;
    int xDistance1;
    int yDistance1;
    int xDistance2;
    int yDistance2;
    int hCost1;
    int hCost2;
    int numDirections1 = 0;
    int numDirections2 = 0;
    char directions1[];
    char directions2[];
    int count1 = 0;
    int gCost1;
    int count2 = 0;
    int gCost2;
    String winner = "No Snake";
    String how = "Path not found";
    tenMin tenmin;
    appleTimer appletimer;


    public GamePanel(JFrame frame, int w, int h) {
        WIDTH = w;
        HEIGHT = h;
        GAME_UNITS = (WIDTH*HEIGHT)/UNIT_SIZE;
        this.x1 = new int[GAME_UNITS];
        this.y1 = new int[GAME_UNITS];
        this.x2 = new int[GAME_UNITS];
        this.y2 = new int[GAME_UNITS];
        this.frame = frame;
        random = new Random();
        random1 = new Random();
        random2 = new Random();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        startGame();
    }


    public void startGame() {
        //new snake
        x1[0] = random1.nextInt((int)(WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        y1[0] = random1.nextInt((int)(HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
        x2[0] = random2.nextInt((int)(WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        y2[0] = random2.nextInt((int)(HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
        SpawnApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();

        tenmin = new tenMin(this);
        tenmin.gameStop();
        if (!running) {
            timer.stop();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.RED);
            g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);   //coordinate of x/y,  width/heigh of the oval
            timer.setDelay(DELAY);
            for (int i = 0; i < body1; i++) {
                if (i == 0) {    //set head
                    g.setColor(Color.BLUE);
                    g.fillRect(x1[i], y1[i], UNIT_SIZE, UNIT_SIZE); //coordinate of x/y,  width/heigh of the rectangle
                } else {    //set body
                    g.setColor(new Color(0, 99, 180));
                    g.fillRect(x1[i], y1[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            for (int i = 0; i < body2; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x2[i], y2[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x2[i], y2[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            //g.drawString("S1: " + applesEaten1, (WIDTH - metrics.stringWidth("S1: " + applesEaten1))/4, g.getFont().getSize());
            g.drawString("S2: " + applesEaten2, (WIDTH - metrics.stringWidth("S2: " + applesEaten2))*3/4, g.getFont().getSize());

            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("S1: " + applesEaten1, (WIDTH - metrics1.stringWidth("S1: " + applesEaten1))/4, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void SpawnApple() {

        appleX = random.nextInt((int)(WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(HEIGHT/UNIT_SIZE)) * UNIT_SIZE;

        fixApple();

        System.out.println("apple: "+appleX + " , " + appleY); // 575 it stops working

        List<Node> path1 = Bot1();  //the value from parents list
        List<Node> path2 = Bot2();
        if (path1 == null) {
            numDirections1 = -1;
            return;
        }
        numDirections1 = path1.size();
        directions1 = new char[numDirections1];
        for (int i = 0; i < numDirections1; i++) {
            directions1[i] = path1.get(i).getDirection();
        }

        if (path2 == null) {
            numDirections2 = -1;
            return;
        }
        numDirections2 = path2.size();
        directions2 = new char[numDirections2];
        for (int i = 0; i < numDirections2; i++) {
            directions2[i] = path2.get(i).getDirection();
        }

    }

    private void fixApple() {
        /*appletimer = new appleTimer(this);
        appletimer.appleThree();
        if(appletimer.apple == false){
            newApple();
        }*/
        for(int i = body1; i>0; i--) {
            if(appleX == x1[i] && appleY == y1[i]) {
                SpawnApple();
            }
        }
        for(int i = body2; i>0; i--) {
            if(appleX == x2[i] && appleY == y2[i]) {
                SpawnApple();
            }
        }
    }

    public void checkApple() {
        if((x1[0] == appleX) && (y1[0] == appleY)) {
            body1++;
            applesEaten1++;
            SpawnApple();
        }
        if((x2[0] == appleX) && (y2[0] == appleY)) {
            body2++;
            applesEaten2++;
            SpawnApple();
        }
    }

    public void Run() {
        if (numDirections1 != -1) { // if numDirections1 == -1, direction1 is defined in pathfinder1
            direction1 = directions1[numDirections1 - 1];   //list add from front
            numDirections1--;
        }
        if (numDirections2 != -1) {
            direction2 = directions2[numDirections2 - 1];
            numDirections2--;
        }

        for(int i = body1; i > 0; i--) {
            x1[i] = x1[i-1];
            y1[i] = y1[i-1];
        }

        for(int i = body2; i > 0; i--) {
            x2[i] = x2[i-1];
            y2[i] = y2[i-1];
        }

        switch(direction1) {
            case 'U':
                y1[0] = y1[0] - UNIT_SIZE;
                break;
            case 'D':
                y1[0] = y1[0] + UNIT_SIZE;
                break;
            case 'L':
                x1[0] = x1[0] - UNIT_SIZE;
                break;
            case 'R':
                x1[0] = x1[0] + UNIT_SIZE;
                break;
        }
        switch(direction2) {
            case 'U':
                y2[0] = y2[0] - UNIT_SIZE;
                break;
            case 'D':
                y2[0] = y2[0] + UNIT_SIZE;
                break;
            case 'L':
                x2[0] = x2[0] - UNIT_SIZE;
                break;
            case 'R':
                x2[0] = x2[0] + UNIT_SIZE;
                break;
        }
    }

    public void Collisions() {
        //p2 won
        for (int i = body1-1; i > 0; i--) {
            if ((x1[0] == x1[i]) && (y1[0] == y1[i])) {
                running = false;
                winner = "Snake2";
                how = "Snake 1 touch itself";
                System.out.println(how);
            }
        }
        if (x1[0] < 0) {
            running = false;
            winner = "Snake2";
            how = "Snake 1 out from the board";
        }
        if (x1[0] > WIDTH) {
            running = false;
            winner = "Snake2";
            how="Snake 1 out from the board";
            System.out.println(how);
        }
        if (y1[0] < 0) {
            running = false;
            winner = "Snake2";
            how = "Snake 1 out from the board";
            System.out.println(how);
        }
        if (y1[0] > HEIGHT) {
            running = false;
            winner = "Snake2";
            how = "Snake 1 out from the board";
            System.out.println(how);
        }

        //p1 won
        for (int i = body2-1; i > 0; i--) {
            if ((x2[0] == x2[i]) && (y2[0] == y2[i])) {
                running = false;
                winner = "Snake1";
                how = "Snake 2 touch itself";
                System.out.println(how);
            }
        }
        if (x2[0] < 0) {
            running = false;
            winner = "Snake1";
            how = "Snake 2 out from the board";
            System.out.println(how);
        }
        if (x2[0] > WIDTH) {
            running = false;
            winner = "Snake1";
            how = "Snake 2 out from the board";
            System.out.println(how);
        }
        if (y2[0] < 0) {
            running = false;
            winner = "Snake1";
            how = "Snake 2 out from the board";
            System.out.println(how);
        }
        if (y2[0] > HEIGHT) {
            running = false;
            winner = "Snake1";
            how = "Snake 2 out from the board";
            System.out.println(how);
        }
        //p2 won
        for (int i = body2-1; i > 0; i--) {
            if ((x1[0] == x2[i]) && (y1[0] == y2[i])) {
                running = false;
                winner = "Snake2";
                how = "Snake 1 touch snake 2";
                System.out.println(how);
            }
        }
        //p1 won
        for (int i = body1-1; i > 0; i--) {
            if ((x2[0] == x1[i]) && (y2[0] == y1[i])) {
                running = false;
                winner = "Snake1";
                how = "Snake 2 touch snake 1";
                System.out.println(how);
            }
        }
        if(x1[0]==x2[0] && y1[0]==y2[0]){
            running = false;
            if(body1>body2){
                winner = "Snake1";
                how = "Snake 1 is longer";
                System.out.println(how);
            }
            if(body2>body1){
                winner = "Snake2";
                how = "Snake 2 is longer";
                System.out.println(how);
            }
        }


    }

    public void gameOver(Graphics g) {
            ((Frame) frame).gameOver(new GameOverPanel(applesEaten1, applesEaten2, how, WIDTH, HEIGHT,frame, winner));
        }

    public void actionPerformed(ActionEvent event) {
        if(running) {
            if (numDirections1 == -1) {
                pathFinder1();
            }
            if (numDirections2 == -1) {
                pathFinder2();
            }
            Run();
            checkApple();
            Collisions();
        }
        repaint();
    }

    private boolean isBlocked(char d, int x, int y) {
        if (d == 'R' ) {
            //border
            if (x >= WIDTH) {
                return true;
            }
        } else if (d == 'L') {
            if (x < 0) {
                return true;
            }
        } else if (d == 'D') {
            if (y >= WIDTH) {
                return true;
            }
        } else {
            if (y < 0) {
                return true;
            }
        }
        for (int i = body2; i > 0; i--) {
            if ((x == this.x2[i]) && (y == this.y2[i])) {
                return true;
            }
        }
        for (int i = body1; i > 0; i--){
            if ((x == this.x1[i]) && (y == this.y1[i])) {
                return true;
            }
        }

        return false;
    }

    private List<Node> Bot1() {

        List<Node> parents = new ArrayList<Node>();
        PriorityQueue<Node> open = new PriorityQueue<Node>();   //Nodes to be evaluated
        List<Node> closed = new ArrayList<Node>();              //Nodes already evaluated
        count1 = 0;
        gCost1 = 0;

        Node startNode = new Node(x1[0], y1[0], gCost1, findHCost1(x1[0], y1[0]));
        startNode.setDirection(direction1); //start with R
        Node goalNode = new Node(appleX, appleY, findHCost1(x1[0], y1[0]), 0);

        open.add(startNode);

        while (!open.isEmpty()) {

            count1++;

            Node current = open.poll(); //remove from open
            current.close();            //true
            closed.add(current);        //add to closed

            if (count1 > (WIDTH / UNIT_SIZE) * (HEIGHT / UNIT_SIZE) * 10) {
                System.out.println("Couldnt find path");
                return null;
            }

            // if current is the target Node
            if (current.same(goalNode)) {
                //backtrack and create parents list
                boolean finished = false;
                Node n = current;
                while (!finished) {
                    parents.add(n);
                    n = n.getParent();
                    if (n.getParent() == null) {
                        finished = true;
                    }
                }
                return parents; //the Bot1 value
            }

            // check neighbours //i == 0 is current direction
            for (int i = 0; i < 3; i++) {

                if (i == 0) {
                    gCost1 = 10; // if current direction
                } else {
                    gCost1 = 14; // if change direction, costs more
                }
                boolean exists = false;
                Node n; //next Node

                if (i == 0) {
                    if (current.getDirection() == 'R') { // Continue Right
                        // CHECK IF BLOCKED
                        if (!isBlocked(current.getDirection(), current.getxAxis() + UNIT_SIZE, current.getyAxis())) {
                            n = new Node(current.getxAxis() + UNIT_SIZE, current.getyAxis(), gCost1, findHCost1(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {    //if its blocked, check next neighbor
                            continue;
                        }
                    }
                    else if (current.getDirection() == 'L') { // Continue Left
                        if (!isBlocked(current.getDirection(), current.getxAxis() - UNIT_SIZE, current.getyAxis())) {
                            n = new Node(current.getxAxis() - UNIT_SIZE, current.getyAxis(), gCost1, findHCost1(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    }
                    else if (current.getDirection() == 'D') { // Continue Down
                        if (!isBlocked(current.getDirection(), current.getxAxis(), current.getyAxis() + UNIT_SIZE)) {
                            n = new Node(current.getxAxis(), current.getyAxis() + UNIT_SIZE, gCost1, findHCost1(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    }
                    else { // Continue Up
                        if(!isBlocked(current.getDirection(), current.getxAxis(), current.getyAxis() - UNIT_SIZE)) {
                            n = new Node(current.getxAxis(), current.getyAxis() - UNIT_SIZE, gCost1, findHCost1(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    }
                }
                else if (i == 1) {
                    if (current.getDirection() == 'R') { // Turn Down
                        if(!isBlocked('D', current.getxAxis(), current.getyAxis() + UNIT_SIZE)) {
                            n = new Node(current.getxAxis(), current.getyAxis() + UNIT_SIZE, gCost1, findHCost1(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    } else if (current.getDirection() == 'L') { // Turn Up
                        if(!isBlocked('U', current.getxAxis(), current.getyAxis() - UNIT_SIZE)) {
                            n = new Node(current.getxAxis(), current.getyAxis() - UNIT_SIZE, gCost1, findHCost1(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    } else if (current.getDirection() == 'D') { // Turn Left
                        if(!isBlocked('L', current.getxAxis() - UNIT_SIZE, current.getyAxis())) {
                            n = new Node(current.getxAxis() - UNIT_SIZE, current.getyAxis(), gCost1, findHCost1(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    } else { // Turn Right
                        if(!isBlocked('R', current.getxAxis() + UNIT_SIZE, current.getyAxis())) {
                            n = new Node(current.getxAxis() + UNIT_SIZE, current.getyAxis(), gCost1, findHCost1(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    }
                }
                else {
                    if (current.getDirection() == 'R') { // Turn Up
                        if(!isBlocked('U', current.getxAxis(), current.getyAxis() - UNIT_SIZE)) {
                            n = new Node(current.getxAxis(), current.getyAxis() - UNIT_SIZE, gCost1, findHCost1(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    } else if (current.getDirection() == 'L') { // Turn Down
                        if(!isBlocked('D', current.getxAxis(), current.getyAxis() + UNIT_SIZE)) {
                            n = new Node(current.getxAxis(), current.getyAxis() + UNIT_SIZE, gCost1, findHCost1(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    } else if (current.getDirection() == 'D') { // Turn Right
                        if(!isBlocked('R', current.getxAxis() + UNIT_SIZE, current.getyAxis())) {
                            n = new Node(current.getxAxis() + UNIT_SIZE, current.getyAxis(), gCost1, findHCost1(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    } else { // Turn Left
                        if(!isBlocked('L', current.getxAxis() - UNIT_SIZE, current.getyAxis())) {
                            n = new Node(current.getxAxis() - UNIT_SIZE, current.getyAxis(), gCost1, findHCost1(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    }
                }

                if (exists && n.isClosed()) {
                    continue;
                }

                // next node should not be closed and exists   //the node was in open is closed
                if (n.getFCost() <= current.getFCost() || !open.contains(n)) {
                    n.setParent(current);
                    if (!open.contains(n)) {
                        n.setgCost(n.getParent().getgCost() + gCost1);
                        open.add(n);
                    }
                }
            }
        }
        return null;    //open is empty
    }
    private List<Node> Bot2() {

        List<Node> parents = new ArrayList<>();
        PriorityQueue<Node> open = new PriorityQueue<>();
        List<Node> closed = new ArrayList<>();

        count2 = 0;
        gCost2 = 0;
        Node startNode = new Node(x2[0], y2[0], gCost2, findHCost2(x2[0], y2[0]));
        startNode.setDirection(direction2);
        Node goalNode = new Node(appleX, appleY, findHCost2(x2[0], y2[0]), 0);

        open.add(startNode);

        while (!open.isEmpty()) {

            count2++;

            Node current = open.poll();
            current.close();
            closed.add(current);

            if (count2 > (WIDTH / UNIT_SIZE) * (HEIGHT / UNIT_SIZE) * 10) {
                System.out.println("Couldnt find path");
                return null;
            }

            if (current.same(goalNode)) {
                //backtrack and create parents list
                boolean finished = false;
                Node n = current;
                while (!finished) {
                    parents.add(n);
                    n = n.getParent();
                    if (n.getParent() == null) {
                        finished = true;
                    }
                }
                return parents;
            }

            // check neighbours
            for (int i = 0; i < 3; i++) {

                if (i == 0) {
                    gCost2 = 10; // if current direction
                } else {
                    gCost2 = 14; // if change direction, costs more
                }

                boolean exists = false;
                Node n;
                if (i == 0) {
                    if (current.getDirection() == 'R') { // Continue Right
                        // CHECK IF BLOCKED
                        if (!isBlocked(current.getDirection(), current.getxAxis() + UNIT_SIZE, current.getyAxis())) {
                            n = new Node(current.getxAxis() + UNIT_SIZE, current.getyAxis(), gCost2, findHCost2(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    } else if (current.getDirection() == 'L') { // Continue Left
                        if (!isBlocked(current.getDirection(), current.getxAxis() - UNIT_SIZE, current.getyAxis())) {
                            n = new Node(current.getxAxis() - UNIT_SIZE, current.getyAxis(), gCost2, findHCost2(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    } else if (current.getDirection() == 'D') { // Continue Down
                        if (!isBlocked(current.getDirection(), current.getxAxis(), current.getyAxis() + UNIT_SIZE)) {
                            n = new Node(current.getxAxis(), current.getyAxis() + UNIT_SIZE, gCost2, findHCost2(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    } else { // Continue Up
                        if(!isBlocked(current.getDirection(), current.getxAxis(), current.getyAxis() - UNIT_SIZE)) {
                            n = new Node(current.getxAxis(), current.getyAxis() - UNIT_SIZE, gCost2, findHCost2(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    }
                } else if (i == 1) {
                    if (current.getDirection() == 'R') { // Turn Down
                        if(!isBlocked('D', current.getxAxis(), current.getyAxis() + UNIT_SIZE)) {
                            n = new Node(current.getxAxis(), current.getyAxis() + UNIT_SIZE, gCost2, findHCost2(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    } else if (current.getDirection() == 'L') { // Turn Up
                        if(!isBlocked('U', current.getxAxis(), current.getyAxis() - UNIT_SIZE)) {
                            n = new Node(current.getxAxis(), current.getyAxis() - UNIT_SIZE, gCost2, findHCost2(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    } else if (current.getDirection() == 'D') { // Turn Left
                        if(!isBlocked('L', current.getxAxis() - UNIT_SIZE, current.getyAxis())) {
                            n = new Node(current.getxAxis() - UNIT_SIZE, current.getyAxis(), gCost2, findHCost2(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    } else { // Turn Right
                        if(!isBlocked('R', current.getxAxis() + UNIT_SIZE, current.getyAxis())) {
                            n = new Node(current.getxAxis() + UNIT_SIZE, current.getyAxis(), gCost2, findHCost2(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    }
                } else {
                    if (current.getDirection() == 'R') { // Turn Up
                        if(!isBlocked('U', current.getxAxis(), current.getyAxis() - UNIT_SIZE)) {
                            n = new Node(current.getxAxis(), current.getyAxis() - UNIT_SIZE, gCost2, findHCost2(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    } else if (current.getDirection() == 'L') { // Turn Down
                        if(!isBlocked('D', current.getxAxis(), current.getyAxis() + UNIT_SIZE)) {
                            n = new Node(current.getxAxis(), current.getyAxis() + UNIT_SIZE, gCost2, findHCost2(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    } else if (current.getDirection() == 'D') { // Turn Right
                        if(!isBlocked('R', current.getxAxis() + UNIT_SIZE, current.getyAxis())) {
                            n = new Node(current.getxAxis() + UNIT_SIZE, current.getyAxis(), gCost2, findHCost2(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    } else { // Turn Left
                        if(!isBlocked('L', current.getxAxis() - UNIT_SIZE, current.getyAxis())) {
                            n = new Node(current.getxAxis() - UNIT_SIZE, current.getyAxis(), gCost2, findHCost2(current.getxAxis(), current.getyAxis()));
                            if (open.contains(n) || closed.contains(n)) {
                                exists = true;
                            }
                        } else {
                            continue;
                        }
                    }
                }

                if (exists && n.isClosed()) {
                    continue;
                }

                if (n.getFCost() <= current.getFCost() || !open.contains(n)) {
                    n.setParent(current);
                    if (!open.contains(n)) {
                        n.setgCost(n.getParent().getgCost() + gCost2);
                        open.add(n);
                    }
                }
            }
        }
        return null;
    }

    private int findHCost1(int xAxis, int yAxis) {
        hCost1 = 0;
        xDistance1 = Math.abs((appleX - xAxis) / UNIT_SIZE);
        yDistance1 = Math.abs((appleY - yAxis) / UNIT_SIZE);
        if (yDistance1 != 0) {
            hCost1 = 4;
        }
        hCost1 += (xDistance1 * 10) + (yDistance1 * 10);
        return hCost1;
    }
    private int findHCost2(int xAxis, int yAxis) {
        hCost2 = 0;
        xDistance2 = Math.abs((appleX - xAxis) / UNIT_SIZE);
        yDistance2 = Math.abs((appleY - yAxis) / UNIT_SIZE);
        if (yDistance2 != 0) {
            hCost2 = 4;
        }
        hCost2 += (xDistance2 * 10) + (yDistance2 * 10);
        return hCost2;
    }

    private void pathFinder1() {
        int hCostA = 0;
        int hCostB = 0;
        int hCostC = 0;
        boolean blocked = false;
        int fCostA = 999999999; //if blocked the number will be 999999999
        int fCostB = 999999999;
        int fCostC = 999999999;

        switch(direction1) {
            case 'U':
                hCostA = 0;
                hCostB = 0;
                hCostC = 0;

                // If space to go up
                if (y1[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for(int i = body1; i>0; i--) {
                        if((x1[0] == x1[i]) && (y1[0] - UNIT_SIZE == y1[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going up
                        xDistance1 = Math.abs((appleX - x1[0]) / UNIT_SIZE);
                        yDistance1 = Math.abs((appleY - (y1[0] - UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance1 != 0) {
                            hCostA = 4;
                        }
                        hCostA+= (xDistance1 * 10) + (yDistance1 * 10);
                        fCostA = hCostA + 10;
                    }
                    blocked = false;
                }

                // If space to go left
                if(x1[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for(int i = body1; i>0; i--) {
                        if((x1[0] - UNIT_SIZE == x1[i]) && (y1[0] == y1[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going left
                        xDistance1 = Math.abs((appleX - (x1[0] - UNIT_SIZE)) / UNIT_SIZE);
                        yDistance1 = Math.abs((appleY - y1[0]) / UNIT_SIZE);
                        if (yDistance1 != 0) {
                            hCostB = 4;
                        }
                        hCostB+= (xDistance1 * 10) + (yDistance1 * 10);
                        fCostB = hCostB + 14;
                    }
                    blocked = false;
                }

                // If space to go right
                if(x1[0] + UNIT_SIZE < WIDTH) {
                    // If no body parts blocking
                    for(int i = body1; i>0; i--) {
                        if((x1[0] + UNIT_SIZE == x1[i]) && (y1[0] == y1[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going right
                        xDistance1 = Math.abs((appleX - (x1[0] + UNIT_SIZE)) / UNIT_SIZE);
                        yDistance1 = Math.abs((appleY - y1[0]) / UNIT_SIZE);
                        if (yDistance1 != 0) {
                            hCostC = 4;
                        }
                        hCostC+= (xDistance1 * 10) + (yDistance1 * 10);
                        fCostC = hCostC + 14;
                    }
                    blocked = false;
                }

                if(fCostA <= fCostB && fCostA <= fCostC) {  // the up neighbor cost is the less
                    direction1 = 'U';
                } else if (fCostB < fCostA && fCostB <= fCostC) {
                    direction1 = 'L';
                } else if(fCostC < fCostB && fCostC < fCostA) {
                    direction1 = 'R';
                }
                fCostA = 999999999;
                fCostB = 999999999;
                fCostC = 999999999;

                break;

            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            case 'D':
                hCostA = 0;
                hCostB = 0;
                hCostC = 0;

                // If space to go down
                if (y1[0] + UNIT_SIZE < HEIGHT) {
                    // If no body parts blocking
                    for (int i = body1; i > 0; i--) {
                        if ((x1[0] == x1[i]) && (y1[0] + UNIT_SIZE == y1[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going down
                        xDistance1 = Math.abs((appleX - x1[0]) / UNIT_SIZE);
                        yDistance1 = Math.abs((appleY - (y1[0] + UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance1 != 0) {
                            hCostA = 4;
                        }
                        hCostA += (xDistance1 * 10) + (yDistance1 * 10);
                        fCostA = hCostA + 10;
                    }
                    blocked = false;
                }

                // If space to go left
                if (x1[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for (int i = body1; i > 0; i--) {
                        if ((x1[0] - UNIT_SIZE == x1[i]) && (y1[0] == y1[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going left
                        xDistance1 = Math.abs((appleX - (x1[0] - UNIT_SIZE)) / UNIT_SIZE);
                        yDistance1 = Math.abs((appleY - y1[0]) / UNIT_SIZE);
                        if (yDistance1 != 0) {
                            hCostB = 4;
                        }
                        hCostB += (xDistance1 * 10) + (yDistance1 * 10);
                        fCostB = hCostB + 14;
                    }
                    blocked = false;
                }

                // If space to go right
                if (x1[0] + UNIT_SIZE < WIDTH) {
                    // If no body parts blocking
                    for (int i = body1; i > 0; i--) {
                        if ((x1[0] + UNIT_SIZE == x1[i]) && (y1[0] == y1[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going right
                        xDistance1 = Math.abs((appleX - (x1[0] + UNIT_SIZE)) / UNIT_SIZE);
                        yDistance1 = Math.abs((appleY - y1[0]) / UNIT_SIZE);
                        if (yDistance1 != 0) {
                            hCostC = 4;
                        }
                        hCostC += (xDistance1 * 10) + (yDistance1 * 10);
                        fCostC = hCostC + 14;
                    }
                    blocked = false;
                }

                if (fCostA <= fCostB && fCostA <= fCostC) {
                    direction1 = 'D';
                } else if (fCostB < fCostA && fCostB <= fCostC) {
                    direction1 = 'L';
                } else if (fCostC < fCostB && fCostC < fCostA) {
                    direction1 = 'R';
                }
                //fCostA = 999999999;
                //fCostB = 999999999;
                //fCostC = 999999999;

                break;

            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            case 'L':
                hCostA = 0;
                hCostB = 0;
                hCostC = 0;

                // If space to go left
                if (x1[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for (int i = body1; i > 0; i--) {
                        if ((x1[0] - UNIT_SIZE == x1[i]) && (y1[0] == y1[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going left
                        xDistance1 = Math.abs((appleX - (x1[0] - UNIT_SIZE)) / UNIT_SIZE);
                        yDistance1 = Math.abs((appleY - y1[0]) / UNIT_SIZE);
                        if (yDistance1 != 0) {
                            hCostA = 4;
                        }
                        hCostA += (xDistance1 * 10) + (yDistance1 * 10);
                        fCostA = hCostA + 10;
                    }
                    blocked = false;
                }

                // If space to go down
                if (y1[0] + UNIT_SIZE < HEIGHT) {
                    // If no body parts blocking
                    for (int i = body1; i > 0; i--) {
                        if ((x1[0] == x1[i]) && (y1[0] + UNIT_SIZE == y1[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going down
                        xDistance1 = Math.abs((appleX - x1[0]) / UNIT_SIZE);
                        yDistance1 = Math.abs((appleY - (y1[0] + UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance1 != 0) {
                            hCostB = 4;
                        }
                        hCostB += (xDistance1 * 10) + (yDistance1 * 10);
                        fCostB = hCostB + 14;
                    }
                    blocked = false;
                }

                // If space to go up
                if (y1[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for (int i = body1; i > 0; i--) {
                        if ((x1[0] == x1[i]) && (y1[0] - UNIT_SIZE == y1[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going up
                        xDistance1 = Math.abs((appleX - x1[0]) / UNIT_SIZE);
                        yDistance1 = Math.abs((appleY - (y1[0] - UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance1 != 0) {
                            hCostC = 4;
                        }
                        hCostC += (xDistance1 * 10) + (yDistance1 * 10);
                        fCostC = hCostC + 14;
                    }
                    blocked = false;
                }

                if (fCostA <= fCostB && fCostA <= fCostC) {
                    direction1 = 'L';
                } else if (fCostB < fCostA && fCostB <= fCostC) {
                    direction1 = 'D';
                } else if (fCostC < fCostB && fCostC < fCostA) {
                    direction1 = 'U';
                }

                fCostA = 999999999;
                fCostB = 999999999;
                fCostC = 999999999;

                break;

            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            case 'R':
                hCostA = 0;
                hCostB = 0;
                hCostC = 0;

                // If space to go right
                if (x1[0] + UNIT_SIZE < WIDTH) {
                    // If no body parts blocking
                    for (int i = body1; i > 0; i--) {
                        if ((x1[0] + UNIT_SIZE == x1[i]) && (y1[0] == y1[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going right
                        xDistance1 = Math.abs((appleX - (x1[0] + UNIT_SIZE)) / UNIT_SIZE);
                        yDistance1 = Math.abs((appleY - y1[0]) / UNIT_SIZE);
                        if (yDistance1 != 0) {
                            hCostA = 4;
                        }
                        hCostA += (xDistance1 * 10) + (yDistance2 * 10);
                        fCostA = hCostA + 10;
                    }
                    blocked = false;
                }

                // If space to go down
                if (y1[0] + UNIT_SIZE < HEIGHT) {
                    // If no body parts blocking
                    for (int i = body1; i > 0; i--) {
                        if ((x1[0] == x1[i]) && (y1[0] + UNIT_SIZE == y1[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going down
                        xDistance1 = Math.abs((appleX - x1[0]) / UNIT_SIZE);
                        yDistance1 = Math.abs((appleY - (y1[0] + UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance1 != 0) {
                            hCostB = 4;
                        }
                        hCostB += (xDistance1 * 10) + (yDistance1 * 10);
                        fCostB = hCostB + 14;
                    }
                    blocked = false;
                }

                // If space to go up
                if (y1[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for (int i = body1; i > 0; i--) {
                        if ((x1[0] == x1[i]) && (y1[0] - UNIT_SIZE == y1[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going up
                        xDistance1 = Math.abs((appleX - x1[0]) / UNIT_SIZE);
                        yDistance1 = Math.abs((appleY - (y1[0] - UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance1 != 0) {
                            hCostC = 4;
                        }
                        hCostC += (xDistance1 * 10) + (yDistance1 * 10);
                        fCostC = hCostC + 14;
                    }
                    blocked = false;
                }

                if (fCostA <= fCostB && fCostA <= fCostC) {
                    direction1 = 'R';
                } else if (fCostB < fCostA && fCostB <= fCostC) {
                    direction1 = 'D';
                } else if (fCostC < fCostB && fCostC < fCostA) {
                    direction1 = 'U';
                }

                //fCostA = 999999999;
                //fCostB = 999999999;
                //fCostC = 999999999;

                break;
        }
    }

    private void pathFinder2() {
        int hCostA = 0;
        int hCostB = 0;
        int hCostC = 0;
        boolean blocked = false;
        int fCostA = 999999999;
        int fCostB = 999999999;
        int fCostC = 999999999;

        switch(direction2) {
            case 'U':
                hCostA = 0;
                hCostB = 0;
                hCostC = 0;

                // If space to go up
                if (y2[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for(int i = body2; i>0; i--) {
                        if((x2[0] == x2[i]) && (y2[0] - UNIT_SIZE == y2[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going up
                        xDistance2 = Math.abs((appleX - x2[0]) / UNIT_SIZE);
                        yDistance2 = Math.abs((appleY - (y2[0] - UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance2 != 0) {
                            hCostA = 4;
                        }
                        hCostA+= (xDistance2 * 10) + (yDistance2 * 10);
                        fCostA = hCostA + 10;
                    }
                    blocked = false;
                }

                // If space to go left
                if(x2[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for(int i = body2; i>0; i--) {
                        if((x2[0] - UNIT_SIZE == x2[i]) && (y2[0] == y2[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going left
                        xDistance2 = Math.abs((appleX - (x2[0] - UNIT_SIZE)) / UNIT_SIZE);
                        yDistance2 = Math.abs((appleY - y2[0]) / UNIT_SIZE);
                        if (yDistance2 != 0) {
                            hCostB = 4;
                        }
                        hCostB+= (xDistance2 * 10) + (yDistance2 * 10);
                        fCostB = hCostB + 14;
                    }
                    blocked = false;
                }

                // If space to go right
                if(x2[0] + UNIT_SIZE < WIDTH) {
                    // If no body parts blocking
                    for(int i = body2; i>0; i--) {
                        if((x2[0] + UNIT_SIZE == x2[i]) && (y2[0] == y2[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going right
                        xDistance2 = Math.abs((appleX - (x2[0] + UNIT_SIZE)) / UNIT_SIZE);
                        yDistance2 = Math.abs((appleY - y2[0]) / UNIT_SIZE);
                        if (yDistance2 != 0) {
                            hCostC = 4;
                        }
                        hCostC+= (xDistance2 * 10) + (yDistance2 * 10);
                        fCostC = hCostC + 14;
                    }
                    blocked = false;
                }

                if(fCostA <= fCostB && fCostA <= fCostC) {
                    direction2 = 'U';
                } else if (fCostB < fCostA && fCostB <= fCostC) {
                    direction2 = 'L';
                } else if(fCostC < fCostB && fCostC < fCostA) {
                    direction2 = 'R';
                }
                fCostA = 999999999;
                fCostB = 999999999;
                fCostC = 999999999;

                break;

            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            case 'D':
                hCostA = 0;
                hCostB = 0;
                hCostC = 0;

                // If space to go down
                if (y2[0] + UNIT_SIZE < HEIGHT) {
                    // If no body parts blocking
                    for (int i = body2; i > 0; i--) {
                        if ((x2[0] == x2[i]) && (y2[0] + UNIT_SIZE == y2[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going down
                        xDistance2 = Math.abs((appleX - x2[0]) / UNIT_SIZE);
                        yDistance2 = Math.abs((appleY - (y2[0] + UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance2 != 0) {
                            hCostA = 4;
                        }
                        hCostA += (xDistance2 * 10) + (yDistance2 * 10);
                        fCostA = hCostA + 10;
                    }
                    blocked = false;
                }

                // If space to go left
                if (x2[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for (int i = body2; i > 0; i--) {
                        if ((x2[0] - UNIT_SIZE == x2[i]) && (y2[0] == y2[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going left
                        xDistance2 = Math.abs((appleX - (x2[0] - UNIT_SIZE)) / UNIT_SIZE);
                        yDistance2 = Math.abs((appleY - y2[0]) / UNIT_SIZE);
                        if (yDistance2 != 0) {
                            hCostB = 4;
                        }
                        hCostB += (xDistance2 * 10) + (yDistance2 * 10);
                        fCostB = hCostB + 14;
                    }
                    blocked = false;
                }

                // If space to go right
                if (x2[0] + UNIT_SIZE < WIDTH) {
                    // If no body parts blocking
                    for (int i = body2; i > 0; i--) {
                        if ((x2[0] + UNIT_SIZE == x2[i]) && (y2[0] == y2[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going right
                        xDistance2 = Math.abs((appleX - (x2[0] + UNIT_SIZE)) / UNIT_SIZE);
                        yDistance2 = Math.abs((appleY - y2[0]) / UNIT_SIZE);
                        if (yDistance2 != 0) {
                            hCostC = 4;
                        }
                        hCostC += (xDistance2 * 10) + (yDistance2 * 10);
                        fCostC = hCostC + 14;
                    }
                    blocked = false;
                }

                if (fCostA <= fCostB && fCostA <= fCostC) {
                    direction2 = 'D';
                } else if (fCostB < fCostA && fCostB <= fCostC) {
                    direction2 = 'L';
                } else if (fCostC < fCostB && fCostC < fCostA) {
                    direction2 = 'R';
                }
                fCostA = 999999999;
                fCostB = 999999999;
                fCostC = 999999999;

                break;

            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            case 'L':
                hCostA = 0;
                hCostB = 0;
                hCostC = 0;

                // If space to go left
                if (x2[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for (int i = body2; i > 0; i--) {
                        if ((x2[0] - UNIT_SIZE == x2[i]) && (y2[0] == y2[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going left
                        xDistance2 = Math.abs((appleX - (x2[0] - UNIT_SIZE)) / UNIT_SIZE);
                        yDistance2 = Math.abs((appleY - y2[0]) / UNIT_SIZE);
                        if (yDistance2 != 0) {
                            hCostA = 4;
                        }
                        hCostA += (xDistance2 * 10) + (yDistance2 * 10);
                        fCostA = hCostA + 10;
                    }
                    blocked = false;
                }

                // If space to go down
                if (y2[0] + UNIT_SIZE < HEIGHT) {
                    // If no body parts blocking
                    for (int i = body2; i > 0; i--) {
                        if ((x2[0] == x2[i]) && (y2[0] + UNIT_SIZE == y2[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going down
                        xDistance2 = Math.abs((appleX - x2[0]) / UNIT_SIZE);
                        yDistance2 = Math.abs((appleY - (y2[0] + UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance2 != 0) {
                            hCostB = 4;
                        }
                        hCostB += (xDistance2 * 10) + (yDistance2 * 10);
                        fCostB = hCostB + 14;
                    }
                    blocked = false;
                }

                // If space to go up
                if (y2[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for (int i = body2; i > 0; i--) {
                        if ((x2[0] == x2[i]) && (y2[0] - UNIT_SIZE == y2[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going up
                        xDistance2 = Math.abs((appleX - x2[0]) / UNIT_SIZE);
                        yDistance2 = Math.abs((appleY - (y2[0] - UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance2 != 0) {
                            hCostC = 4;
                        }
                        hCostC += (xDistance2 * 10) + (yDistance2 * 10);
                        fCostC = hCostC + 14;
                    }
                    blocked = false;
                }

                if (fCostA <= fCostB && fCostA <= fCostC) {
                    direction2 = 'L';
                } else if (fCostB < fCostA && fCostB <= fCostC) {
                    direction2 = 'D';
                } else if (fCostC < fCostB && fCostC < fCostA) {
                    direction2 = 'U';
                }

                fCostA = 999999999;
                fCostB = 999999999;
                fCostC = 999999999;

                break;
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            case 'R':
                hCostA = 0;
                hCostB = 0;
                hCostC = 0;

                // If space to go right
                if (x2[0] + UNIT_SIZE < WIDTH) {
                    // If no body parts blocking
                    for (int i = body2; i > 0; i--) {
                        if ((x2[0] + UNIT_SIZE == x2[i]) && (y2[0] == y2[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going right
                        xDistance2 = Math.abs((appleX - (x2[0] + UNIT_SIZE)) / UNIT_SIZE);
                        yDistance2 = Math.abs((appleY - y2[0]) / UNIT_SIZE);
                        if (yDistance2 != 0) {
                            hCostA = 4;
                        }
                        hCostA += (xDistance2 * 10) + (yDistance2 * 10);
                        fCostA = hCostA + 10;
                    }
                    blocked = false;
                }

                // If space to go down
                if (y2[0] + UNIT_SIZE < HEIGHT) {
                    // If no body parts blocking
                    for (int i = body2; i > 0; i--) {
                        if ((x2[0] == x2[i]) && (y2[0] + UNIT_SIZE == y2[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going down
                        xDistance2 = Math.abs((appleX - x2[0]) / UNIT_SIZE);
                        yDistance2 = Math.abs((appleY - (y2[0] + UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance2 != 0) {
                            hCostB = 4;
                        }
                        hCostB += (xDistance2 * 10) + (yDistance2 * 10);
                        fCostB = hCostB + 14;
                    }
                    blocked = false;
                }

                // If space to go up
                if (y2[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for (int i = body2; i > 0; i--) {
                        if ((x2[0] == x2[i]) && (y2[0] - UNIT_SIZE == y2[i])) {
                            blocked = true;
                            break;
                        }
                    }

                    if (blocked != true) {
                        // Going up
                        xDistance2 = Math.abs((appleX - x2[0]) / UNIT_SIZE);
                        yDistance2 = Math.abs((appleY - (y2[0] - UNIT_SIZE)) / UNIT_SIZE);
                        if (yDistance2 != 0) {
                            hCostC = 4;
                        }
                        hCostC += (xDistance2 * 10) + (yDistance2 * 10);
                        fCostC = hCostC + 14;
                    }
                    blocked = false;
                }

                if (fCostA <= fCostB && fCostA <= fCostC) {
                    direction2 = 'R';
                } else if (fCostB < fCostA && fCostB <= fCostC) {
                    direction2 = 'D';
                } else if (fCostC < fCostB && fCostC < fCostA) {
                    direction2 = 'U';
                }

                fCostA = 999999999;
                fCostB = 999999999;
                fCostC = 999999999;

                break;
        }
    }
}