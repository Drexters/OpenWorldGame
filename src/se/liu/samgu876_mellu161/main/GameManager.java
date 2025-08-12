package se.liu.samgu876_mellu161.main;

import se.liu.samgu876_mellu161.assets.Asset;
import se.liu.samgu876_mellu161.assets.ImageAsset;
import se.liu.samgu876_mellu161.assets.SoundAsset;
import se.liu.samgu876_mellu161.gameobjects.GameObject;
import se.liu.samgu876_mellu161.gameobjects.ObjectID;
import se.liu.samgu876_mellu161.input.KeyHandler;
import se.liu.samgu876_mellu161.input.MouseHandler;
import se.liu.samgu876_mellu161.managers.DayCycleManager;
import se.liu.samgu876_mellu161.managers.EnvironmentManager;
import se.liu.samgu876_mellu161.managers.ObjectManager;
import se.liu.samgu876_mellu161.managers.UIManager;
import se.liu.samgu876_mellu161.map.GameLevel;
import se.liu.samgu876_mellu161.utilz.PerformanceTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.logging.Level;

import static se.liu.samgu876_mellu161.main.Main.LOGGER;
import static se.liu.samgu876_mellu161.utilz.Constants.PANEL_HEIGHT;
import static se.liu.samgu876_mellu161.utilz.Constants.PANEL_WIDTH;

/**
 * Main controller class for the game loop and rendering.
 * Manages input, updates, drawing, and coordination between major subsystems.
 */
public class GameManager extends JPanel
{
    private KeyHandler keyHandler = new KeyHandler();
    private MouseHandler mouseHandler = new MouseHandler();

    private ObjectManager objectManager = new ObjectManager(this);
    private UIManager uiManager = new UIManager(this);
    private EnvironmentManager environmentManager;

    private Camera camera;
    private final GameLevel gameLevel = new GameLevel(this);

    private boolean debugging = false;
    private PerformanceTimer drawTimer = new PerformanceTimer("draw");
    private PerformanceTimer updateTimer = new PerformanceTimer("update");

    public GameManager() {
        initPanel(PANEL_WIDTH,PANEL_HEIGHT);

        Asset.loadAll(ImageAsset.class);
        Asset.loadAll(SoundAsset.class);

        camera = new Camera(ObjectID.PLAYER, 0.08f, this);

        gameLevel.loadMap(ImageAsset.MAP_3.get());
        environmentManager = new EnvironmentManager(120, this);

        objectManager.initGameObjects();
        Asset.unloadAll(ImageAsset.class);
        uiManager.addMessage("Welcome!");

        // Start the main theme song
        SoundAsset.MAIN_THEME_SONG.playLoop();

        LOGGER.log(Level.FINE, "Game initialized successfully.");
    }

    private void initPanel(int width, int height){
        // INITIALIZE PANEL
        setPreferredSize(new Dimension(width, height));
        setBackground(new Color(0,0,0));
        setDoubleBuffered(true);

        // ADD LISTENERS
        addKeyListener(keyHandler);
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
        setFocusable(true);
    }

    public void updateGame(){
        if (debugging) updateTimer.start();
        gameLevel.update();
        objectManager.update();
        environmentManager.update();

        if (keyHandler.isKeyClicked(KeyEvent.VK_SPACE)) {
            debugging = !debugging;
            System.out.println("DEBUG: " + (debugging ? "ON" : "OFF"));
        }

        // INTERACT WITH OBJECTS
        if (mouseHandler.isButtonClicked(1)) {
            int mouseX = mouseHandler.getMouseX();
            int mouseY = mouseHandler.getMouseY();

            GameObject player = objectManager.getObject(ObjectID.PLAYER);

            // Adjust the mouse position on screen to world position
            Point worldPos = camera.screenToWorld(mouseX, mouseY);

            if (player.containsPoint(worldPos.x, worldPos.y)) {

                // Get all objects at this position
                List<GameObject> objects = objectManager.getObjectsAt(worldPos.x, worldPos.y);

                for (GameObject obj : objects) {
                    uiManager.showInteractionMenu(obj, mouseX, mouseY);
                    keyHandler.clearKeys();
                }
            }
        }

        if (environmentManager.getDayManager().getDayPhase() == DayCycleManager.DayPhase.NIGHT) {
            if (keyHandler.isKeyClicked(KeyEvent.VK_K)) {
                environmentManager.getDayManager().setDayPhase(DayCycleManager.DayPhase.DAWN);
            }
        }

        mouseHandler.updateMouse();
        keyHandler.updateKeys();
        camera.update();

        if (debugging) updateTimer.stopAndPrint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateGame();
        if (debugging) drawTimer.start();

        Graphics2D g2 = (Graphics2D) g;
        uiManager.setG2(g2);

        camera.applyTranslation(g2);
        // DRAW GAME STUFF HERE (AFFECTED BY TRANSLATION)

        gameLevel.draw(g2);
        objectManager.draw(g2);

        environmentManager.draw(g2); // This may cause lag, comment out this line if it lags too much

        camera.removeTranslation(g2);
        // DRAW UI HERE (NOT AFFECTED BY TRANSLATION)

        uiManager.draw();

        g2.dispose();
        if (debugging) drawTimer.stopAndPrint();
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public UIManager getUIManager() {
        return uiManager;
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    public boolean isDebugging() {
        return debugging;
    }

    public Camera getCamera() {
        return camera;
    }

    public EnvironmentManager getEnvironmentManager() {
        return environmentManager;
    }
}