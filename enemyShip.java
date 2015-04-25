//Originally coded by Douglas Radoye
//the enemy ship
import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.util.*;

class enemyShip {
	private static final double SPEED = 100.0; //15.0;
	private static final double MOVE = 10.0; //15.0;
	private Image[] enemy;
	private double x, y, dx, dy;
	private boolean lost;
	public boolean hit;
	private int count;
	
	private int where = 1;
	private double MOVE_TIME = 2.0;
	private double timer = 0.0;

	public enemyShip(int x, int y) {
		try {
			//images needed for the enemy and to explode him
			enemy = new Image[8];
			enemy[0] = ImageIO.read(new File("enemy.png"));
 			enemy[1] = ImageIO.read(new File("explosion.png"));
                        enemy[2] = ImageIO.read(new File("explosion1.png"));
                        enemy[3] = ImageIO.read(new File("explosion2.png"));
                        enemy[4] = ImageIO.read(new File("explosion3.png"));
                        enemy[5] = ImageIO.read(new File("explosion4.png"));
                        enemy[6] = ImageIO.read(new File("explosion5.png"));
                        enemy[7] = null;
		} catch(Exception e) {
			enemy = null;
		}

		this.x = (x - 10);
		this.y = y;
		dx = dy = 0;
		lost = false;
		hit = false;
		count = 0;
	}

	public void draw(Graphics g) {
		// explosion animation if hit
		if (hit && count < 7) { count++; }
	
		/* draw enemy ship on the screen */
		g.drawImage(enemy[count], (int)x, (int)y, null);
	}

	public int getX() { return (int)x; }
	public int getY() { return (int)y; }

	/* stop enemy */
	public void stop( ) {
		dx = dy = 0;
	}

	//adds a way to change the time the ships pause
	public void changeTime(int m){
		MOVE_TIME = m;
	}
	
	//move
	public void move(){
		//System.out.println(where);
		if(where == 1)
		{
			down();
			where++;
		}else if(where == 2)
		{
			right();
			where++;
		}else if(where == 3)
		{
			down();
			where++;
		}else if(where == 4)
		{
			left();
			where = 1;
		}
	}

	/* left/up/right/down */
	public void left( ) {
		x -= (2 *MOVE);
	}
	public void up( ) {
		y -= MOVE;
	}
	public void right( ) {
		x += (2 *MOVE);
	}
	public void down( ) {
		y += MOVE;
	}

	// return if lost
	public boolean lose() { return lost; }

	// return the ship image
	public Image getImage() { return enemy[count];}
	
	// return the count
	public int getCount() { return count; }
	
	/* update him */
	public void update(double dt) {
		timer += dt;
		if(timer >= MOVE_TIME) {
			timer = 0;
			move();
		}

		if(y < 0) y = 0;
		if(y > 500-36) {
			lost = true;
			y = 500 - 36;
		}
		if(x < 0) x = 0;
		if(x > 500) x = 500;

	}
}
