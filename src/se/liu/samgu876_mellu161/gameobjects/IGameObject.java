package se.liu.samgu876_mellu161.gameobjects;

import se.liu.samgu876_mellu161.components.AABBComponent;
import se.liu.samgu876_mellu161.interactions.InteractionAction;

import java.awt.*;

/**
 * Interface for game objects that defines the core behaviors for updating, rendering,
 * handling collisions, and interaction in the game world.
 * Any class implementing this interface must provide implementations for these actions.
 */
public interface IGameObject
{
    void update();
    void draw(Graphics2D g2);
    void handleCollision(final AABBComponent otherAABB);
    void interact(InteractionAction action);
}
