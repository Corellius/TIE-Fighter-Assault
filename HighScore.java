/* HighScore.java
 * Author: Logan Wholey
 * Date: 11.24.2013
*/

import javax.swing.*;
import javax.imageio.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.util.*;

class HighScore extends JFrame {
	private String name; // player name
	private String champion; // highscore name
	private int highscore;
	private int score;
	File file;

	public HighScore() {
		// get the highscore from the text file
		try {
			file = new File("highscore.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			// get the name and highscore
			String line = reader.readLine();
			if (line != null) {
				// break the file into two parts
				String[] parts = line.split(" ");
				champion = parts[0];
				highscore = Integer.parseInt(parts[1]);
			} else {
				champion = " ";
				highscore = 0;
			}
		} catch (FileNotFoundException f) {
			champion = " ";
			highscore = 0;
			f.printStackTrace();
		} catch (IOException e) {
			champion = " ";
			highscore = 0;
			e.printStackTrace();
		}

		// intialize the score
		score = 0;
		
		// how to play
		JOptionPane.showMessageDialog(null, "How To Play!\nDestroy the empire before they destroy you!");

		// get the player name
		name = JOptionPane.showInputDialog("Enter your name!");

		// controls
		JOptionPane.showMessageDialog(null, "Controls\n1. LEFT ARROW: Ship moves left.\n2. RIGHT ARROW: Ship moves right.\n3. SPACEBAR: Shoot.\n4. ESCAPE: Exit the game."); 
	}

	// return the highscore
	public int getHighScore() { return highscore; }
	
	// return champion name
	public String getChamp() { return champion; }

	// return the score
	public int getScore() { return score; }
	
	// add to the score
	public void addScore(int a) { score += a; }	


	// return player name
	public String getName() { return name; }

	// set the highscore
	public void setHighScore() {
		champion = name;
		highscore = score;

		// write them to the file
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(champion + " " + highscore);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
