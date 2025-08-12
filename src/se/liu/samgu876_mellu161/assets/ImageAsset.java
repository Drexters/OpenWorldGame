package se.liu.samgu876_mellu161.assets;


import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;

import static se.liu.samgu876_mellu161.main.Main.LOGGER;
import static se.liu.samgu876_mellu161.utilz.HelpFunctions.loadImage;
import static se.liu.samgu876_mellu161.utilz.HelpFunctions.loadTileSet;

/**
 * Represent all image assets used in the game, such as pixel-maps, tiles, player sprites and enemies.
 * Each enum constant holds a file path to the image and optional tile coordinates if it's part of a tileset.
 * Implements the {@link Asset} interface for load and unload functionality.
 */
public enum ImageAsset implements Asset
{
    // --- PIXEL-MAPS -----------------------------
    MAP_0("images/map_0.png"),
    MAP_1("images/map_2.png"),
    MAP_3("images/map_3.png"),
    MAP_4("images/map_4.png"),

    // --- TILES ----------------------------------
    GRASS("images/grass04.png", 1, 1),
    GRASS_UP("images/water1.png", 0, 1),
    GRASS_DOWN("images/water1.png", 2, 1),
    GRASS_RIGHT("images/water1.png", 1, 2),
    GRASS_LEFT("images/water1.png", 1, 0),
    GRASS_TOP_RIGHT("images/water1.png", 0, 2),
    GRASS_TOP_LEFT("images/water1.png", 0, 0),
    GRASS_DOWN_RIGHT("images/water1.png", 2, 2),
    GRASS_DOWN_LEFT("images/water1.png", 2, 0),
    WALL("images/grass04.png", 0, 9),
    WATER("images/water2.png", 4, 1),

    // --- PLAYER ----------------------------------
    CHARACTER_BASIC("images/player/char_a_p1/char_a_p1_0bas_humn_v01.png"),
//    CHARACTER_ARMOUR_GOLD("images/player/char_a_p1/1out/char_a_p1_1out_fstr_v04.png"),
//    CHARACTER_ARMOUR_DIAMOND("images/player/char_a_p1/1out/char_a_p1_1out_pfpn_v04.png"),
//    CHARACTER_HAIR("images/player/char_a_p1/4har/char_a_p1_4har_bob1_v01.png"),
//    CHARACTER_HAT("images/player/char_a_p1/5hat/char_a_p1_5hat_pnty_v04.png"),
//    CHARACTER_BOXERS("images/player/char_a_p1/1out/char_a_p1_1out_boxr_v01.png"),
//    CHARACTER_WOMEN_UNDERWEAR("images/player/char_a_p1/1out/char_a_p1_1out_undi_v01.png"),

    //--- ENVIRONMENT ------------------------------
    TREE("images/environment/Tree-1-4.png"), STONE("images/environment/stone.png"),

    // --- ENEMIES ---------------------------------
    ANGRY_PLANT("images/enemy/Plant_attack.png"), FIRE_SKULL("images/enemy/fire_skull.png"),

    ;
    private final String path;
    private boolean isTile = false;
    private int tileX, tileY;
    private BufferedImage image = null;
    private static final int TILE_SIZE = 32;

    ImageAsset(String path) {
	this.path = path;
    }

    ImageAsset(String path, int tileY, int tileX) {
	this.path = path;
	this.isTile = true;
	this.tileX = tileX;
	this.tileY = tileY;
    }

    public BufferedImage get() {
	if (image == null) {
	    load();
	}
	return image;
    }

    @Override public void load() {
	if (image != null) {
	    return; // Already loaded
	}

	while(true) {
	    try {
		if (isTile) {
		    image = loadTileSet(path, TILE_SIZE, TILE_SIZE)[tileY][tileX];
		} else {
		    image = loadImage(path);
		}
		LOGGER.log(Level.INFO, "Loaded image: " + this + " [" + path + "]");
		return;

	    } catch (IOException e) {
		LOGGER.log(Level.SEVERE, "Unable to load image: " + this + " [" + path + "]", e);

		int input = JOptionPane.showConfirmDialog(
		    null,
		    "Unable to load image: " + this + "\nWould you like to try again?",
		    "Error",
		    JOptionPane.YES_NO_OPTION,
		    JOptionPane.ERROR_MESSAGE
		);

		if (input == JOptionPane.NO_OPTION) {
		    return;
		}
	    }
	}
    }

    @Override public void unload() {
	image = null;
	LOGGER.log(Level.INFO, "Unloaded image: " + this + " [" + path + "]");
    }
}
