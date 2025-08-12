package se.liu.samgu876_mellu161.main;

import se.liu.samgu876_mellu161.components.TimerComponent;

import java.awt.image.BufferedImage;

/**
 * Manages the animation of a sequence of frames.
 * Controls the progression of frames based on a specified time duration per frame.
 * The animation loops automatically once the last frame is reached.
 * <p>
 * To update the animation, call {@link #update()} once per frame to advance the current frame.
 * The current frame can be retrieved using {@link #getCurrentFrame()}.
 * <p>
 * The animation can also be copied using {@link #copy()} to create a new instance with the same frames and timing.
 */
public class Animation
{
    private BufferedImage[] frames;
    private int frameIndex = 0;
    private TimerComponent frameTime;

    public Animation(final BufferedImage[] frames, final int frameTime) {
	this.frames = frames;
	this.frameTime = new TimerComponent(frameTime);
    }

    public void update() {
	if (frames.length <= 1) return;

	frameTime.update();

	if (frameTime.isDone()) {
	    frameIndex++;
	    frameTime.restart();
	}

	if (frameIndex >= frames.length)
	    frameIndex = 0;
    }

    public BufferedImage getCurrentFrame() {
	if (frameIndex >= 0 && frameIndex < frames.length) {
	    return frames[frameIndex];
	}
	return null;
    }

    public boolean isDone() {
	return frameIndex >= frames.length - 1;
    }
}