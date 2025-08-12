package se.liu.samgu876_mellu161.interactions;

import se.liu.samgu876_mellu161.gameobjects.GameObject;

/**
 * An {@link InteractionAction} that sets the mass of the target {@link GameObject} to 0,
 * effectively making it static (immovable).
 */
public class MakeStaticInteraction implements InteractionAction
{
    @Override public String getName() {
	return "Make Static";
    }

    @Override public void execute(final GameObject target) {
	target.setMass(0);
    }
}
