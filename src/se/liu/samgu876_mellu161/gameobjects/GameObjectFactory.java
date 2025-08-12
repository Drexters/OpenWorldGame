package se.liu.samgu876_mellu161.gameobjects;

/**
 * Factory interface for creating GameObject instances at specified coordinates.
 */
public interface GameObjectFactory
{
    GameObject create(int x, int y);
}
