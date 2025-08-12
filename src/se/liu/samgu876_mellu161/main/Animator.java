package se.liu.samgu876_mellu161.main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Animator is responsible for managing and switching between different animations.
 * It supports locking the current animation and queuing the next one for smoother transitions.
 */
public class Animator
{
    private Map<String, Animation> animations = new HashMap<>();
    private Animation currentAnimation = null;
    private Animation nextAnimation = null;
    private boolean locked = false;

    public Animator() {}

    public void addAnimation(String trigger, Animation animation) {
	animations.put(trigger, animation);
    }

    public void trigger(String trigger) {
	if (!animations.containsKey(trigger)) return;

	Animation requestedAnimation = animations.get(trigger);
	if (currentAnimation == null) {
	    currentAnimation = requestedAnimation;

	} else if (locked) {
	    nextAnimation = requestedAnimation;

	} else if (!currentAnimation.equals(requestedAnimation)) {
		currentAnimation = requestedAnimation;
	}
    }

    public void update() {
	if (currentAnimation == null) return;

	currentAnimation.update();

	if (locked && currentAnimation.isDone()) {
	    locked = false;
	    if (nextAnimation != null) {
		currentAnimation = nextAnimation;
		nextAnimation = null;
	    }
	}
    }

    public void setLocked(boolean locked) {
	this.locked = locked;
    }

    public BufferedImage getCurrentFrame() {
	return currentAnimation != null ? currentAnimation.getCurrentFrame() : null;
    }

    public void render(final Graphics2D g2, final float x, final float y) {
	if (getCurrentFrame() != null) {
	    g2.drawImage(getCurrentFrame(), (int) x, (int) y, null);
	}
    }
}