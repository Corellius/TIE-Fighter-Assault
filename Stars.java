/*
* created by Harry on 11/8/2013
* Runs the space background of the game
*/

//all comments groomed by Douglas on December 7th

import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.util.*;

//background for the game
class Star {
        private int x, y, dx, dy;

        public Star( ) {
                Random r = new Random( );
                x = r.nextInt(500);
                y = r.nextInt(500);
                dy = r.nextInt(3) + 9;
        }

        public void draw(Graphics g) { g.fillRect(x, y, 3, 3); }

        public void update(int dx ) {
                y += dy;
                x += dx;
                if(y < 0) y = 500;
                if(y > 500) y = 0;
                if(x < 0) x = 500;
                if(x > 500) x = 0;
        }
}


class GameWorld extends JComponent implements KeyListener {
        private ArrayList<Star> stars;
        private ArrayList<Blasters> barrage;
        private ArrayList<Blasters> enemyBarrage;
        private Ship ship;
        private ArrayList<enemyShip> enemy;
        private long elapsed;
        private boolean hit = false; // if the ship was hit
        private boolean enemyHit = false; // if the enemy was hit
        private int xMove = 0;
        private boolean lost = false;
        
        private Sound music;
	private Sound laser;
        private Image gameover; // game over image

        // player info
        private HighScore intro;
        private int highscore = 0;
        private String name = null;

        // generates enemies
        public void enemyGenerator() {
                int x = 20;
                int y = 0;
                for (int i = 0; i < 3; i++) {
                        for (int q = 0; q < 7; q++) {
                                enemy.add(new enemyShip(x, y));
                                x+= 70;
                        }
                        x = 20;
                        y += 50;
                }
        }

        public GameWorld( ) {
                // get info from player
                intro = new HighScore();
                name = intro.getName();
                highscore = intro.getHighScore();

                elapsed = new Date( ).getTime( );
                ship = new Ship( );
                enemy = new ArrayList<enemyShip>();
                stars = new ArrayList<Star>( );
                barrage = new ArrayList<Blasters>();
                enemyBarrage = new ArrayList<Blasters>();
                
                // add stars to background
                for(int i = 0; i < 100; i++) { stars.add(new Star( )); }
                
                // add some enemies
                enemyGenerator();

                // load the game over image
                try {
                        gameover = ImageIO.read(new File("gameover.png"));
                } catch (Exception e) {
                        gameover = null;
                }

                // playe background music
                try {
                        music = new Sound("music.wav");
                        music.gameMusic();
                } catch (Exception butss) {
                        music = null;
                }
        }

        public void keyTyped(KeyEvent e) {
                // added just in case we needed it...
        }

        // get the key pressed for movement
        public void keyPressed(KeyEvent e) {
                if (hit == false && e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        ship.right( );
                        xMove = 9;
                } else if (hit == false && e.getKeyCode() == KeyEvent.VK_LEFT) {
                        ship.left( );
                        xMove = -9;
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE && barrage.size() <= 10) {
                        if (lost == false) {
				laser = new Sound("laser.wav");
                		laser.play();
                                barrage.add(new Blasters(ship.cannonAnchor(), 440, false));
                                
                                // enemy shoots back
                                Random rand = new Random();
                                int size = enemy.size();
                                int n = rand.nextInt(size);
                                if (size > 0) {
                                        enemyBarrage.add(new Blasters(enemy.get(n).getX() + 16, enemy.get(n).getY() + 36, true));
                                }
                        }
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        System.exit(0); // close the program
                }
        }

        // stop the ship when keys are released
        public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() != KeyEvent.VK_SPACE){
                        ship.stop( );
                        xMove = 0;
                }
        }

        public void paintComponent(Graphics g) {
                /* set the color to light blue */
                g.setColor(new Color(0, 0, 0));
                g.fillRect(0, 0, 500, 500);

                // draw the stars
                g.setColor(Color.white);
                for(Star f : stars) { f.draw(g); }

                // draw the blasters
                g.setColor(Color.red);
                for(Blasters b : barrage) { b.draw(g); }

                // draw the enemy barrage
                g.setColor(Color.green);
                for(Blasters b : enemyBarrage) { b.draw(g); }

                // draw the ship
                ship.draw(g);

                // draw the enemy
                for (enemyShip e : enemy) { e.draw(g); }

                /* highscore and score */
                g.setColor(Color.black);
                g.drawString("High Score", 0, 520);
                g.drawString("Score", 350, 520);
                g.drawString(Integer.toString(intro.getScore()), 420, 520);
                g.drawString(intro.getChamp(), 90, 520);
                g.drawString(Integer.toString(intro.getHighScore()), 150, 520);

                // update the stars
                for(Star f : stars) { f.update(xMove); }
                
                // update the times
                long time_now = new Date( ).getTime( );
                double seconds = (time_now - elapsed) / 1000.0f;
                elapsed = time_now;
        
                // update the blasters
                for (int i = 0; i < barrage.size(); i++) {
                        barrage.get(i).update(seconds, ship, enemy, barrage, enemyBarrage, intro);
                }

                // update the enemy blasters
                for (int i = 0; i < enemyBarrage.size(); i++) {
                        enemyBarrage.get(i).update(seconds, ship, enemy, barrage, enemyBarrage, intro);
                }

                // update the ship
                ship.update(seconds, hit); // needs the hit variable
                
                //update the enemy
                //for (enemyShip e : enemy) { e.move(); }
                for (enemyShip e : enemy) { e.update(seconds); }

                /* force an update */
                revalidate( );
                repaint( );
                
                // determine if game is over
                for (enemyShip e : enemy) {
                        if (e.lose()) {
                                lost = true;
                                enemy.clear();
                        }

                        if (e.lose() && intro.getScore() > highscore) {
                                intro.setHighScore();
                        }
                }        
        
                // blast hits the ship
                for (Blasters b : enemyBarrage) {
                        if (b.lose()) {
                                lost = true;
                                hit = true;
                                enemy.clear();
                        }
                        if (b.lose() && intro.getScore() > highscore) {
                                intro.setHighScore();
                        }
                }

                // remove the enemy ship if hit
                for (int i = 0; i < enemy.size(); i++) {
                        if (enemy.get(i).getImage() == null) {
                                enemy.remove(enemy.get(i));
                        }
                }

                // display game over message
                if (lost) { g.drawImage(gameover, 70, 50, null); }

                // create more ships if it is empty
                if (enemy.isEmpty() && lost == false) { enemyGenerator(); }        

                // update the highscore
                if (intro.getScore() > highscore) { intro.setHighScore(); }        
                
                /* sleep for 1/20th of a second */
                try {
                        Thread.sleep(50);
                } catch(InterruptedException e) {
                        Thread.currentThread( ).interrupt( );
                }
        }
}


public class Stars {
        public static void main(String args[]) {
                // create and set up the window.
                JFrame frame = new JFrame("Star Wars");

                // make the program close when the window closes
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // add the GameWorld component
                GameWorld g = new GameWorld( );
                frame.add(g);
                frame.addKeyListener(g);
                
                // display the window.
                frame.setSize(500, 555);
                frame.setVisible(true);
        }
}