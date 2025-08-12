package se.liu.samgu876_mellu161.components;

import se.liu.samgu876_mellu161.gameobjects.GameObject;

import java.awt.*;

/**
 * Interface for components that can be attached to a {@link GameObject}.
 * Defines methods for updating and rendering the component.
 */
public interface IGameComponent
{
    void update();
    void render(Graphics2D g2);
}
