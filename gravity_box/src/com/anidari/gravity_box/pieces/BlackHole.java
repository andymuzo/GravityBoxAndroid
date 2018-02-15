package com.anidari.gravity_box.pieces;

import java.util.ArrayList;

import com.anidari.gravity_box.GameWorld;
import com.anidari.gravity_box.tools.Vector2d;

public class BlackHole extends GamePiece {

	public static final float BLACK_HOLE_MIN_DIAMETER = 5f;
	public static final float BLACK_HOLE_MAX_RADIUS = 100f;
	public static final float GROW_FACTOR = 0.2f; // how much it grows in
													// relation to what was
													// absorbed
	// this is the standard measurement i.e. for every 1 area there is 6 mass
	public static float BLACK_HOLE_DENSITY = 6f;

	public ArrayList<GamePiece> piecesToDelete;
	private boolean grows;

	public BlackHole(float posX, float posY, boolean grows) {
		position = new Vector2d(posX, posY);
		isActive = true;
		colour = 15;
		mass = getMassFromDiameter(BLACK_HOLE_MIN_DIAMETER);
		radius = BLACK_HOLE_MIN_DIAMETER / 2;
		diameter = BLACK_HOLE_MIN_DIAMETER;
		canDelete = true;
		piecesToDelete = new ArrayList<GamePiece>();
		addNewOrbiter = false;
		density = BLACK_HOLE_DENSITY;
		this.grows = grows;
		isConsumable = true;
	}

	@Override
	public void update(float delta, float worldWidth, float worldHeight) {
		// clear delete list
		piecesToDelete.clear();
	}

	@Override
	public void updateGravity(GamePiece gamePiece) {
		if (gamePiece.isConsumable) {
			// minimum distance between centres (adding both radius' together)
			float minDistanceBetweenCentresSquared = (gamePiece.radius + radius)
					* (gamePiece.radius + radius);
			// current distance between centres
			float distanceBetweenCentresSquared = Vector2d.fromPoints(
					gamePiece.position.x, gamePiece.position.y, position.x,
					position.y).getMagnitudeSquared();
			// collision detection
			if (distanceBetweenCentresSquared < minDistanceBetweenCentresSquared) {
				// reduce the size of the piece appropriately
				float sizeReduction = (float) (Math
						.sqrt(distanceBetweenCentresSquared) - Math
						.sqrt(minDistanceBetweenCentresSquared));

				// increase by the required amount

				// if both pieces are black holes then the bigger eats the
				// smaller
				if (!gamePiece.canDelete
						|| (gamePiece.canDelete && (this.radius >= gamePiece.radius))) {

					if (grows) {
						// this piece consumes those it touches
						increaseSizeByMass(sizeReduction, gamePiece.radius,
								gamePiece.density);
					}
					// change the size of the colliding piece
					if (!gamePiece.changeSize(sizeReduction)) {
						// add to delete list if it is below minimum size
						piecesToDelete.add(gamePiece);
					}
				}
			}
		}
	}

	@Override
	public ArrayList<GamePiece> getDeleteList() {
		return piecesToDelete;
	}

	@Override
	/**
	 * also changes diameter and mass parameter, can't go below BLACK_HOLE_MIN_DIAMETER / 2f
	 * @param newRadius
	 */
	public void setSizeByRadius(float newRadius) {
		if (newRadius < (BLACK_HOLE_MIN_DIAMETER / 2f)) {
			newRadius = BLACK_HOLE_MIN_DIAMETER / 2f;
		} else if (newRadius > BLACK_HOLE_MAX_RADIUS) {
			newRadius = BLACK_HOLE_MAX_RADIUS;
		}
		radius = newRadius;
		diameter = newRadius * 2;
		mass = getMassFromRadius(radius);
	}

	/**
	 * used for stealing mass from other objects and adding to itself taking
	 * into account densities
	 * 
	 * @param sizeReduction
	 * @param radius
	 * @param density
	 */
	private void increaseSizeByMass(float sizeReduction, float targetRadius,
			float targetDensity) {
		// get amount of mass to increase by
		// area = pi*r^2
		// area of reduction = mass of original size - mass of new size
		float originalArea = GameWorld.PI * targetRadius * targetRadius;
		float newRadius = targetRadius + sizeReduction;
		float newArea = (newRadius <= 0f ? 0f : GameWorld.PI * newRadius
				* newRadius);
		float massStolen = (originalArea - newArea) * targetDensity;
		// increase by mass
		// mass = pi*r^2*density
		// r = sqrt.(mass/(pi*density))
		radius += (float) Math.sqrt(massStolen / (GameWorld.PI * density))
				* GROW_FACTOR;
		if (radius > BLACK_HOLE_MAX_RADIUS) {
			radius = BLACK_HOLE_MAX_RADIUS;
		}
		diameter = radius * 2f;
		mass = getMassFromRadius(radius);
	}
}
