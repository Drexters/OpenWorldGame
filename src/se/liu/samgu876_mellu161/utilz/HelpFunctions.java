package se.liu.samgu876_mellu161.utilz;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static se.liu.samgu876_mellu161.utilz.Constants.TARGET_FPS;

/**
 * Utility class containing various static helper functions used throughout the game.
 * Includes image and sound loading, volume control, sprite sheet handling,
 * coordinate checking, time conversions, and math utilities.
 *
 * <p>This class is not meant to be instantiated.
 */
public class HelpFunctions
{

    public static float clamp(float value, float min, float max) {
	return Math.max(min, Math.min(max, value));
    }

    public static Clip loadSound(String path) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
	URL url = ClassLoader.getSystemResource(path);
	if (url == null) throw new IOException("File [" + path + "] does not exist in resource folder.");
	AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	Clip clip = AudioSystem.getClip();
	clip.open(audioIn);
	return clip;
    }

    public static void setVolumeOnClip(Clip clip, float volume) {
	if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
	    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    float min = gainControl.getMinimum();
	    float max = gainControl.getMaximum();
	    float dB = (float) (Math.log10(Math.max(volume, Float.MIN_VALUE)) * 20); // dB = 20 x log10(amplitude), therefor * 20
	    dB = clamp(dB, min, max);
	    gainControl.setValue(dB);
	}
    }

    public static BufferedImage loadImage(String path) throws IOException {
	URL url = ClassLoader.getSystemResource(path);
	if (url == null) throw new IOException("File [" + path + "] does not exist in resource folder.");
	BufferedImage img = ImageIO.read(url);
	return img;
    }

    public static BufferedImage[][] loadTileSet(String path, int tileWidth, int tileHeight) throws IOException {
	BufferedImage image = loadImage(path);

	int cols = image.getWidth()/tileWidth;
	int rows = image.getHeight()/tileHeight;

	BufferedImage[][] tiles = new BufferedImage[rows][cols];

	for (int y = 0; y < rows; y++) {
	    for (int x = 0; x < cols; x++) {
		// + 1 ty en pixel mellan alla tilse, detta gör att grass04 är den ända som fungerar just nu
		tiles[y][x] = image.getSubimage(x*(tileWidth+1), y*(tileHeight+1), tileWidth, tileHeight);
	    }
	}

	return tiles;
    }

    public static int secToUpdates(int seconds) {
	return seconds * TARGET_FPS;
    }

    public static BufferedImage[] getImagesFromSpriteSheet(BufferedImage sheet, int imgWidth, int imgHeight, float scale, List<Point> indexes) {
	BufferedImage[] result = new BufferedImage[indexes.size()];

	for (int i = 0; i < result.length; i++) {
	    BufferedImage sub = sheet.getSubimage(
		    indexes.get(i).x * imgWidth,
		    indexes.get(i).y * imgHeight,
		    imgWidth, imgHeight
	    );

	    BufferedImage scaledSub = scaleImage(sub, scale);

	    result[i] = scaledSub;
	}
	return result;
    }

    public static BufferedImage scaleImage(BufferedImage img, float scale) {
	int width = (int)(img.getWidth()*scale);
	int height = (int)(img.getHeight()*scale);
	BufferedImage scaledImg = new BufferedImage(width, height, img.getType());
	Graphics2D g2 = scaledImg.createGraphics();
	g2.drawImage(img.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
	g2.dispose();
	return scaledImg;
    }

    public static double nanoToMs(double nanoSeconds) {
	return nanoSeconds / 1_000_000.0;
    }

    public static boolean isSquareOnScreen(int x, int y, int width, int height, int camX, int camY, int screenWidth, int screenHeight) {
	return x + width > camX &&
	       x < camX + screenWidth &&
	       y + height > camY &&
	       y < camY + screenHeight;
    }
}