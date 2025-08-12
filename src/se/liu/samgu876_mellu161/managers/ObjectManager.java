package se.liu.samgu876_mellu161.managers;

import se.liu.samgu876_mellu161.gameobjects.AngryPlant;
import se.liu.samgu876_mellu161.gameobjects.FireSkull;
import se.liu.samgu876_mellu161.gameobjects.GameObject;
import se.liu.samgu876_mellu161.gameobjects.GameObjectFactory;
import se.liu.samgu876_mellu161.gameobjects.ObjectID;
import se.liu.samgu876_mellu161.gameobjects.PickUpItem;
import se.liu.samgu876_mellu161.gameobjects.Player;
import se.liu.samgu876_mellu161.gameobjects.Bed;
import se.liu.samgu876_mellu161.gameobjects.StaticObject;
import se.liu.samgu876_mellu161.gameobjects.Stone;
import se.liu.samgu876_mellu161.gameobjects.Tree;
import se.liu.samgu876_mellu161.interactions.MakeStaticInteraction;
import se.liu.samgu876_mellu161.main.AABBCollision;
import se.liu.samgu876_mellu161.main.GameManager;
import se.liu.samgu876_mellu161.map.GameLevel;
import se.liu.samgu876_mellu161.utilz.PositionComparator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static se.liu.samgu876_mellu161.main.Main.LOGGER;
import static se.liu.samgu876_mellu161.utilz.Constants.RND;
import static se.liu.samgu876_mellu161.utilz.Constants.TILE_SIZE;
import static se.liu.samgu876_mellu161.utilz.HelpFunctions.isSquareOnScreen;

/**
 * Handles creation, updating, rendering and management of all {@link GameObject}s.
 *
 * <p>The {@link ObjectManager} is responsible for initializing the world with objects,
 * handling objects, collision checks and ensuring objects are only draw and updated
 * when visible on screen.
 *
 * <p>New objects can be added or removed using {@link #addObject(GameObject)} and {@link #removeObject(GameObject)}.
 * All game objects are sorted each frame using a {@link PositionComparator} to ensure correct rendering order.
 */
public class ObjectManager
{
    private final PositionComparator positionComparator = new PositionComparator();

    private List<GameObject> objects = new ArrayList<>();
    private List<GameObject> toAdd = new ArrayList<>();
    private List<GameObject> toRemove = new ArrayList<>();
    private GameManager gameManager;
    private AABBCollision aabbCollision = new AABBCollision();

    public ObjectManager(GameManager gm) {
        this.gameManager = gm;
    }

    public void initGameObjects() {
        objects.add(new Player(gameManager, TILE_SIZE * 20, TILE_SIZE * 20));
        Bed bed = new Bed(gameManager, TILE_SIZE * 21, TILE_SIZE * 20);
        bed.addAction(new MakeStaticInteraction());
        objects.add(bed);

        // World borders
        int mapH = gameManager.getGameLevel().getHeight();
        int mapW = gameManager.getGameLevel().getWidth();
        int borderSize = (int)(TILE_SIZE * 4.4f);
        objects.add(new StaticObject(gameManager, 0, 0, borderSize, TILE_SIZE * mapH));
        objects.add(new StaticObject(gameManager, 0, 0, TILE_SIZE * mapW, borderSize));
        objects.add(new StaticObject(gameManager, 0, TILE_SIZE * mapH - borderSize, TILE_SIZE * mapW, borderSize));
        objects.add(new StaticObject(gameManager, TILE_SIZE * mapW - borderSize, 0, borderSize, TILE_SIZE * mapH));

        // ENVIRONMENT
        int levelSize = gameManager.getGameLevel().getSize();
        int amount = levelSize / 6;
        spawnObjects(amount, (x, y) -> new Tree(gameManager, x, y));

        amount = levelSize / 100;
        spawnObjects(amount, (x, y) -> new Stone(gameManager, x, y));


        amount = levelSize / 200;
        spawnObjects(amount, (x, y) -> new AngryPlant(gameManager, x, y));

        amount = levelSize / 100;
        spawnObjects(amount, (x, y) -> new PickUpItem(gameManager, x, y));

        amount = levelSize / 150;
        spawnObjects(amount, (x, y) -> new FireSkull(gameManager, x, y));

        LOGGER.log(Level.FINE, getClass().getSimpleName() + " successfully loaded " + objects.size() + " objects.");
    }

    public void update() {

        // Update objects
        for (GameObject obj : objects) {
            if (!isObjectOnScreen(obj)) continue;
            obj.update();
            obj.updateComponents();
        }

        aabbCollision.update();

        // Remove the dead objects
        objects.removeAll(toRemove);
        toRemove.clear();

        objects.addAll(toAdd);
        toAdd.clear();

        // Sort objects by their position on the map
        objects.sort(positionComparator);
    }

    public void draw(Graphics2D g2) {
        for (GameObject obj : objects) {
            if (!isObjectOnScreen(obj)) continue;
            obj.draw(g2);
            if (gameManager.isDebugging())
                obj.drawDebug(g2);
        }
    }

    public void addObject(GameObject obj) {
        toAdd.add(obj);
    }

    public void removeObject(GameObject obj) {
        toRemove.add(obj);
    }

    public GameObject getObject(ObjectID id){
        for(GameObject object : objects) {
            if (object.getID() == id) {
                return object;
            }
        }
        return null;
    }

    public List<GameObject> getObjectsAt(final int x, final int y) {
        List<GameObject> result = new ArrayList<>();
        for (GameObject obj : objects)
            if (obj.containsPoint(x,y))
                result.add(obj);
        return result;
    }

    private boolean isObjectOnScreen(GameObject obj) {
        Rectangle visibleZone = obj.getVisibleZone();

        int x = visibleZone.x;
        int y = visibleZone.y;
        int width = visibleZone.width;
        int height = visibleZone.height;

        final int camX = (int) gameManager.getCamera().getX();
        final int camY = (int) gameManager.getCamera().getY();
        final int screenWidth = gameManager.getWidth();
        final int screenHeight = gameManager.getHeight();

        return isSquareOnScreen(x, y, width, height, camX, camY, screenWidth, screenHeight);
    }

    private void spawnObjects(int amount, GameObjectFactory factory) {
        final int mapH = gameManager.getGameLevel().getHeight();
        final int mapW = gameManager.getGameLevel().getWidth();
        final int mapBorderWidth = GameLevel.BORDER_SIZE;

        for (int i = 0; i < amount; i++) {
            int x = (RND.nextInt(mapW - 2 * mapBorderWidth) + mapBorderWidth) * TILE_SIZE;
            int y = (RND.nextInt(mapH - 2 * mapBorderWidth) + mapBorderWidth) * TILE_SIZE;
            x += RND.nextInt(TILE_SIZE) - TILE_SIZE/2;
            y += RND.nextInt(TILE_SIZE) - TILE_SIZE/2;
            objects.add(factory.create(x, y));
        }
    }

    public AABBCollision getAabbCollision() {
        return aabbCollision;
    }
}
