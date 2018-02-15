package com.anidari.gravity_box.pieces;

import java.util.ArrayList;

import com.anidari.gravity_box.tools.Vector2d;

public class Remover extends GamePiece {

	public static final float REMOVER_DIAMETER = 10f;

	public ArrayList<GamePiece> piecesToDelete;
	
	public Remover(float posX, float posY) {
		position = new Vector2d(posX, posY);
		isActive = true;
		colour = NO_COLOUR;
		mass = 0;
		radius = REMOVER_DIAMETER / 2;
		diameter = REMOVER_DIAMETER;
		canDelete = true;
		addNewOrbiter = false;
		piecesToDelete = new ArrayList<GamePiece>();
	}

	@Override
	public void update(float delta, float worldWidth, float worldHeight) {
		// clear delete list
		piecesToDelete.clear();
	}

	@Override
	public void updateGravity(GamePiece gamePiece) {
		// distance between centres
		float minDistanceBetweenCentresSquared = (gamePiece.radius + radius) * (gamePiece.radius + radius);
		
		float distanceBetweenCentresSquared = Vector2d.fromPoints(gamePiece.position.x, gamePiece.position.y, position.x, position.y).getMagnitudeSquared();
		// collision detection
		if (distanceBetweenCentresSquared < minDistanceBetweenCentresSquared) {
			// add to delete list
			piecesToDelete.add(gamePiece);
		}
	}
	
	@Override
	public ArrayList<GamePiece> getDeleteList() {
		return piecesToDelete;
	}

}
