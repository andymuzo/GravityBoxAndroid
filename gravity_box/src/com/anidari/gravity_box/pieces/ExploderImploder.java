package com.anidari.gravity_box.pieces;

import com.anidari.gravity_box.tools.Vector2d;

public class ExploderImploder extends GamePiece {

	public static final float GRAV_BOMB_MASS = -2500f;
	public static final float GRAV_BOMB_TIME = 0.2f;
	public static final float GRAV_WELL_DIAMETER = 10;

	public float activationTimer;

	public ExploderImploder(boolean isExploder, float posX, float posY) {
		position = new Vector2d(posX, posY);
		isActive = true;
		activationTimer = 0f;
		colour = NO_COLOUR;
		mass = isExploder ? GRAV_BOMB_MASS : GRAV_BOMB_MASS * -1;
		radius = GRAV_WELL_DIAMETER / 2;
		diameter = GRAV_WELL_DIAMETER;
		canDelete = false;
		addNewOrbiter = false;
		isConsumable = false;
	}

	@Override
	public void update(float delta, float worldWidth, float worldHeight) {
		if (isActive) {
			// count up how long it has been active for
			activationTimer += delta;
			// bomb explodes for a certain amount of time
			if (activationTimer > GRAV_BOMB_TIME) {
				isActive = false;
			}
		}
	}

	@Override
	public void updateGravity(GamePiece gamePiece) {
		// do nothing
	}
	
}
