package se.liu.samgu876_mellu161.utilz;

import se.liu.samgu876_mellu161.components.AABBComponent;
import se.liu.samgu876_mellu161.gameobjects.GameObject;

import java.util.Comparator;

/**
 * Comparator used to sort GameObjects based on their bottom Y-coordinate.
 * Useful for rendering objects in the correct visual order (e.g., for depth layering).
 */
public class PositionComparator implements Comparator<GameObject>
{
    @Override public int compare(final GameObject obj1, final GameObject obj2) {

	AABBComponent aabb1 = (AABBComponent) obj1.getComponent("aabb");
	AABBComponent aabb2 = (AABBComponent) obj2.getComponent("aabb");
	float bottom1, bottom2;

	if (aabb1 != null) {
	    bottom1 = aabb1.getHitbox().y + aabb1.getHitbox().height;
	} else {
	    bottom1 = obj1.getY() + obj1.getHeight();
	}

	if (aabb2 != null) {
	    bottom2 = aabb2.getHitbox().y + aabb2.getHitbox().height;
	} else {
	    bottom2 = obj2.getY() + obj2.getHeight();
	}

	return Float.compare(bottom1, bottom2);
    }
}
