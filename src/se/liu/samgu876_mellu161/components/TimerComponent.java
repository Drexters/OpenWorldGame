package se.liu.samgu876_mellu161.components;

import java.awt.*;

/**
 * Represents a timer component in the game.
 * The timer counts down from a specified duration and provides methods to
 * check if the timer is done, get the progress, and restart the timer.
 * <p>
 * The progress is represented as a float value between 0.0f and 1.0f, where 0.0f means the timer is at the start
 * and 1.0f means the timer is complete.
 */
public class TimerComponent extends GameComponent
{
    private int time;
    private int duration;

    public TimerComponent(final int duration) {
	super(null, null);
	this.duration = duration;
	time = 0;
    }

    public void update() {
	if (!isDone()) time--;
    }

    @Override public void render(final Graphics2D g2) {

    }

    public void restart() {
	time = duration;
    }

    public boolean isDone() {
	return time <= 0;
    }

    public float getProgress() {
	return 1.0f - ((float) time / duration);  // 0.0 to 1.0
    }

    public void setProgress(float progress) {
	time = (int) (duration * (1 - progress));
    }

    @Override public String toString() {
	return "MyTimer{" + "time=" + time + ", duration=" + duration + '}';
    }
}
