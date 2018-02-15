package com.anidari.gravity_box.pieces;

import com.anidari.gravity_box.GameWorld;
import com.anidari.gravity_box.WorldGravity;
import com.anidari.gravity_box.tools.Vector2d;

public class StandardOrbiter extends GamePiece {

	public static final float ORBITER_DENSITY = 0.3f;

	public float bounceDamp;
	private Vector2d pointOfOrigin;
	private float targetRadius;

	/**
	 * Constructor for creating a standard orbiter, mass is calculated from
	 * diameter this constructor takes note of the starting point and grows the
	 * orbiter to its target diameter as it moves away use other constructor for
	 * anti-matter
	 * 
	 * @param position
	 * @param speed
	 * @param diameter
	 */
	public StandardOrbiter(Vector2d position, Vector2d speed, float diameter) {
		this.pointOfOrigin = new Vector2d(position.x, position.y);
		this.position = position;
		this.velocity = speed;
		this.targetRadius = Math.abs(diameter) / 2f;
		// starting diameter must be minimum allowable
		this.diameter = GameWorld.minPieceSize;
		this.mass = this.getMassFromDiameter(this.diameter);
		this.radius = this.diameter / 2;
		this.colour = (mass > 0 ? (int) (Math.random() * 7) : (int) (Math
				.random() * 2) + 7);
		this.bounceDamp = 0.8f;
		canDelete = false;
		addNewOrbiter = false;
		this.isActive = true;
		this.density = ORBITER_DENSITY;
		isConsumable = true;
	}

	/**
	 * constructor for creating an initially inactive orbiter
	 * 
	 * @param position
	 * @param speed
	 * @param diameter
	 *            passing a negative creates an anti-matter piece
	 * @param isActive
	 */
	public StandardOrbiter(Vector2d position, Vector2d speed, float diameter,
			boolean isActive) {
		this.pointOfOrigin = null;
		this.position = position;
		this.velocity = speed;
		this.diameter = Math.abs(diameter);
		this.mass = this.getMassFromDiameter(this.diameter)
				* (diameter > 0 ? 1f : -1f);
		this.radius = this.diameter / 2;
		this.colour = (mass > 0 ? (int) (Math.random() * 7) : (int) (Math
				.random() * 2) + 7);
		this.bounceDamp = 0.8f;
		canDelete = false;
		addNewOrbiter = false;
		this.isActive = isActive;
		this.density = ORBITER_DENSITY;
	}

	@Override
	public void update(float delta, float worldWidth, float worldHeight) {
		if (isActive) {
			// adjusts for world gravity
			if (WorldGravity.worldGravityYOn) {
				this.velocity.y -= WorldGravity.worldGravityY;
			}
			if (WorldGravity.worldGravityXOn) {
				this.velocity.x -= WorldGravity.worldGravityX;
			}

			// moves ball according to speed vector
			this.position.addVectorByScalar(velocity, delta);
			// boundary checking
			if (this.position.x + this.radius > worldWidth) {
				// right boundary
				this.velocity.makeXNegative();
				this.position.addX(this.velocity.x * delta);
				this.velocity.scaleX(this.bounceDamp);
			} else if (this.position.x - this.radius < -worldWidth) {
				// left boundary
				this.velocity.makeXPositive();
				this.position.addX(this.velocity.x * delta);
				this.velocity.scaleX(this.bounceDamp);
			}
			
			if (this.position.y + this.radius > worldHeight) {
				// top boundary
				this.velocity.makeYNegative();
				this.position.addY(this.velocity.y * delta);
				this.velocity.scaleY(this.bounceDamp);
			} else if (this.position.y - this.radius < -worldHeight) {
				// bottom boundary
				this.velocity.makeYPositive();
				this.position.addY(this.velocity.y * delta);
				this.velocity.scaleY(this.bounceDamp);
			}

			// grows the orbiter if it needs to
			if (pointOfOrigin != null) {
				// calculate the distance between origin and current position
				float distanceBetweenPoints = Vector2d.fromPoints(
						pointOfOrigin.x, pointOfOrigin.y, position.x,
						position.y).getMagnitude();
				// check to see if the orbiter is up to its target size
				if (distanceBetweenPoints >= targetRadius) {
					distanceBetweenPoints = targetRadius;
					pointOfOrigin = null;
				}
				// reduce the size of the piece appropriately
				setSizeByRadius(distanceBetweenPoints);
			}
		}
	}

	/**
	 * called every frame for all objects that move with reference to gravity
	 * populates the acceleration vector
	 */
	public void updateGravity(GamePiece gamePiece) {
		// Fg = G(m1*m2)/d*d

		// calculates the force based on absolute distance then applied to the
		// direction components
		float xDistance = gamePiece.position.x - this.position.getX();
		float yDistance = gamePiece.position.y - this.position.getY();

		float distanceSquared = (xDistance * xDistance)
				+ (yDistance * yDistance);

		float combinedRadiusSquared = (this.radius + gamePiece.radius)
				* (this.radius + gamePiece.radius);
		float gravityAttract = 0f;

		// avoids a divide by zero
		if (distanceSquared != 0f) {
			if (distanceSquared <= combinedRadiusSquared) {
				// a quick check to stop crazy gravity happening at very close
				// range
				// this happens when they overlap, may never get called if
				// collision
				// is implemented
				float distanceMultiplier = distanceSquared
						/ combinedRadiusSquared;
				gravityAttract = (gamePiece.mass * WorldGravity.G * distanceMultiplier)
						/ distanceSquared;
			} else {
				// below used to include the mass of the object but this is
				// cancelled out
				// when it is later divided by the mass of the object to work
				// out
				// the acceleration
				// after inertia
				gravityAttract = (gamePiece.mass * WorldGravity.G)
						/ distanceSquared;
			}

			// applying the force in the proper directions
			float xComponent = (Math.abs(xDistance) / (Math.abs(xDistance) + Math
					.abs(yDistance))) * gravityAttract;
			float yComponent = gravityAttract - xComponent;

			if (this.mass < 0f) {
				xComponent *= -1;
				yComponent *= -1;
			}

			// updating the velocity based on which direction it must be applied
			if (this.position.getX() > gamePiece.position.x) {
				this.velocity.minusX(xComponent);
			} else {
				this.velocity.addX(xComponent);
			}

			if (this.position.getY() > gamePiece.position.y) {
				this.velocity.minusY(yComponent);
			} else {
				this.velocity.addY(yComponent);
			}
		}
	}

}
