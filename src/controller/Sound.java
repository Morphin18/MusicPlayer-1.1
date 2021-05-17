package controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import view.MainFrame;

public class Sound {

    private BasicPlayer player;
    private FloatControl volumeControl;
    private Port outline;
    private static final Logger logger = Logger.getLogger(Sound.class.getName());

    public Sound(FileInputStream fis) throws JavaLayerException {

        player = new BasicPlayer();
        try {
            player.open(new BufferedInputStream(fis));
            outline = (Port) AudioSystem.getLine(Port.Info.SPEAKER);
            outline.open();
            volumeControl = (FloatControl) outline.getControl(FloatControl.Type.VOLUME);
        } catch (LineUnavailableException | BasicPlayerException ex) {
            logger.log(Level.SEVERE, null, ex);
           
        }

    }

    public int getPlayerStatus() {
        return player.getStatus();

    }

    public void play() {

        try {
            player.play();

        } catch (BasicPlayerException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

    }

    public void stop() {
        try {
            player.stop();
        } catch (BasicPlayerException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        MainFrame.setFieldDisplay("");
        MainFrame.setjSliderEnabled(false);
    }

    public void pause() {
        try {
            player.pause();
        } catch (BasicPlayerException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

    }

    public void setVolume(int vol) {
        if (AudioSystem.isLineSupported(Port.Info.SPEAKER)) {
            volumeControl.setValue((float) vol / 100);
        }
    }

    public void mute() {
        if (AudioSystem.isLineSupported(Port.Info.SPEAKER)) {
            if (volumeControl.getValue() == volumeControl.getMinimum()) {
                volumeControl.setValue(volumeControl.getMaximum());
            } else {
                volumeControl.setValue(volumeControl.getMinimum());
            }
        }
    }

    public BasicPlayer getPlayer() {
        return player;
    }

    public void resume() {
        try {
            player.resume();
        } catch (BasicPlayerException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

}
