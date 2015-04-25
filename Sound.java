/* Sound.java
* Author: Logan Wholey
* Date: 12.01.2013
*/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.sound.sampled.*;
import java.io.*;

class Sound {
        private Clip clip;
        private AudioInputStream is;
        private long last_play = 0;

        // create the sound
        public Sound(String name) {
                try {
                        clip = AudioSystem.getClip( );
                        is = AudioSystem.getAudioInputStream(new File(name));
                        clip.open(is);
                } catch(Exception e) {
                }
        }

        // play a sound
        public void play( ) {
                long now = new Date( ).getTime( );
                if((now - last_play) < 250) {
                        return;
                }
                try {
                        clip.stop( );
                        clip.setFramePosition(0);
                        clip.start( );
                        last_play = now;
                } catch(Exception e) {
                }
        }

        // play the game music
        public void gameMusic() {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
}