package se.liu.samgu876_mellu161.main;

import javax.swing.*;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The entry point of the game.
 * Loads all assets, initializes the game manager, and starts the game loop.
 */
public class Main {
    /**
     * Global logger instance for the application. Use this to log messages throughout the program.
     */
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        // SET UP THE LOGGER
        try {
            LogManager.getLogManager().reset();

            FileHandler fileHandler = new FileHandler("log.txt", false);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);

            LOGGER.setLevel(Level.ALL);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to set up logger", e);
            e.printStackTrace();
        }

        LOGGER.log(Level.INFO, "Started application");

        // START THE APPLICATION
        GameManager gameManager = new GameManager();
        GameContainer gameContainer = new GameContainer(gameManager);
        gameContainer.setUpFrame();
        gameContainer.startGame();
    }
}
