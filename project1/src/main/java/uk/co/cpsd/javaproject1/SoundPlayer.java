package uk.co.cpsd.javaproject1;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {

    public static void playSound(String resourcePath) {
        try {
            URL soundURL = SoundPlayer.class.getResource(resourcePath);
            if (soundURL == null) {
                throw new RuntimeException("Sound file not found: " + resourcePath);
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
