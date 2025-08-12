package se.liu.samgu876_mellu161.components;

import se.liu.samgu876_mellu161.gameobjects.GameObject;
import se.liu.samgu876_mellu161.main.GameManager;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import static se.liu.samgu876_mellu161.utilz.Constants.TILE_SIZE;


/**
 * Represents an Axis Aligned Bounding Box (AABB) component for a {@link GameObject}.
 * Handles collision registration, hitbox positioning, and tracking overlapping tiles.
 * Can be marked as solid for physical collisions, or non-solid for zone-based triggers.
 */
public class AABBComponent extends GameComponent
{
    private final Rectangle2D.Float hitbox;
    private Point offset = new Point(0, 0);
    private boolean solid;
    private Set<Point> overlapTiles = new HashSet<>();

    public AABBComponent(final GameManager gm, final GameObject parent, boolean solid) {
	super(gm, parent);
	hitbox = new Rectangle2D.Float(parent.getX(), parent.getY(), parent.getWidth(), parent.getHeight());
	this.solid = solid;
	updateOverlappingTiles();
    }

    @Override public void update() {
	if (parent.shouldRemove()) return;

	if (!parent.isStatic()) { // If parent is static, no need to update position and overlapping tiles
	    updatePos();
	    updateOverlappingTiles();
	}

	// Register to the AABBCollision-handler
	gameManager.getObjectManager().getAabbCollision().registerAABBComponent(this);
    }

    private void updatePos() {
	hitbox.x = parent.getX() + offset.x;
	hitbox.y = parent.getY() + offset.y;
    }

    @Override public void render(final Graphics2D g2) {
	g2.setColor(Color.RED);
	g2.drawRect((int)hitbox.x, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    private void updateOverlappingTiles() {
	overlapTiles.clear();

	int startX = (int)Math.floor(hitbox.x / TILE_SIZE);
	int startY = (int)Math.floor(hitbox.y / TILE_SIZE);
	int endX = (int)Math.floor((hitbox.x + hitbox.width) / TILE_SIZE);
	int endY = (int)Math.floor((hitbox.y + hitbox.height) / TILE_SIZE);

	for (int x = startX; x <= endX; x++) {
	    for (int y = startY; y <= endY; y++) {
		overlapTiles.add(new Point(x, y));
	    }
	}
    }

    public void moveAccordingToCollision(float dx, float dy) {
	hitbox.x += dx;
	hitbox.y += dy;

	parent.updatePosAccordingToHitbox(this);
    }

    public Rectangle2D.Float getHitbox() {
	return hitbox;
    }

    public void setOffset(int offX, int offY) {
	this.offset = new Point(offX, offY);
	updatePos();
    }

    public boolean isSolid() {
	return solid;
    }

    public Point getOffset() {
	return offset;
    }

    public Set<Point> getOverlapTiles() {
	return overlapTiles;
    }
}
