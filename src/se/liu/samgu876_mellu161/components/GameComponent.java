package se.liu.samgu876_mellu161.components;

import se.liu.samgu876_mellu161.gameobjects.GameObject;
import se.liu.samgu876_mellu161.main.GameManager;


/**
 * Base class for components that can be attached to a {@link GameObject}.
 * Provides optional update and render behavior, and access to the parent object.
 */
public abstract class GameComponent implements IGameComponent
{
    protected GameObject parent;
    protected GameManager gameManager;

    protected GameComponent(final GameManager gm, final GameObject parent) {
        this.gameManager = gm;
        this.parent = parent;
    }

    public GameObject getParent() {
	return parent;
    }
}
