package se.liu.samgu876_mellu161.gameobjects;

import se.liu.samgu876_mellu161.components.AABBComponent;
import se.liu.samgu876_mellu161.components.IGameComponent;
import se.liu.samgu876_mellu161.interactions.InteractionAction;
import se.liu.samgu876_mellu161.main.Animation;
import se.liu.samgu876_mellu161.main.Animator;
import se.liu.samgu876_mellu161.main.GameManager;
import se.liu.samgu876_mellu161.components.TimerComponent;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static se.liu.samgu876_mellu161.utilz.HelpFunctions.getImagesFromSpriteSheet;


/**
 * Base class for objects in the game world.
 * Provides functionality for managing position, size, components, and interactions with other objects.
 * Supports movement, collision detection, and updating/rendering behavior.
 * Each GameObject can have components like hitboxes, timers and animations.
 */
public abstract class GameObject implements IGameObject
{
    protected float x, y;
    protected int width, height;
    protected ObjectID id;
    protected float mass; // 0 = static (not movable)

    protected boolean remove = false;
    protected GameManager gameManager;
    protected Map<String, IGameComponent> components = new HashMap<>();
    protected List<InteractionAction> interactions = new ArrayList<>();
    protected Animator animator = new Animator();

    protected GameObject(final GameManager gameManager, float x, float y, int width, int height, ObjectID id, float mass) {
	this.gameManager = gameManager;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.id = id;
        this.mass = mass;
    }

    public void addAction(InteractionAction interaction) {
        interactions.add(interaction);
    }

    public void addComponent(IGameComponent component, String id) {
        components.put(id, component);
    }

    public void removeComponent(String id) {
        components.remove(id);
    }

    public IGameComponent getComponent(String id) {
        return components.get(id);
    }

    public void updateComponents() {
        for (IGameComponent component : components.values())
            component.update();
    }

    public void renderComponents(Graphics2D g2) {
        for (IGameComponent component : components.values())
            component.render(g2);
    }

    protected void resetTimer(String id) {
        TimerComponent timer = (TimerComponent) components.get(id);
        timer.restart();
    }

    protected boolean isTimerDone(String id) {
        TimerComponent timer = (TimerComponent) components.get(id);
        return timer.isDone();
    }

    protected void printTimer(String id) {
        // --- Used for debugging
        System.out.println(id + ": " + components.get(id));
    }

    protected void removeMe() {
        gameManager.getObjectManager().removeObject(this);
    }

    public void updatePosAccordingToHitbox(final AABBComponent aabb) {
        x = aabb.getHitbox().x - aabb.getOffset().x;
        y = aabb.getHitbox().y - aabb.getOffset().y;
    }

    public boolean containsPoint(int x, int y) {
        return x > this.x && x < this.x + width &&
               y > this.y && y < this.y + height;
    }

    public void drawDebug(Graphics2D g2) {
        renderComponents(g2);

        // Draw the outline of the GameObject
        g2.setColor(Color.WHITE);
        final int offset = 1;
        g2.drawRect((int)x - offset, (int)y - offset, width + 2 * offset, height + 2 * offset);
    }

    protected void addAnimation(String trigger, BufferedImage spriteSheet, float scale, int w, int h, int speed, int... xyCoords) {
        List<Point> frames = new ArrayList<>();
        final int step = 2; // because the xyCoords are as followed: x0,y0, x1,y1, x2,y2, ...
        for (int i = 0; i < xyCoords.length; i += step) {
            frames.add(new Point(xyCoords[i], xyCoords[i + 1]));
        }

        Animation animation = new Animation(
                getImagesFromSpriteSheet(spriteSheet, w, h, scale, frames),
                speed
        );
        animator.addAnimation(trigger, animation);
    }

    public float getCenterX() {
        return x + width/2.0f;
    }

    public float getCenterY() {
        return y + height/2.0f;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public ObjectID getID() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean shouldRemove() {
        return remove;
    }

    public float getMass() {
        return mass;
    }

    public List<InteractionAction> getInteractions() {
        return interactions;
    }

    public void setMass(final float mass) {
        this.mass = mass;
    }

    public boolean isStatic() {
        return mass == 0;
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    public Rectangle getVisibleZone() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}
