package com.anidari.gravity_box.backgrounds;

import com.anidari.gravity_box.GameWorld;
import com.anidari.gravity_box.tools.Vector2d;

/**
 * stores the information needed for making a star in the background.
 * 
 * @author ajrog_000
 * 
 */
public class Star implements Comparable<Star> {

	public static final float MAX_SIZE = 9f;
	public static final float MIN_SIZE = 3f;
	public static final float MAX_SPEED = 4f;
	public static final float MAX_DEPTH = 15f;

	private Vector2d position;
	private Vector2d offSetPosition;
	private Vector2d wrapPosition;
	// depth is 0 when at the plane of play, increases to a maximum of 10f
	private float depth;
	private Vector2d speed;
	private float size;

	private GameWorld gameWorld;

	public Star(GameWorld gameWorld, Vector2d position, float depth,
			Vector2d speed) {
		this.gameWorld = gameWorld;
		this.position = position;
		this.offSetPosition = new Vector2d(position);
		this.wrapPosition = new Vector2d(offSetPosition);
		this.depth = depth;
		this.speed = speed;
		// TODO: the way the depth works needs sorting
		this.speed.timesBy((MAX_DEPTH - depth) / MAX_DEPTH);
		// size should be a function of depth
		this.size = (((MAX_DEPTH - depth) / MAX_DEPTH) * (MAX_SIZE - MIN_SIZE))
				+ MIN_SIZE;
	}

	public void update(float tiltX, float tiltY, float delta) {
		position.addVectorByScalar(speed, delta);
		// boundary checking; the star should be completely off-screen before
		// being moved
		// check y
		if (position.y > gameWorld.worldHeight) {
			// move to the bottom
			position.y = position.y - (gameWorld.worldHeight * 2f) - size;
			// reset the tilt setting
			offSetPosition.y = position.y + (tiltY * depth);
		} else if (position.y < (gameWorld.worldHeight * -1f) - size) {
			// move to the top
			position.y = position.y + (gameWorld.worldHeight * 2f) + size;
			offSetPosition.y = position.y + (tiltY * depth);
		}

		// check x
		if (position.x > gameWorld.worldWidth) {
			// move to the left
			position.x = position.x - (gameWorld.worldWidth * 2f) - size;
			offSetPosition.x = position.x + (tiltX * depth);
		} else if (position.x < (gameWorld.worldWidth * -1f) - size) {
			// move to the right
			position.x = position.x + (gameWorld.worldWidth * 2f) + size;
			offSetPosition.x = position.x + (tiltX * depth);
		}

		// tilts
		// now update the offset position vector
		offSetPosition.averageWithPoints(position.x + (tiltX * depth), position.y
				+ (tiltY * depth));
		// check boundaries of the tilt offset, wrap around
		// check y
		if (offSetPosition.y > gameWorld.worldHeight) {
			// move to the bottom
			wrapPosition.y = offSetPosition.y - (gameWorld.worldHeight * 2f) - size;
		} else if (offSetPosition.y < (gameWorld.worldHeight * -1f) - size) {
			// move to the top
			wrapPosition.y = offSetPosition.y + (gameWorld.worldHeight * 2f) + size;
		} else {
			wrapPosition.y = offSetPosition.y;
		}

		// check x
		if (offSetPosition.x > gameWorld.worldWidth) {
			// move to the left
			wrapPosition.x = offSetPosition.x - (gameWorld.worldWidth * 2f) - size;
		} else if (offSetPosition.x < (gameWorld.worldWidth * -1f) - size) {
			// move to the right
			wrapPosition.x = offSetPosition.x + (gameWorld.worldWidth * 2f) + size;
		} else {
			wrapPosition.x = offSetPosition.x;
		}
	}

	public Vector2d getPosition() {
		return wrapPosition;
	}

	public float getDepth() {
		return depth;
	}

	public float getSize() {
		return size;
	}

	@Override
	public int compareTo(Star otherStar) {
		// sorts in order smallest to largest
		return (int) ((size - otherStar.size) * 10f);
	}
}
