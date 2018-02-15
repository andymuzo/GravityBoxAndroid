package com.anidari.gravity_box.pieces;

import com.anidari.gravity_box.tools.Vector2d;

public class GravWellHill extends GamePiece {

	public static final float GRAV_WELL_MASS = 600f;
	public static final float GRAV_WELL_DIAMETER = 10;
	
	public GravWellHill(boolean isWell, float posX, float posY) {
		position = new Vector2d(posX, posY);
		isActive = true;
		colour = NO_COLOUR;
		mass = GRAV_WELL_MASS;
		radius = GRAV_WELL_DIAMETER / 2;
		diameter = GRAV_WELL_DIAMETER;
		canDelete = false;
		addNewOrbiter = false;
		isConsumable = false;
	}
	
	@Override
	public void update(float delta, float worldWidth, float worldHeight) {
		// do nothing
		
	}

	@Override
	public void updateGravity(GamePiece gamePiece) {
		// do nothing
		
	}
	
	/**
	 * should be triggered when touch up
	 */
	public void deactivate() {
		isActive = false;
	}
}
