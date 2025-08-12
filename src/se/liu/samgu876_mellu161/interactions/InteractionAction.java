package se.liu.samgu876_mellu161.interactions;

import se.liu.samgu876_mellu161.gameobjects.GameObject;

/**
 * Represents an interaction that can be performed on a {@link GameObject}.
 * Each action has a name and defines behavior via the {@link #execute(GameObject)} method.
 */
public interface InteractionAction
{
    String getName();
    void execute(GameObject target);
}
