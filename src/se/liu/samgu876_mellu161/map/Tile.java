package se.liu.samgu876_mellu161.map;

import se.liu.samgu876_mellu161.assets.ImageAsset;

import java.awt.*;

import static se.liu.samgu876_mellu161.utilz.Constants.TILE_SIZE;

/**
 * Abstract class representing a single tile in the game level.
 * Each tile has an image, position, and collision property.
 * Subclasses define specific tile behavior in the update method.
 */
public abstract class Tile
{
    protected ImageAsset image;
    protected int x, y;
    protected boolean collision;

    protected Tile(final ImageAsset image, final int x, final int y, final boolean collision) {
	this.image = image;
	this.x = x;
	this.y = y;
	this.collision = collision;
    }

    public abstract void update();

    public void draw(Graphics2D g2) {
	g2.drawImage(image.get(), x, y, TILE_SIZE, TILE_SIZE, null);
    }

    public boolean hasCollision() {
	return collision;
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }
}
