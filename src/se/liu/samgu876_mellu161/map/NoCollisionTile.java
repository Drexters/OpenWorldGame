package se.liu.samgu876_mellu161.map;

import se.liu.samgu876_mellu161.assets.ImageAsset;

import static se.liu.samgu876_mellu161.utilz.Constants.TILE_SIZE;

/**
 * Represents a tile without collision, allowing movement through it.
 */
public class NoCollisionTile extends Tile
{
    protected NoCollisionTile(final int xIndex, final int yIndex, ImageAsset image) {
	super(image, xIndex * TILE_SIZE, yIndex * TILE_SIZE, false);
    }

    @Override public void update() {}
}
