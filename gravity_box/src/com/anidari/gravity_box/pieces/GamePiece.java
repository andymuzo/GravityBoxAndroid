package com.anidari.gravity_box.pieces;

import java.util.ArrayList;

import com.anidari.gravity_box.GameWorld;
import com.anidari.gravity_box.tools.Vector2d;

public abstract class GamePiece {
	
	public static final int NO_COLOUR = -1;
	public float density = 0.2f;
	
	public Vector2d position;
	public Vector2d velocity;
	public float mass;
	public int colour;
	public float radius; // used for calculations
	public float diameter; // used for rendering
	public boolean isActive;
	public boolean canDelete;
	public boolean addNewOrbiter;
	public boolean isConsumable; // decides whether a black hole will destroy it or not, doesn't effect Remover
	
	public abstract void update(float delta, float worldWidth, float worldHeight);
	public abstract void updateGravity(GamePiece gamePiece);
	
	public void destruct() {
		
	}

	public void activate() {
		
	}
	
	public void deactivate() {
		
	}
	
	public void moveTo(float x, float y) {
		this.position.x = x;
		this.position.y = y;
	}
	
	/**
	 * returns null unless there is something to delete
	 * @return
	 */
	public ArrayList<GamePiece> getDeleteList() {
		return null;
	}
	
	/**
	 * changes the diameter, radius and mass
	 * @return false if new size is smaller than minimum. It will still make the change even if false is returned.
	 * @param diameterIncrease
	 */
	public boolean changeSize(float diameterIncrease) {
		this.diameter += diameterIncrease;
		this.radius += diameterIncrease / 2f;
		this.mass = getMassFromDiameter(diameter);
		
		return diameter >= GameWorld.minPieceSize;
	}
	
	/**
	 * updates diameter and mass too
	 * @param newRadius
	 */
	public void setSizeByRadius(float newRadius) {
		this.diameter = newRadius * 2f;
		this.radius = newRadius;
		this.mass = getMassFromDiameter(diameter);
	}
	
	/**
	 * works out the area and hence the mass of the circle
	 */
	public float getMassFromDiameter(float diameter) {
		// work out area
		// pi * r^2
		float area = GameWorld.PI * (diameter / 2) * (diameter / 2);
		// times by mass per area
		float mass = area * density;		
		
		return mass;
	}
	
	/**
	 * works out the area and hence the mass of the circle
	 */
	public float getMassFromRadius(float radius) {
		// work out area
		// pi * r^2
		float area = GameWorld.PI * radius * radius;
		// times by mass per area
		float mass = area * density;		
		
		return mass;
	}
}
