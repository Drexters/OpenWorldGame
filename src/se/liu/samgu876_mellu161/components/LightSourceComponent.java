package se.liu.samgu876_mellu161.components;

import se.liu.samgu876_mellu161.gameobjects.GameObject;
import se.liu.samgu876_mellu161.main.GameManager;

import java.awt.*;

/**
 * Represents a light source in the game.
 * The light source is associated with a {@link GameObject} and emits light
 * with a specific radius, color, and fractional gradient for light intensity.
 */
public class LightSourceComponent extends GameComponent
{
    private int x, y;
    private int radius;
    private float[] fractions;
    private Color[] colors;

    public LightSourceComponent(final GameManager gm, final int x, final int y, final int radius, final GameObject parent) {
	super(gm, parent);
	this.x = x;
	this.y = y;
	this.radius = radius;
	initLightAppearance();
    }

    private void initLightAppearance() {
	final float beginFraction = 0.0f;
	final float middleFraction = 0.7f;
	final float endFraction = 1.0f;
	fractions = new float[]{beginFraction, middleFraction, endFraction}; // For pixel perfect light,
	colors = new Color[] {
		new Color(0, 0, 0, 200),
		new Color(0, 0, 0, 100),
		new Color(0, 0, 0, 0)
	};
    }

    @Override public void update() {
	this.x = (int)parent.getCenterX();
	this.y = (int)parent.getCenterY();
	gameManager.getEnvironmentManager().registerLightSource(this);
    }

    @Override public void render(final Graphics2D g2) {

    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public int getRadius() {
	return radius;
    }

    public int getDiameter() {
	return radius * 2;
    }

    public float[] getFractions() {
	return fractions;
    }

    public Color[] getColors() {
	return colors;
    }

    public Rectangle getVisibleZone(Rectangle zone) {
	float diameter = getDiameter();

	if (diameter > zone.width) {
	    zone.width = (int) diameter;
	    zone.x = x - radius;
	}
	if (diameter > zone.height) {
	    zone.height = (int) diameter;
	    zone.y = y - radius;
	}
    	return zone;
    }
}