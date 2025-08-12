package se.liu.samgu876_mellu161.gameobjects;

import se.liu.samgu876_mellu161.assets.SoundAsset;
import se.liu.samgu876_mellu161.components.AABBComponent;
import se.liu.samgu876_mellu161.components.LightSourceComponent;
import se.liu.samgu876_mellu161.interactions.InteractionAction;
import se.liu.samgu876_mellu161.components.LightEmitter;
import se.liu.samgu876_mellu161.main.GameManager;

import java.awt.*;

import static se.liu.samgu876_mellu161.utilz.Constants.SCALE;

/**
 * Represents a pick-up item in the game.
 * The pick-up item is a consumable object that can be collected by the player.
 * It has a light source to make it more visible and can be picked up upon collision with the player.
 * When collected, the item is removed from the game, and a sound effect is played.
 */
public class PickUpItem extends GameObject implements LightEmitter
{
    private final static int WIDTH = (int) (7 * SCALE);
    private final static int HEIGHT = (int) (7 * SCALE);

    public PickUpItem(final GameManager gm, final int posX, final int posY) {
	super(gm, posX, posY, WIDTH, HEIGHT, ObjectID.CONSUMABLE, 0);
	addComponent(
		new AABBComponent(gameManager, this, false),
		"aabb"
	);
	addComponent(
		new LightSourceComponent(gameManager, (int)getCenterX(), (int)getCenterY(), 70, this),
		"light source"
	);
    }

    @Override public void update() {}

    @Override public void draw(final Graphics2D g2) {
	g2.setColor(new Color(255, 255, 0, 200));
	g2.fillRect((int)x, (int)y , width, height);

	if (gameManager.isDebugging())
	    getComponent("aabb").render(g2);
    }

    @Override public void handleCollision(final AABBComponent otherAABB) {
	GameObject other = otherAABB.getParent();
	switch (other.getID()) {
	    case PLAYER -> {
		removeMe();
		SoundAsset.PICK_UP_ITEM.play();
		gameManager.getUIManager().getMessageManager().addMessage("Picked up item");
	    }
	}
    }

    @Override public void interact(final InteractionAction action) {}

    @Override public LightSourceComponent getLightSource() {
	return (LightSourceComponent) getComponent("light source");
    }

    @Override public Rectangle getVisibleZone() {
	// Resize the zone if light area is larger than objects normal visible zone
	return getLightSource().getVisibleZone(super.getVisibleZone());
    }
}
