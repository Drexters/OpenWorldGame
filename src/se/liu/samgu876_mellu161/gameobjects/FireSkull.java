package se.liu.samgu876_mellu161.gameobjects;

import se.liu.samgu876_mellu161.assets.ImageAsset;
import se.liu.samgu876_mellu161.components.AABBComponent;
import se.liu.samgu876_mellu161.components.LightSourceComponent;
import se.liu.samgu876_mellu161.interactions.InteractionAction;
import se.liu.samgu876_mellu161.components.LightEmitter;
import se.liu.samgu876_mellu161.main.Direction;
import se.liu.samgu876_mellu161.main.GameManager;

import java.awt.*;
import java.awt.image.BufferedImage;

import static se.liu.samgu876_mellu161.utilz.Constants.RND;
import static se.liu.samgu876_mellu161.utilz.Constants.SCALE;
import static se.liu.samgu876_mellu161.utilz.HelpFunctions.secToUpdates;

/**
 * Represents a FireSkull object in the game.
 * The FireSkull object moves randomly and has a walking- and dead- state.
 * It has a light source, an attack zone, and a hitbox for realistic interaction.
 * The FireSkull can collide with other objects and change its state to DEAD upon collision with an AngryPlant.
 */
public class FireSkull extends GameObject implements LightEmitter
{
    private final static int WIDTH = (int) (12 * SCALE);
    private final static int HEIGHT = (int) (12 * SCALE);
    private final static float DEFAULT_SPEED = 2.1f;
    private final static int WALK_DURATION_RND = secToUpdates(2);
    private final static int WALK_DURATION_MIN = secToUpdates(1);
    private enum State {WALK, DEAD}
    private State state = State.WALK;
    private Direction direction = Direction.S;
    private int moveTimer;

    private AABBComponent aliveAABB;
    private AABBComponent deadAABB;

    public FireSkull(final GameManager gm, final float x, final float y) {
	super(gm, x, y,WIDTH, HEIGHT, ObjectID.FIRE_SKULL, 1);
        addAnimation();
        addComponents();
        addHitbox();
    }

    private void addHitbox() {
        // There is alot of magic numbers here but all of them are unique and are used for pixel perfect hitboxes.
        // Therefor we decided not to make them variables or constants.

        aliveAABB = new AABBComponent(gameManager, this, true);
        int offX = width / 4;
        int offY = height / 4;
        aliveAABB.setOffset(offX, offY);
        aliveAABB.getHitbox().width = width - 2 * offX;
        aliveAABB.getHitbox().height = height - 2 * offY;

        deadAABB = new AABBComponent(gameManager, this, true);
        deadAABB.setOffset(width/3, height/2);
        deadAABB.getHitbox().width = width / 3.0f;
        deadAABB.getHitbox().height = height / 3.0f;

        addComponent(aliveAABB, "aabb");
    }

    private void addAnimation() {
        // Lot of magic numbers here as well and they are used to point to a specific image in a sprite sheet.
        // All of the numbers represents unique images and is only used here to initialize the animations.

        BufferedImage spriteSheet = ImageAsset.FIRE_SKULL.get();
        final int w = spriteSheet.getWidth() / 4; // Original frame width (total sprite width / amount of frames on x-axis)
        final int h = spriteSheet.getHeight() / 10; // Original frame height (total sprite height / amount of frames on y-axis)
        float scale = (float) WIDTH / w; // How much to scale the images to match the width and height
        final int animationSpeed = 10;

        addAnimation("WALK E", spriteSheet, scale, w, h, animationSpeed, 0,0, 1,0, 2,0, 3,0);
        addAnimation("WALK W", spriteSheet, scale, w, h, animationSpeed, 0,1, 1,1, 2,1, 3,1);
        addAnimation("WALK S", spriteSheet, scale, w, h, animationSpeed, 0,2, 1,2, 2,2, 3,2);
        addAnimation("WALK N", spriteSheet, scale, w, h, animationSpeed, 0,3, 1,3, 2,3, 3,3);

        addAnimation("DIE E", spriteSheet, scale, w, h, animationSpeed, 0,8, 1,8, 2,8, 3,8);
        addAnimation("DIE W", spriteSheet, scale, w, h, animationSpeed, 0,9, 1,9, 2,9, 3,9);

        addAnimation("DIE E", spriteSheet, scale, w, h, animationSpeed, 3,8);
        addAnimation("DIE W", spriteSheet, scale, w, h, animationSpeed, 3,9);

        animator.trigger("WALK S");
    }

    private void addComponents() {
        addComponent(
                new LightSourceComponent(gameManager, (int)x, (int)y, (int)(width * 2/3.0f), this),
                "light source"
        );
    }


    @Override public void update() {
        if (state == State.DEAD) return; // no movement if dead

        // UPDATE MOVEMENT
        if (moveTimer <= 0) {
            final int availableDirections = 5;
            direction = Direction.values()[RND.nextInt(availableDirections)]; // random N, E, S, W, NE
            moveTimer = RND.nextInt(WALK_DURATION_RND) + WALK_DURATION_MIN;
            animator.trigger("WALK " + direction.name());
        }
        // går baserat på riktning
        switch (direction) {
            case N -> y -= DEFAULT_SPEED;
            case S -> y += DEFAULT_SPEED;
            case E -> x += DEFAULT_SPEED;
            case W -> x -= DEFAULT_SPEED;
            case NE ->x += 0;
        }

        moveTimer--;

        // UPDATE ANIMATOR
        animator.update();
    }

    @Override public void draw(final Graphics2D g2) {
        animator.render(g2, x, y);
    }

    @Override public void handleCollision(final AABBComponent otherAABB) {
        GameObject other = otherAABB.getParent();

        if (other.getID() != ObjectID.ANGRY_PLANT) return;

        if (state != State.DEAD) {
            animator.trigger("DIE E");
            animator.setLocked(true);
            animator.trigger("DEAD E");
            state = State.DEAD;
            mass = 0;
            removeComponent("aabb");
            addComponent(deadAABB, "aabb");
        }
    }

    @Override public void interact(final InteractionAction action) {
    }

    @Override public LightSourceComponent getLightSource() {
	return (LightSourceComponent) getComponent("light source");
    }

    @Override public Rectangle getVisibleZone() {
        // Resize the zone if light area is larger than objects normal visible zone
        return getLightSource().getVisibleZone(super.getVisibleZone());
    }
}
