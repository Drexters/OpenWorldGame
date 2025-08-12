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
 * Represents a tree object in the game world.
 * The Tree features a solid hitbox and supports a "Cut down" interaction,
 * which removes the object and grants the player wood.
 */
public class Tree extends GameObject
{
    private final static int WIDTH = (int) (44 * SCALE);
    private final static int HEIGHT = (int) (59 * SCALE);

    public Tree(final GameManager gameManager, final float x, final float y) {
	super(gameManager, x, y, WIDTH, HEIGHT, ObjectID.TREE, 0);
	addAnimation();
	addHitbox();
	addActions();
    }

    private void addActions() {
	addAction(new SimpleInteraction("Cut down"));
    }

    private void addHitbox() {
	AABBComponent aabb = new AABBComponent(gameManager, this, true);
	aabb.setOffset((int)(width/3.17), (int)(height/1.36));
	aabb.getHitbox().width = (float) (width/2.22);
	aabb.getHitbox().height = (float) (height/6.0);
	addComponent(aabb, "aabb");
    }

    private void addAnimation() {
	BufferedImage spriteSheet = ImageAsset.TREE.get();
	final int w = spriteSheet.getWidth(); // Original frame width
	final int h = spriteSheet.getHeight(); // Original frame height
	final float scale = (float) WIDTH / w; // How much to scale the images to match the width and height
	final int animationSpeed = 0;

	addAnimation("tree", spriteSheet, scale, w, h, animationSpeed, 0, 0);
	animator.trigger("tree");
    }

    @Override public void update() {
    }

    @Override public void draw(final Graphics2D g2) {
	animator.render(g2, x, y);

	if (gameManager.isDebugging()) {
	    getComponent("aabb").render(g2);
	    g2.setColor(Color.WHITE);
	    g2.drawRect((int)x, (int)y, width, height);
	}
    }

    @Override public void handleCollision(final AABBComponent otherAABB) {

    }

    @Override public void interact(final InteractionAction action) {
	switch (action.getName()) {
	    case "Cut down" -> {
		gameManager.getUIManager().addMessage("+1 Wood");
		removeMe();
	    }
	}
    }
}
