/* Filename: Ship.java
 * Author: Logan Wholey
 * Date: 11.08.2013
*/
import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.util.*;

class Ship {
        private static final double SPEED = 150.0;
        private Image[] ship;
        private double x, y, dx, dy;
	boolean hit;
	private int count;

        // ship constructor
        public Ship( ) {
                // get the ship & explosion image
                try {
			ship = new Image[8];
                        ship[0] = ImageIO.read(new File("mf.png"));
                        ship[1] = ImageIO.read(new File("explosion.png"));
			ship[2] = ImageIO.read(new File("explosion1.png"));
			ship[3] = ImageIO.read(new File("explosion2.png"));
			ship[4] = ImageIO.read(new File("explosion3.png"));
			ship[5] = ImageIO.read(new File("explosion4.png"));
			ship[6] = ImageIO.read(new File("explosion5.png"));
			ship[7] = null;
                } catch(Exception e) {
                        ship = null;
                }
		
		hit = false;
		count = 0;

                // initial ship location
                x = 250 - 40;
                y = 500 - 60;
                dx = dy = 0;
        }

	// draw the ship
        public void draw(Graphics g) {
		// explosion animation if hit
		if (hit && count < 7) {
			count++;
		}
		g.drawImage(ship[count], (int)x, (int)y, null);
        }

        // stop ship
        public void stop( ) { dx = dy = 0; }

        // reset ship, for game replay
        public void restart( ) {
                dx = dy = 0;
                x = 250 - 40;
                y = 500 - 60;
        }

        // left & right movement
        public void left( ) {dx = -SPEED;}
        public void right( ) {dx = SPEED;}

	// return x and y coords
	public int getX() { return (int)x; }
	public int getY() { return (int)y; }

        // update ship
        public void update(double dt, boolean h) {
                x += (dx * dt);
                y += (dy * dt);
		hit = h;
        
                // make sure ship is not out of bounds
                if(y < 0) y = 0;
                    if(y > 500) y = 500;
                    if(x < 0) x = 0;
                    if(x > 500 - 40) x = 500 - 40;        
        }
        
        // location of cannon
        public int cannonAnchor( ) { return (int)x + 36; }        
}
