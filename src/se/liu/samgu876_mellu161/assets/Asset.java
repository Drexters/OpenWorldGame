package se.liu.samgu876_mellu161.assets;

import java.util.logging.Level;

import static se.liu.samgu876_mellu161.main.Main.LOGGER;

/**
 * Interface for loadable and unloadable assets such as images, sounds, etc.
 */
public interface Asset
{
    void load();
    void unload();

    static <T extends Enum<T> & Asset> void unloadAll(Class<T> clazz) {
	for (T asset : clazz.getEnumConstants())
	    asset.unload();
	LOGGER.log(Level.FINE, "Unloaded all " + clazz.getSimpleName() + "s");
    }


    static <T extends Enum<T> & Asset> void loadAll(Class<T> clazz) {
	for (T asset : clazz.getEnumConstants())
	    asset.load();
	LOGGER.log(Level.FINE, "Successfully loaded all " + clazz.getSimpleName() + "s");
    }
}