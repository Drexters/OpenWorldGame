package se.liu.samgu876_mellu161.managers;

import se.liu.samgu876_mellu161.components.LightSourceComponent;
import se.liu.samgu876_mellu161.main.GameManager;
import se.liu.samgu876_mellu161.utilz.PerformanceTimer;

import java.awt.*;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;

import static se.liu.samgu876_mellu161.utilz.Constants.PANEL_HEIGHT;
import static se.liu.samgu876_mellu161.utilz.Constants.PANEL_WIDTH;

/**
 * Manages the game's environment, specifically dynamic lighting and the day-night cycle.
 *
 * <p>This includes:
 * <ul>
 *   <li>Rendering a light mask over the game world using {@link VolatileImage} for GPU-accelerated performance.</li>
 *   <li>Registering and rendering light sources that lights up the environment.</li>
 *   <li>Applying color tints to simulate time-of-day effects via {@link DayCycleManager}.</li>
 * </ul>
 *
 * <p>Light sources must register through {@link #registerLightSource(LightSourceComponent)}.
 */
public class EnvironmentManager
{
    private List<LightSourceComponent> lightSources = new ArrayList<>();
    private GameManager gameManager;

    /**
     * Instead of BufferedImage (which is CPU-intensive and caused lag), we use VolatileImage.
     * VolatileImage uses GPU acceleration, significantly improving performance.
     * This works best on systems with a dedicated GPU and when not in battery-saving mode.
     *  It's optimized for desktop PCs, but performance may still vary on laptops.
     */
    private VolatileImage lightMap = null;

    private DayCycleManager dayManager;
    private PerformanceTimer pTimer = new PerformanceTimer("light");

    public EnvironmentManager(final int dayDuration, final GameManager gm) {
	this.gameManager = gm;
	dayManager = new DayCycleManager(dayDuration);
    }

    public void registerLightSource(final LightSourceComponent light) {
	lightSources.add(light);
    }

    public void update() {
	dayManager.update();
    }

    public void draw(Graphics2D g2) {
	if (gameManager.isDebugging()) pTimer.start();
	if (dayManager.getDayPhase() == DayCycleManager.DayPhase.DAY) { // No light filter if its DAY
	    lightSources.clear();
	    return;
	}
	int camX = (int) gameManager.getCamera().getX();
	int camY = (int) gameManager.getCamera().getY();
	int width = PANEL_WIDTH;
	int height = PANEL_HEIGHT;

	GraphicsConfiguration gc = getGpuConfigurations();

	// Check if the lightMap need to be changed
	if (lightMap == null || lightMap.getWidth() != width || lightMap.getHeight() != height ||
	    lightMap.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
	    lightMap = gc.createCompatibleVolatileImage(width, height, Transparency.TRANSLUCENT);
	}
	Graphics2D lightG = lightMap.createGraphics();

	// Draw the darkness filter
	lightG.setComposite(AlphaComposite.Src);
	lightG.setColor(dayManager.getTint());
	lightG.fillRect(0, 0, lightMap.getWidth(), lightMap.getHeight());

	// Make the light sources "add up" on each other
	lightG.setComposite(AlphaComposite.DstOut);

	// Make the light effect around lightsources
	for (LightSourceComponent light : lightSources) {
	    if (!isLightOnScreen(light, camX, camY, width, height)) continue;

	    // Get the light position on the world (kind of)
	    Point lightPos = gameManager.getCamera().worldToScreen(light.getX(), light.getY());

	    RadialGradientPaint gradient = new RadialGradientPaint(
		    lightPos.x, lightPos.y,
		    light.getRadius(),
		    light.getFractions(), light.getColors()
	    );
	    lightG.setPaint(gradient);
	    lightG.fillOval(lightPos.x - light.getRadius(), lightPos.y - light.getRadius(), light.getDiameter(), light.getDiameter());
	}

	lightG.dispose();
	g2.drawImage(lightMap, camX, camY, null);
	lightSources.clear();
	//if (gm.isDebugging()) pTimer.stopAndPrint();
    }

    private boolean isLightOnScreen(LightSourceComponent light, int camX, int camY, int screenWidth, int screenHeight) {
	int screenX = light.getX() - camX;
	int screenY = light.getY() - camY;
	int radius = light.getRadius();

	return screenX + radius >= 0 &&
	       screenX - radius <= screenWidth &&
	       screenY + radius >= 0 &&
	       screenY - radius <= screenHeight;
    }

    public DayCycleManager getDayManager() {
	return dayManager;
    }

    /**
     * Get the GPUs configurations (how it wants images to be stored)
     */
    private static GraphicsConfiguration getGpuConfigurations() {
	return GraphicsEnvironment
		.getLocalGraphicsEnvironment()
		.getDefaultScreenDevice()
		.getDefaultConfiguration();
    }
}

/*
Other ways to fill a BufferedImage with a specific color:

    lightG.setColor(dayManager.getTint()); // 0 ms
    lightG.fillRect(0, 0, lightMap.getWidth(), lightMap.getHeight()); // 10-15 ms

    lightG.setBackground(dayManager.getTint());
    lightG.clearRect(0, 0, lightMap.getWidth(), lightMap.getHeight()); // Ofta stabilt runt 7-10 men kan sticka iväg till 20 ibland

    int[] data = ((DataBufferInt) lightMap.getRaster().getDataBuffer()).getData();
    Arrays.fill(data,dayManager.getTint().getRGB()); // 8-13 ms
 */
