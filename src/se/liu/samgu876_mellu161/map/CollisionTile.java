package se.liu.samgu876_mellu161.map;

import se.liu.samgu876_mellu161.assets.ImageAsset;

import static se.liu.samgu876_mellu161.utilz.Constants.TILE_SIZE;

/**
 * Represents a tile that blocks movement (has collision).
 */
public class CollisionTile extends Tile
{
    protected CollisionTile(final int xIndex, final int yIndex, ImageAsset image) {
	super(image, xIndex * TILE_SIZE, yIndex * TILE_SIZE, true);
    }

    @Override public void update() {
    }
}
