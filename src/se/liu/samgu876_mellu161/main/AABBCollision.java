package se.liu.samgu876_mellu161.main;

import se.liu.samgu876_mellu161.components.AABBComponent;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles Axis-Aligned Bounding Box (AABB) collision detection and resolution between registered components.
 *
 * <p>This class is responsible for:
 * <ul>
 *     <li>Registering all AABB components that may participate in collisions during the update cycle.</li>
 *     <li>Filtering invalid pairs (e.g. same parent, both static, or not sharing overlapping tiles).</li>
 *     <li>Detecting collisions and notifying the involved game objects via {@code handleCollision}.</li>
 *     <li>Resolving solid-to-solid collisions based on mass and axis overlap.</li>
 * </ul>
 * <p>
 * Should be invoked once per update cycle via {@link #update()} to process all potential collisions.
 */
public class AABBCollision
{
    private List<AABBComponent> aabbComponents = new ArrayList<>();

    public AABBCollision() {}

    public void registerAABBComponent(AABBComponent aabb) {
	aabbComponents.add(aabb);
    }

    public void update() {
	for (int i = 0; i < aabbComponents.size(); i++) {
	    for (int j = i+1; j < aabbComponents.size(); j++) {

		AABBComponent aabb0 = aabbComponents.get(i);
		AABBComponent aabb1 = aabbComponents.get(j);

		// Filter out unvalid component pairs
		if (
			(aabb0.getParent().equals(aabb1.getParent())) || 		    // Both have same parent
			(aabb0.getParent().isStatic() && aabb1.getParent().isStatic()) ||   // Both parents are static
			(!shareOverlappingTiles(aabb0, aabb1)) ||			    // Don't share any overlapping tiles
			(!aabb0.isSolid() && !aabb1.isSolid())				    // Both are "zones"
		) {
		    continue;
		}

		// Valid aabb:s, check collision
		if (aabb0.getHitbox().intersects(aabb1.getHitbox())) {

		    if (aabb0.isSolid() && aabb1.isSolid()) // If both are dynamically movable (i.e., not pick-up items)
			resolveCollision(aabb0, aabb1);

		    aabb0.getParent().handleCollision(aabb1);
		    aabb1.getParent().handleCollision(aabb0);
		}
	    }
	}
	aabbComponents.clear();
    }

    private boolean shareOverlappingTiles(final AABBComponent aabb0, final AABBComponent aabb1) {
	for (Point tile : aabb0.getOverlapTiles())
	    if (aabb1.getOverlapTiles().contains(tile))
		return true;
	return false;
    }

    private void resolveCollision(AABBComponent c0, AABBComponent c1) {
	// Get the intersection rectangle
	Rectangle2D intersection = c0.getHitbox().createIntersection(c1.getHitbox());

	// Get the overlap amounts
	double intersectionWidth = intersection.getWidth();
	double intersectionHeight = intersection.getHeight();

	// Determine if it's horizontal or vertical collision
	boolean horizontal = intersectionWidth < intersectionHeight;

	// Get the overlap amount in this axis
	double overlap = horizontal ? intersectionWidth : intersectionHeight;

	// Remember, we already know that at least one of the components are dynamic
	if (c0.getParent().isStatic()) {
	    // c0 is static, move c1
	    resolveDynamicToStaticCollision(c0, c1, horizontal, overlap);
	} else if (c1.getParent().isStatic()) {
	    // c1 is static, move c0
	    resolveDynamicToStaticCollision(c1, c0, horizontal, overlap);
	} else {
	    // Move both
	    resolveDynamicCollision(c0, c1, horizontal, overlap);
	}
    }

    private void resolveDynamicToStaticCollision(AABBComponent staticComponent, AABBComponent dynamicComponent, boolean horizontal, double overlap) {
	if (horizontal) {
	    if (staticComponent.getHitbox().x < dynamicComponent.getHitbox().x) {
		dynamicComponent.moveAccordingToCollision((float)overlap, 0); // █←c   =>   █ c→
	    } else {
		dynamicComponent.moveAccordingToCollision((float)-overlap, 0); // c→█   =>   ←c █
	    }
	} else {
	    // Same logic as above but for y-axis
	    if (staticComponent.getHitbox().y < dynamicComponent.getHitbox().y) {
		// static component is above, move dynamic component down
		dynamicComponent.moveAccordingToCollision(0, (float)overlap);
	    } else {
		// static component is under, move dynamic component up
		dynamicComponent.moveAccordingToCollision(0, (float)-overlap);
	    }
	}
    }

    private void resolveDynamicCollision(AABBComponent c0, AABBComponent c1, boolean horizontal, double overlap) {
	float totalMass = c0.getParent().getMass() + c1.getParent().getMass();

	// Calculate how much each component should move
	float move0 = (float) ((c1.getParent().getMass() / totalMass) * overlap);
	float move1 = (float) overlap - move0;

	if (horizontal) {
	    int direction = (c0.getHitbox().x < c1.getHitbox().x) ? -1 : 1; // c0→←c1 => ←c0 c1→   |   c1→←c0 => ←c1 c0→

	    c0.moveAccordingToCollision(move0 * direction, 0);
	    c1.moveAccordingToCollision(-move1 * direction, 0);
	} else {
	    // Same logic as above but for y-axis
	    int direction = (c0.getHitbox().y < c1.getHitbox().y) ? -1 : 1;

	    c0.moveAccordingToCollision(0, move0 * direction);
	    c1.moveAccordingToCollision(0, -move1 * direction);
	}
    }
}