package se.liu.samgu876_mellu161.map;

import se.liu.samgu876_mellu161.assets.ImageAsset;
import se.liu.samgu876_mellu161.main.GameManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.logging.Level;

import static se.liu.samgu876_mellu161.main.Main.LOGGER;
import static se.liu.samgu876_mellu161.utilz.Constants.TILE_SIZE;
import static se.liu.samgu876_mellu161.utilz.HelpFunctions.isSquareOnScreen;

/**
 * Represents a game level made up of tiles loaded from a pixel map image.
 * Each tile corresponds to a specific color in the image, defining its type and properties.
 * Manages loading, updating, and drawing the level tiles.
 */
public class GameLevel
{
    private static final Map<Color, BiFunction<Integer, Integer, Tile>> TILE_MAPPINGS = Collections.unmodifiableMap(createColorMap());

    private static Map<? extends Color,? extends BiFunction<Integer, Integer, Tile>> createColorMap() {
	Map<Color, BiFunction<Integer, Integer, Tile>> result = new HashMap<>();
	result.put(new Color(0, 0, 0), (x, y) -> new CollisionTile(x, y, ImageAsset.WALL));
	result.put(new Color(0, 0, 255), (x, y) -> new NoCollisionTile(x, y, ImageAsset.WATER));
	result.put(new Color(255, 255, 255), (x, y) -> new NoCollisionTile(x, y, ImageAsset.GRASS));
	result.put(new Color(200, 200, 200), (x, y) ->  new NoCollisionTile(x, y, ImageAsset.GRASS_UP));
	result.put(new Color(230, 230, 230), (x, y) -> new NoCollisionTile(x, y, ImageAsset.GRASS_LEFT));
	result.put(new Color(210, 210, 210), (x, y) -> new NoCollisionTile(x, y, ImageAsset.GRASS_DOWN));
	result.put(new Color(220, 220, 220), (x, y) -> new NoCollisionTile(x, y, ImageAsset.GRASS_RIGHT));
	result.put(new Color(203, 203, 203), (x, y) -> new NoCollisionTile(x, y, ImageAsset.GRASS_TOP_LEFT));
	result.put(new Color(202, 202, 202), (x, y) -> new NoCollisionTile(x, y, ImageAsset.GRASS_TOP_RIGHT));
	result.put(new Color(213, 213, 213), (x, y) -> new NoCollisionTile(x, y, ImageAsset.GRASS_DOWN_LEFT));
	result.put(new Color(212, 212, 212), (x, y) -> new NoCollisionTile(x, y, ImageAsset.GRASS_DOWN_RIGHT));

	return result;
    }

    /**
     * How many tiles of water around the map
     */
    public static final int BORDER_SIZE = 6;

    private Tile[][] lvlData = null;
    private int width, height;
    private GameManager gameManager;

    public GameLevel(GameManager gm) {
	this.gameManager = gm;
    }

    public void loadMap(BufferedImage pixelMap) {
	LOGGER.log(Level.FINE, "Loading level...");
	boolean loadSuccessful = true;

	height = pixelMap.getHeight();
	width = pixelMap.getWidth();

	lvlData = new Tile[height][width];

	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		int pixelRGB = pixelMap.getRGB(x, y);
		Color color = new Color(pixelRGB);

		// Get the "tile factory"
		BiFunction<Integer, Integer, Tile> tileCreator = TILE_MAPPINGS.get(color);

		if (tileCreator != null) {
		    lvlData[y][x] = tileCreator.apply(x, y);
		} else {
		    lvlData[y][x] = new CollisionTile(x, y, ImageAsset.WALL);
		    loadSuccessful = false;
		    LOGGER.log(Level.WARNING, "Color [" + color + "] does not match any tile.");
		}
	    }
	}

	if (loadSuccessful) {
	    LOGGER.log(Level.FINE, "Level was loaded successfully.");
	} else {
	    LOGGER.log(Level.WARNING, "Level was not loaded successfully.");
	}
    }

    public void update() {
	for (int x = 0; x < width; x++)
	    for (int y = 0; y < height; y++)
		lvlData[y][x].update();
    }

    public void draw(Graphics2D g2) {
	for (int x = 0; x < width; x++) {
	    for (int y = 0; y < height; y++) {
		Tile tile = lvlData[y][x];
		if (!isTileOnScreen(tile)) continue;
		tile.draw(g2);
	    }
	}
    }

    private boolean isTileOnScreen(Tile tile) {
	int x = tile.getX();
	int y = tile.getY();
	int width = TILE_SIZE;
	int height = TILE_SIZE;
	int camX = (int) gameManager.getCamera().getX();
	int camY = (int) gameManager.getCamera().getY();
	int screenWidth = gameManager.getWidth();
	int screenHeight = gameManager.getHeight();

	return isSquareOnScreen(x, y, width, height, camX, camY, screenWidth, screenHeight);
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public int getSize() {
	return width * height;
    }
}
