/*
* Blasters.java
* Created by Harry Rol on 11/8/2013
* Edited by Logan Wholey
*/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.sound.sampled.*;
import java.io.*;
import java.awt.Rectangle;

class Blasters {
        // laser size and speed variables
        private static final double SPEED = 200.0;
        private static final int WIDTH = 2;
        private static final int HEIGHT = 15;
        
        // game variables
        private int score = 0;
        private boolean lost = false;
        
        // player ship variables
        private Ship ship;        
        private double x, y, dy;
        private ArrayList<Blasters> barrage;

        // enemy variables
        private ArrayList<enemyShip> fleet;
        private ArrayList<Blasters> enemyBarrage;


        // sound variables
        private Sound explosion;
        private Sound laser;

        public Blasters(double xCoord, double yCoord, boolean enemy) {
                x = xCoord;
                y = yCoord;
                if (enemy == true) {
                        dy = SPEED;
                } else {
                        dy = -SPEED;
                }

                // intialize sounds
                explosion = new Sound("explosion.aiff");
                laser = new Sound("laser2.aiff");
                laser.play();
        }

        // draw the blast
        public void draw(Graphics g) { g.fillRect((int)x, (int)y, WIDTH, HEIGHT); }

        // direction of the barrage
        public void up() {dy = -SPEED;}
        public void down() {dy = SPEED;}
        
        // return if game was lost
        public boolean lose() { return lost; }

        // determine if enemy blaster collides with ship
        boolean collidesShip(Ship s) {
                // establish a rectangle for the player ship
                Rectangle ship = new Rectangle((int)s.getX() + 5, (int)s.getY(), 30, 50);

                // determine if an enemy blast intersects ship rectangle
                for(int i = 0; i < enemyBarrage.size(); i++) {
                        Rectangle blaster = new Rectangle((int)enemyBarrage.get(i).getX(), (int)enemyBarrage.get(i).getY(), WIDTH, HEIGHT);
                        if (blaster.intersects(ship)) {
                                return true; // hit
                        }
                }
                return false; // miss
        }
        
        // determine if a blaster collides with an enemy ship
        boolean collidesEnemy() {
                for(int i = 0; i < barrage.size(); i++) {
                        // establish a rectangle for the laser
                        Rectangle blaster = new Rectangle((int)barrage.get(i).getX(), (int)barrage.get(i).getY(), WIDTH, HEIGHT);
                        
                        // determine if the laser intersects an enemy
                        for (int q = 0; q < fleet.size(); q++) {
                                Rectangle enemy = new Rectangle((int)fleet.get(q).getX(), (int)fleet.get(q).getY(), 40, 36);        
                                
                                // if blaster hits a ship, remove it
                                if (blaster.intersects(enemy) && fleet.get(q).getCount() == 0) {
                                        fleet.get(q).hit = true; // set the enemy hit
                                        barrage.remove(barrage.get(i));
                                        score += 100; // add to score
                                        return true; // hit
                                }
                        }
                }
                return false; // miss
        }
        
        // get the coordinates of a blast
        public int getY() { return (int)y; }
        public int getX() { return (int)x; }        
        
        // update the blasters
        public void update(double dt, Ship s, ArrayList<enemyShip> e, ArrayList<Blasters> b, ArrayList<Blasters> eb, HighScore hs) {
                // Update the position
                y += (dy * dt);
                
                // set the objects equal
                fleet = e;
                barrage = b;
                ship = s;
                enemyBarrage = eb;
                
                // remove the blasts from the array if they are out of range
                for (int i = 0; i < b.size(); i++) {
                        if (b.get(i).getY() > 500) {
                                b.remove(b.get(i));
                        } else if (b.get(i).getY() < 0) {
                                b.remove(b.get(i));
                        }
                }

                for (int i = 0; i< eb.size(); i++) {
                        if (eb.get(i).getY() > 500) {
                                eb.remove(eb.get(i));
                        } else if (eb.get(i).getY() < 0) {
                                eb.remove(eb.get(i));
                        }
                }
                
                // if the blaster collides with an enemy
                if (collidesEnemy()) {
                        hs.addScore(100);
                        explosion.play();
                }
                
                // if the enemy blaster collides with the ship
                if (collidesShip(s)) {
                        lost = true;
                        explosion.play();
                }
        }
}