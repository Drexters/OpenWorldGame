package se.liu.samgu876_mellu161.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;

import static se.liu.samgu876_mellu161.main.Main.LOGGER;

/**
 * Handles mouse input for the game.
 * Tracks mouse button states, position, and supports detection of button clicks.
 */
public class MouseHandler extends MouseAdapter
{
    private final static int BUTTON_AMOUNT = 5;
    private boolean[] buttons = new boolean[BUTTON_AMOUNT];
    private boolean[] oldButtons = new boolean[BUTTON_AMOUNT];
    private int mouseX = 0;
    private int mouseY = 0;

    @Override public void mousePressed(final MouseEvent mouseEvent) {
        int index = mouseEvent.getButton();
        if (index > BUTTON_AMOUNT) {
            LOGGER.log(Level.WARNING, "Button-index out of bounds: Index " + index + " out of bounds for length " + BUTTON_AMOUNT);
            return;
        }
        buttons[mouseEvent.getButton()] = true;
    }

    @Override public void mouseReleased(final MouseEvent mouseEvent) {
        int index = mouseEvent.getButton();
        if (index > BUTTON_AMOUNT) {
            LOGGER.log(Level.WARNING, "Button-index out of bounds: Index " + index + " out of bounds for length " + BUTTON_AMOUNT);
            return;
        }
        buttons[mouseEvent.getButton()] = false;
    }


    @Override public void mouseDragged(final MouseEvent mouseEvent) {
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();
    }

    @Override public void mouseMoved(final MouseEvent mouseEvent) {
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();
    }

    public boolean isButtonDown(int button) {
        if (button > BUTTON_AMOUNT) {
            LOGGER.log(Level.WARNING, "Button-index out of bounds: Index " + button + " out of bounds for length " + BUTTON_AMOUNT);
            return false;
        }
        return buttons[button];
    }

    public boolean isButtonClicked(int button) {
        if (button > BUTTON_AMOUNT) {
            LOGGER.log(Level.WARNING, "Button-index out of bounds: Index " + button + " out of bounds for length " + BUTTON_AMOUNT);
            return false;
        }
        return buttons[button] && ! oldButtons[button];
    }

    public void updateMouse() {
        oldButtons = buttons.clone();
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }
}
