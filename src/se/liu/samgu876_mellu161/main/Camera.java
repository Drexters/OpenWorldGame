package se.liu.samgu876_mellu161.main;

import se.liu.samgu876_mellu161.gameobjects.GameObject;
import se.liu.samgu876_mellu161.gameobjects.ObjectID;

import java.awt.*;
import java.util.logging.Level;

import static se.liu.samgu876_mellu161.main.Main.LOGGER;
import static se.liu.samgu876_mellu161.utilz.Constants.TILE_SIZE;
import static se.liu.samgu876_mellu161.utilz.HelpFunctions.clamp;

/**
 * The Camera class controls what part of the game world is visible on the screen.
 * It follows a specific game object smoothly and makes sure the view doesn't go outside the map.
 * It can also convert positions between screen coordinates and world coordinates.
 */
public class Camera
{
    private float x, y;
    private ObjectID targetID;
    private GameObject target = null;
    private GameManager gameManager;
    private float speed;

    public Camera(final ObjectID targetID, final float speed, final GameManager gameManager) {
	this.targetID = targetID;
	this.speed = speed;
	this.gameManager = gameManager;

	x = 0;
	y = 0;
    }

    public void update() {
	if (target == null) target = gameManager.getObjectManager().getObject(targetID);	// Get a new target if no target
	if (target == null) {
	    LOGGER.log(Level.WARNING, "Camera is unable to find target.");
	    return;
	}

	// CENTER OF TARGET
	float centerX = target.getX() + target.getWidth() / 2.0f;
	float centerY = target.getY() + target.getHeight() / 2.0f;
	// CALCULATE NEW CAMERA POSITION
	x += ((centerX - gameManager.getWidth() / 2.0f) - x) * speed;
	y += ((centerY - gameManager.getHeight() / 2.0f) - y) * speed;
	// CLAMP CAMERA TO THE MAP
	x = clamp(x, 0, gameManager.getGameLevel().getWidth() * TILE_SIZE - gameManager.getWidth());
	y = clamp(y, 0, gameManager.getGameLevel().getHeight() * TILE_SIZE - gameManager.getHeight());
    }

    public void applyTranslation(Graphics2D g2) {
	g2.translate((int)-x, (int)-y);
    }

    public void removeTranslation(Graphics2D g2) {
	g2.translate((int) x, (int) y);
    }

    public Point screenToWorld(final int x, final int y) {
	return new Point((int)(x + this.x), (int)(y + this.y));
    }

    public Point worldToScreen(final int x, final int y) {
	return new Point((int)(x - this.x), (int)(y - this.y));
    }

    public float getX() {
	return x;
    }

    public float getY() {
	return y;
    }
}
