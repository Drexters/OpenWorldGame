package se.liu.samgu876_mellu161.utilz;

import java.awt.*;
import java.util.Random;

/**
 * Holds global constants used throughout the game.
 * Includes settings such as target FPS, scaling factors, panel dimensions,
 * tile size, default fonts, and a shared random number generator.
 */
public class Constants
{
    /**
     * The targeted FPS-rate for the game.
     */
    public final static int TARGET_FPS = 60;

    /**
     * The amount we scale tha game. Use this constant if you want something to scale the same way as the rest of the game.
     */
    public final static float SCALE = 3;

    /**
     * The map is built out of a grid (tiles). Each tile/grid-cell has this size.
     */
    public final static int TILE_SIZE = (int)(32 * SCALE);

    /**
     * The width of the game panel.
     */
    public final static int PANEL_WIDTH = 1400;

    /**
     * The height of the game panel
     */
    public final static int PANEL_HEIGHT = 1100;

    /**
     * Use this for random values in the game. So that we don't have to initialize
     * new random-objects all over the game (that could cause lag).
     */
    public final static Random RND = new Random();

    /**
     * Default font used in the game. Font: Arial. Size = 10. PLAIN font
     */
    public final static Font ARIAL_10 = new Font("Arial", Font.PLAIN, (int)(10 * SCALE));
}
