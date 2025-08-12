package se.liu.samgu876_mellu161.gameobjects;

import se.liu.samgu876_mellu161.assets.ImageAsset;
import se.liu.samgu876_mellu161.components.AABBComponent;
import se.liu.samgu876_mellu161.components.LightSourceComponent;
import se.liu.samgu876_mellu161.input.KeyHandler;
import se.liu.samgu876_mellu161.interactions.InteractionAction;
import se.liu.samgu876_mellu161.components.LightEmitter;
import se.liu.samgu876_mellu161.main.Direction;
import se.liu.samgu876_mellu161.main.GameManager;
import se.liu.samgu876_mellu161.components.TimerComponent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import static se.liu.samgu876_mellu161.utilz.Constants.SCALE;
import static se.liu.samgu876_mellu161.utilz.HelpFunctions.secToUpdates;

/**
 * Represents the Player object in the game.
 * The Player can move in four directions using keyboard input and supports both walking and running.
 * The Player has animated states such as idle, walk, run, and push, and interacts with solid objects by pushing them.
 * A light source component surrounds the player for visibility, and a timer-based action allows the player to "say hi."
 */
public class Player extends GameObject implements LightEmitter
{
    private final static int WIDTH = (int) (43 * SCALE);
    private final static int HEIGHT = (int) (43 * SCALE);
    /**
     * How often the player can say hi.
     */
    private final static int SAY_HI_TIMER_DURATION = secToUpdates(5);
    private final static int WALK_SPEED = 4;
    private final static int RUN_SPEED = 6;
    private enum State {IDLE, WALK, PUSH, RUN}
    private KeyHandler keyHandler;
    private Direction direction;
    private boolean pushing;

    public Player(GameManager gm, float startX, float startY) {
        super(gm, startX, startY, WIDTH, HEIGHT, ObjectID.PLAYER, 1);
        this.keyHandler = (KeyHandler) gm.getKeyListeners()[0];
        addComponents();
        addAnimation();
        addHitbox();
        direction = Direction.S;
    }

    private void addComponents() {
        addComponent(
                new LightSourceComponent(gameManager, (int)x, (int)y, 150, this),
                "light source"
        );
        addComponent(
                new TimerComponent(SAY_HI_TIMER_DURATION),
                "hi"
        );
    }

    private void addAnimation() {
        // Lot of magic numbers here, they are used to point to a specific image in a sprite sheet.
        // All of the numbers represents unique images and is only used here to initialize the animations.

        BufferedImage spriteSheet = ImageAsset.CHARACTER_BASIC.get();
        final int w = 64; // Original frame width
        final int h = 64; // Original frame height
        float scale = (float) WIDTH / w; // How much to scale the images to match the width and height

        int animationSpeed = 0;
        addAnimation("IDLE S", spriteSheet, scale, w, h, animationSpeed, 0, 0);
        addAnimation("IDLE N", spriteSheet, scale, w, h, animationSpeed, 0, 1);
        addAnimation("IDLE E", spriteSheet, scale, w, h, animationSpeed, 0, 2);
        addAnimation("IDLE W", spriteSheet, scale, w, h, animationSpeed, 0, 3);

        animationSpeed = 5;
        addAnimation("WALK S", spriteSheet, scale, w, h, animationSpeed, 0, 4, 1, 4, 2, 4, 3, 4, 4, 4, 5, 4);
        addAnimation("WALK N", spriteSheet, scale, w, h, animationSpeed, 0, 5, 1, 5, 2, 5, 3, 5, 4, 5, 5, 5);
        addAnimation("WALK E", spriteSheet, scale, w, h, animationSpeed, 0, 6, 1, 6, 2, 6, 3, 6, 4, 6, 5, 6);
        addAnimation("WALK W", spriteSheet, scale, w, h, animationSpeed, 0, 7, 1, 7, 2, 7, 3, 7, 4, 7, 5, 7);

        animationSpeed = 3;
        addAnimation("RUN S", spriteSheet, scale, w, h, animationSpeed, 0, 4, 1, 4, 6, 4, 3, 4, 4, 4, 7, 4);
        addAnimation("RUN N", spriteSheet, scale, w, h, animationSpeed, 0, 5, 1, 5, 6, 5, 3, 5, 4, 5, 7, 5);
        addAnimation("RUN E", spriteSheet, scale, w, h, animationSpeed, 0, 6, 1, 6, 6, 6, 3, 6, 4, 6, 7, 6);
        addAnimation("RUN W", spriteSheet, scale, w, h, animationSpeed, 0, 7, 1, 7, 6, 7, 3, 7, 4, 7, 7, 7);

        animationSpeed = 10;
        addAnimation("PUSH S", spriteSheet, scale, w, h, animationSpeed, 1, 0, 2, 0);
        addAnimation("PUSH N", spriteSheet, scale, w, h, animationSpeed, 1, 1, 2, 1);
        addAnimation("PUSH E", spriteSheet, scale, w, h, animationSpeed, 1, 2, 2, 2);
        addAnimation("PUSH W", spriteSheet, scale, w, h, animationSpeed, 1, 3, 2, 3);

        // Don't need the sprite sheet any more
        ImageAsset.CHARACTER_BASIC.unload();

        // Trigger the start animation
        animator.trigger("IDLE S");
    }

    private void addHitbox() {
        // Lots of magic numbers here. They are used for pixel perfect hitboxes and are never used after these initializations.
        AABBComponent aabb = new AABBComponent(gameManager, this, true);
        aabb.setOffset((int)(width*0.35), (int)(height*0.47)); // These magic numbers are for pixel perfect hitboxes
        aabb.getHitbox().width = width * 0.31f;
        aabb.getHitbox().height = height * 0.20f;
        addComponent(aabb, "aabb");
    }

    @Override public void update() {

        // DECLARE CURRENT SPEED
        final boolean running = keyHandler.isKeyDown(KeyEvent.VK_SHIFT);
        int speed = running ? RUN_SPEED : WALK_SPEED;

        // MOVEMENT
        float dx = 0; // Velocity x
        float dy = 0; // Velocity y

        boolean moveUp = keyHandler.isKeyDown(KeyEvent.VK_W);
        boolean moveDown = keyHandler.isKeyDown(KeyEvent.VK_S);
        boolean moveRight = keyHandler.isKeyDown(KeyEvent.VK_D);
        boolean moveLeft = keyHandler.isKeyDown(KeyEvent.VK_A);


        if (moveUp && !moveDown) {
            dy -= speed;
            direction = Direction.N;
        } else if (moveDown && !moveUp) {
            dy += speed;
            direction = Direction.S;
        }

        if (moveLeft && !moveRight) {
            dx -= speed;
            direction = Direction.W;
        } else if (moveRight && !moveLeft) {
            dx += speed;
            direction = Direction.E;
        }

        x += dx;
        y += dy;

        // UPDATE STATE
        State state;
        if (pushing) {
            state = State.PUSH;
        } else if (dx == 0 && dy == 0) {
            state = State.IDLE;
        }else if (running){
            state = State.RUN;
        } else {
            state = State.WALK;
        }

        // TRIGGER ANIMATION
        animator.trigger(state + " " + direction);
        animator.update();

        // OTHER
        if (isTimerDone("hi")) {
            if (keyHandler.isKeyClicked(KeyEvent.VK_H)) {
                gameManager.getUIManager().addMessage("Player says hi!");
                resetTimer("hi");
            }
        }

        pushing = false;
    }

    @Override public void draw(final Graphics2D g2) {
        animator.render(g2, x, y);
    }

    @Override public void handleCollision(final AABBComponent otherAABB) {
        if (otherAABB.isSolid())
            pushing = true;
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
