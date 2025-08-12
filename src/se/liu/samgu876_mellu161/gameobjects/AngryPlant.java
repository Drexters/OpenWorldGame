package se.liu.samgu876_mellu161.gameobjects;

import se.liu.samgu876_mellu161.assets.ImageAsset;
import se.liu.samgu876_mellu161.components.AABBComponent;
import se.liu.samgu876_mellu161.interactions.InteractionAction;
import se.liu.samgu876_mellu161.main.GameManager;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static se.liu.samgu876_mellu161.utilz.Constants.SCALE;

/**
 * Represents an AngryPlant object in the game.
 * The AngryPlant object has an attack and idle animation, a trigger zone / attack zone,
 * and a hitbox for collision. The plant changes its animation state based on interactions
 * and collisions.
 */
public class AngryPlant extends GameObject
{
    private final static int WIDTH = (int) (64 * SCALE);
    private final static int HEIGHT = (int) (64 * SCALE);

    public AngryPlant(final GameManager gm, final float x, final float y) {
	super(gm, x, y, WIDTH, HEIGHT, ObjectID.ANGRY_PLANT, 0);
	addAnimations();
	addHitbox();
    }

    private void addHitbox() {
	// Lots of magic numbers here. They are used for pixel perfect hitboxes and are never used after these initializations.
	AABBComponent triggerZone = new AABBComponent(gameManager, this, false);
	triggerZone.setOffset((int)(width * 0.21), (int)(height * 0.21)); // width * x = 40, height * y = 40
	triggerZone.getHitbox().width = (int)(width * 0.58);
	triggerZone.getHitbox().height = (int)(height * 0.63);
	addComponent(triggerZone, "trigger zone");

	AABBComponent aabb = new AABBComponent(gameManager, this, true);
	aabb.setOffset((int)(width * 0.42), (int)(height * 0.57));
	aabb.getHitbox().width = (int)(width * 0.16);
	aabb.getHitbox().height = (int)(height * 0.04);
	addComponent(aabb, "aabb");
    }

    private void addAnimations() {
	BufferedImage spriteSheet = ImageAsset.ANGRY_PLANT.get();
	final int framesAmountX = 14;
	final int framesAmountY = 5;
	final int w = spriteSheet.getWidth() / framesAmountX; // Original frame width
	final int h = spriteSheet.getHeight() / framesAmountY; // Original frame height
	float scale = (float) WIDTH / w; // How much to scale the images to match the width and height

	final int animationSpeed = 4;
	addAnimation("attack", spriteSheet, scale, w, h, animationSpeed,
		     0, 0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0, 7, 0, 8, 0, 9, 0, 10, 0
	);
	addAnimation("idle", spriteSheet, scale, w, h, animationSpeed, 0, 0);

	animator.trigger("idle");
    }

    @Override public void update() {
	animator.update();
    }

    @Override public void draw(final Graphics2D g2) {
	animator.render(g2, x, y);

	if (gameManager.isDebugging()) {
	    AABBComponent triggerZone = (AABBComponent) getComponent("trigger zone");
	    Rectangle2D.Float zone = triggerZone.getHitbox();
	    g2.setColor(Color.ORANGE);
	    g2.drawRect((int) zone.x, (int) zone.y, (int) zone.width, (int) zone.height);
	}
    }

    @Override public void handleCollision(final AABBComponent otherAABB) {
	GameObject other = otherAABB.getParent();
	if (other.isStatic()) return; // Dont react to static objects

	animator.trigger("attack");
	animator.setLocked(true);
	animator.trigger("idle"); // Trigger this animation after the attack animation is done
    }

    @Override public void interact(final InteractionAction action) {
    }
}
