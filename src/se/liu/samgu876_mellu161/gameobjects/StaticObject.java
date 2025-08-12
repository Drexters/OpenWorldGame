package se.liu.samgu876_mellu161.gameobjects;

import se.liu.samgu876_mellu161.components.AABBComponent;
import se.liu.samgu876_mellu161.interactions.InteractionAction;
import se.liu.samgu876_mellu161.main.GameManager;

import java.awt.*;

/**
 * Represents a static, non-movable object in the game world.
 * StaticObject has a solid hitbox and zero mass, meaning it cannot be pushed or moved.
 * It is mainly used for world borders.
 */
public class StaticObject extends GameObject
{
    public StaticObject(final GameManager gameManager, final float x, final float y, final int width, final int height) {
	super(gameManager, x, y, width, height, ObjectID.STATIC_OBJ, 0);
	addComponent(
		new AABBComponent(gameManager, this, true),
		"aabb"
	);
    }

    @Override public void update() {
    }

    @Override public void draw(final Graphics2D g2) {
    }

    @Override public void handleCollision(final AABBComponent otherAABB) {
    }

    @Override public void interact(final InteractionAction action) {
    }
}