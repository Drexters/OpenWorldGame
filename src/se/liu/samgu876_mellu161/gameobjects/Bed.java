package se.liu.samgu876_mellu161.gameobjects;

import se.liu.samgu876_mellu161.components.AABBComponent;
import se.liu.samgu876_mellu161.interactions.SimpleInteraction;
import se.liu.samgu876_mellu161.interactions.InteractionAction;
import se.liu.samgu876_mellu161.managers.DayCycleManager;
import se.liu.samgu876_mellu161.main.GameManager;

import java.awt.*;
import java.util.logging.Level;

import static se.liu.samgu876_mellu161.main.Main.LOGGER;
import static se.liu.samgu876_mellu161.utilz.Constants.SCALE;

/**
 * Represents a Bed object in the game.
 * The Bed object allows for a "sleep" interaction that advances the day cycle from NIGHT to DAWN.
 * The Bed has a solid hitbox which allows for moving the Bed, it can be set to static via interactions.
 */
public class Bed extends GameObject
{
    private final static int WIDTH = (int) (14 * SCALE);
    private final static int HEIGHT = (int) (34 * SCALE);

    public Bed(final GameManager gm, int posX, int posY) {
	super(gm, posX, posY, WIDTH, HEIGHT, ObjectID.SOLID_SQUARE, 5);
	addComponent(
		new AABBComponent(gameManager, this, true),
		"aabb"
	);
	addAction(new SimpleInteraction("Sleep"));
    }

    @Override public void update() {}

    @Override public void draw(final Graphics2D g2) {
	g2.setColor(Color.CYAN);
	g2.fillRect((int)x, (int)y, width, height);
    }

    @Override public void handleCollision(final AABBComponent otherAABB) {
    }

    @Override public void interact(final InteractionAction action) {
	switch (action.getName()) {
	    case "Sleep" -> {
		DayCycleManager dm = gameManager.getEnvironmentManager().getDayManager();
		if (dm.getDayPhase() == DayCycleManager.DayPhase.NIGHT) {
		    dm.setDayPhase(DayCycleManager.DayPhase.DAWN);
		    gameManager.getUIManager().addMessage("zZzZzZz");
		    LOGGER.log(Level.FINE, "Player slept.");
		} else {
		    gameManager.getUIManager().addMessage("You can only sleed at night");
		}
	    }
	}
    }
}
