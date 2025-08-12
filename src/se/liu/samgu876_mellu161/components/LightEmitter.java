package se.liu.samgu876_mellu161.components;

/**
 * Interface for objects that can emit light in the game.
 * Provides a method to retrieve the associated {@link LightSourceComponent}.
 */
public interface LightEmitter
{
    LightSourceComponent getLightSource();
}
