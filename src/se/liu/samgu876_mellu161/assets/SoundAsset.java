package se.liu.samgu876_mellu161.assets;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import java.io.IOException;
import java.util.logging.Level;

import static se.liu.samgu876_mellu161.main.Main.LOGGER;
import static se.liu.samgu876_mellu161.utilz.HelpFunctions.loadSound;
import static se.liu.samgu876_mellu161.utilz.HelpFunctions.setVolumeOnClip;

/**
 * Represent all sound assets used in the game, such as sound effects and music.
 * Each enum constant holds a file path to the image and a default volume.
 * Implements the {@link Asset} interface for load and unload functionality.
 */
public enum SoundAsset implements Asset
{
    PICK_UP_ITEM("audio/pick_up_item.wav", 0.08f),
    MAIN_THEME_SONG("audio/theme_song.wav", 0.1f),
    ;
    private final String path;
    private float volume;
    private Clip clip = null;

    SoundAsset(String path, float defaultVolume) {
	this.path = path;
	this.volume = defaultVolume;
    }

    public void play() {
	if (clip == null) load();
	if (clip.isRunning()) clip.stop();
	clip.setFramePosition(0);
	clip.start();
	LOGGER.log(Level.FINE, "Play sound: " + this);
    }

    public void playLoop() {
	if (clip == null) load();
	if (clip.isRunning()) clip.stop();
	clip.loop(Clip.LOOP_CONTINUOUSLY);
	clip.start();
	LOGGER.log(Level.FINE, "Play looped sound: " + this);
    }

    public void setVolume(float volume) {
	this.volume = volume;
	if (clip != null) {
	    setVolumeOnClip(clip, volume);
	}
    }

    @Override public void load() {
	if (clip != null) {
	    return;
	}
	while(true) {
	    try {
		clip = loadSound(path);
		setVolume(volume);
		LOGGER.log(Level.INFO, "Loaded sound: " + myNameAndPath());
		return;

	    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
		LOGGER.log(Level.SEVERE, "Unable to load sound: " + myNameAndPath(), e);

		int input = JOptionPane.showConfirmDialog(
		    null,
		    "Unable to load sound: " + this + "\nWould you like to try again?",
		    "Error",
		    JOptionPane.YES_NO_OPTION,
		    JOptionPane.ERROR_MESSAGE
		);

		if (input == JOptionPane.NO_OPTION) {
		    return;
		}
	    }
	}
    }

    @Override public void unload() {
	if (clip != null) clip.close();
	clip = null;
	LOGGER.log(Level.INFO, "Unloaded sound: " + myNameAndPath());
    }

    private String myNameAndPath() {
	return this + "[" + path + "]";
    }
}
