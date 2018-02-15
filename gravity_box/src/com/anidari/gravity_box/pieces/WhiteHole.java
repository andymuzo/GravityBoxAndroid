package com.anidari.gravity_box.pieces;

import com.anidari.gravity_box.tools.Vector2d;

public class WhiteHole extends GamePiece {

	public static final float WHITE_HOLE_DIAMETER = 8f;
	public static final float WHITE_HOLE_MASS = 0f;
	
	public static final float MIN_TIME = 1f;
	public static final float MAX_TIME = 6f;
	
	public float targetTime;
	public float totalTime;
	
	public WhiteHole(float posX, float posY) {
		position = new Vector2d(posX, posY);
		velocity = new Vector2d(0f, 0f);
		isActive = true;
		colour = 16;
		mass = WHITE_HOLE_MASS;
		radius = WHITE_HOLE_DIAMETER / 2;
		diameter = WHITE_HOLE_DIAMETER;
		canDelete = false;
		setTargetTime();
		totalTime = 0f;
		
		addNewOrbiter = false;
		isConsumable = false;
	}
	
	/**
	 * the time to elapse before the next piece is made
	 */
	public void setTargetTime() {
		targetTime = (((float) Math.random()) * (MAX_TIME - MIN_TIME)) + MIN_TIME;
	}
	
	/**
	 * updates the timer and flags for adding a new orbiter if the time is met
	 */
	@Override
	public void update(float delta, float worldWidth, float worldHeight) {
		// add to total time
		totalTime += delta;
		addNewOrbiter = false;
		
		if (totalTime > targetTime) {
			// create new piece
			addNewOrbiter = true;
			// reset counter
			setTargetTime();
			totalTime = 0f;
		}
	}

	@Override
	public void updateGravity(GamePiece gamePiece) {
		// empty
	}
}
