package se.liu.samgu876_mellu161.gameobjects;

import se.liu.samgu876_mellu161.assets.ImageAsset;
import se.liu.samgu876_mellu161.components.AABBComponent;
import se.liu.samgu876_mellu161.interactions.SimpleInteraction;
import se.liu.samgu876_mellu161.interactions.InteractionAction;
import se.liu.samgu876_mellu161.main.GameManager;

import java.awt.*;
import java.awt.image.BufferedImage;

import static se.liu.samgu876_mellu161.utilz.Constants.SCALE;

/**
 * Represents a mineable stone object in the game world.
 * The Stone has a solid hitbox and supports a "Mine" interaction, which removes
 * the object from the game and rewards the player with stone.
 */
public class Stone extends GameObject
{
    private final static int WIDTH = (int) (44 * SCALE);
    private final static int HEIGHT = (int) (25 * SCALE);

    public Stone(final GameManager gameManager, final float x, final float y) {
	super(gameManager, x, y, WIDTH, HEIGHT, ObjectID.STONE, 0);
	addAnimations();
	addHitbox();
	addActions();
    }

    private void addActions() {
	addAction(new SimpleInteraction("Mine"));
    }

    private void addHitbox() {
	AABBComponent aabb = new AABBComponent(gameManager, this, true);
	aabb.setOffset((int)(width/6.7), (int)(height/2.2)); // These magic numbers are for pixel perfect hitboxes
	aabb.getHitbox().width = (float) (width/1.3);
	aabb.getHitbox().height = (float) (height/3.0);
	addComponent(aabb, "aabb");
    }

    private void addAnimations() {
	BufferedImage spriteSheet = ImageAsset.STONE.get();
	final int w = spriteSheet.getWidth(); // Original frame width
	final int h = spriteSheet.getHeight(); // Original frame height
	final float scale = (float) WIDTH / w; // How much to scale the images to match the width and height
	final int animationSpeed = 0;

	addAnimation("stone", spriteSheet, scale, w, h, animationSpeed, 0, 0);
	animator.trigger("stone");
    }

    @Override public void update() {}

    @Override public void draw(final Graphics2D g2) {
	animator.render(g2, x, y);

	if (gameManager.isDebugging()) {
	    getComponent("aabb").render(g2);
	    g2.setColor(Color.WHITE);
	    g2.drawRect((int)x, (int)y, width, height);
	}
    }

    @Override public void handleCollision(final AABBComponent otherAABB) {}

    @Override public void interact(final InteractionAction action) {
	switch (action.getName()) {
	    case "Mine" -> {
		gameManager.getUIManager().addMessage("+1 Stone");
		removeMe();
	    }
	}
    }
}
