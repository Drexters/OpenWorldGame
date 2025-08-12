package se.liu.samgu876_mellu161.interactions;

import se.liu.samgu876_mellu161.gameobjects.GameObject;

/**
 * A basic implementation of {@link InteractionAction} that prints its name when executed.
 * Useful for testing and if execute logic is in the object.
 */
public class SimpleInteraction implements InteractionAction
{
    private String name;

    public SimpleInteraction(final String name) {
	this.name = name;
    }

    @Override public String getName() {
	return name;
    }

    @Override public void execute(final GameObject target) {
	System.out.println(name);
    }
}
