package se.liu.samgu876_mellu161.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.logging.Level;

import static se.liu.samgu876_mellu161.main.Main.LOGGER;

/**
 * Handles keyboard input for the game.
 * Tracks key states across frames to distinguish between key presses, releases, and clicks.
 * Supports queries for current key states and whether a key was just clicked.
 */
public class KeyHandler extends KeyAdapter
{
    private static final int KEY_AMOUNT = 256;
    private boolean[] keys = new boolean[KEY_AMOUNT];
    private boolean[] oldKeys = new boolean[KEY_AMOUNT];

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int index = keyEvent.getKeyCode();
        if (index > KEY_AMOUNT) {
            LOGGER.log(Level.WARNING, "Key-index out of bounds: Index " + index + " out of bounds for length " + KEY_AMOUNT);
            return;
        }
        keys[index] = true;
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int index = keyEvent.getKeyCode();
        if (index > KEY_AMOUNT) {
            LOGGER.log(Level.WARNING, "Key-index out of bounds: Index " + index + " out of bounds for length " + KEY_AMOUNT);
            return;
        }
        keys[index] = false;
    }

    public boolean isKeyDown(int keyCode){
        if (keyCode > KEY_AMOUNT) {
            LOGGER.log(Level.WARNING, "Key-index out of bounds: Index " + keyCode + " out of bounds for length " + KEY_AMOUNT);
            return false;
        }
        return keys[keyCode];
    }

    public boolean isKeyClicked(int keyCode) {
        if (keyCode > KEY_AMOUNT) {
            LOGGER.log(Level.WARNING, "Key-index out of bounds: Index " + keyCode + " out of bounds for length " + KEY_AMOUNT);
            return false;
        }
        return keys[keyCode] && !oldKeys[keyCode];
    }

    public void updateKeys(){
        oldKeys = keys.clone();
    }

    public void clearKeys() {
        Arrays.fill(keys, false);
        Arrays.fill(oldKeys, false);
    }
}
