package com.anidari.gravity_box;

import com.anidari.gravity_box.menu.Menu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;

/**
 * 
 * @author ajrog_000
 * 
 */
public class WorldGravity {

	public static float G = 14f;// gravitational strength
	public static boolean IS_G_CHANGING = false;// gravitational strength

	public static final int WORLD_GRAV_MAX_OPTIONS = 4;

	public static final float gravMax = 14f;
	public static final float gravMin = 10f;

	public static float worldGravityY = 0f;
	public static float worldGravityX = 0f;
	public static boolean worldGravityYOn = true;
	public static boolean worldGravityXOn = true;
	public static final float worldGravMax = 5f;
	public static final float worldGravMin = -5f;

	boolean increasingGrav;

	boolean increasingWorldGravY;
	float worldGravYOnTimer;
	float worldGravYOnTarget;
	float maxGravYTimer;

	boolean increasingWorldGravX;
	float worldGravXOnTimer;
	float worldGravXOnTarget;
	float maxGravXTimer;

	boolean tiltAvailable;
	boolean tiltSensorOn;

	public Menu menu;

	public WorldGravity(Menu menu) {
		increasingGrav = true;
		increasingWorldGravY = true;
		worldGravYOnTimer = 0;
		maxGravYTimer = 30;
		worldGravYOnTarget = (int) Math.random() * maxGravYTimer;

		increasingWorldGravX = true;
		worldGravXOnTimer = 0;
		maxGravXTimer = 25;
		worldGravXOnTarget = (int) Math.random() * maxGravXTimer;

		tiltAvailable = Gdx.input
				.isPeripheralAvailable(Peripheral.Accelerometer);
		tiltSensorOn = false;

		this.menu = menu;
	}

	public void updateWorldGravity(float delta) {
		switch (menu.worldGravity.mode) {
		case 0:
			// off
			worldGravityYOn = false;
			worldGravityXOn = false;
			tiltSensorOn = false;
			break;
		case 1:
			// on
			worldGravityYOn = true;
			worldGravityXOn = true;
			tiltSensorOn = false;
			break;
		case 2:
			// intermittent
			// flips the world gravity on or off
			updateWorldGravTimer(delta);
			tiltSensorOn = false;
			break;
		default:
			worldGravityYOn = true;
			worldGravityXOn = true;
			tiltSensorOn = true;
			break;
		}
		// update the direction of gravity
		updateGravityPullDirection(delta);
	}

	private void updateWorldGravTimer(float delta) {
		// Y axis ***********************************
		// sets the target timer is target is reached
		worldGravYOnTimer += delta;
		if (worldGravYOnTimer >= worldGravYOnTarget) {
			worldGravYOnTarget = ((float) (Math.random() * maxGravYTimer))
					+ (worldGravityYOn ? 10f : 0f);
			worldGravYOnTimer = 0;
			worldGravityYOn = !worldGravityYOn;
		}
		// X axis ***********************************
		// sets the target timer is target is reached
		worldGravXOnTimer += delta;
		if (worldGravXOnTimer >= worldGravXOnTarget) {
			worldGravXOnTarget = ((float) (Math.random() * maxGravXTimer))
					+ (worldGravityXOn ? 10f : 0f);
			worldGravXOnTimer = 0;
			worldGravityXOn = !worldGravityXOn;
		}
	}

	private void updateGravityPullDirection(float delta) {
		// update world gravity
		// update via tilt sensor if activated, otherwise via the global changes
		if (tiltSensorOn) {
			if (tiltAvailable) {
				// tilt controls here
				worldGravityY = Gdx.input.getAccelerometerX() / 2f;
				worldGravityX = Gdx.input.getAccelerometerY() / -2f;
			} else {
				// keyboard controls here
				worldGravityX = 0f;
				worldGravityY = 0f;
				if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT))
					worldGravityX = 5f;
				if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT))
					worldGravityX = -5f;
				if (Gdx.input.isKeyPressed(Keys.DPAD_UP))
					worldGravityY = -5f;
				if (Gdx.input.isKeyPressed(Keys.DPAD_DOWN))
					worldGravityY = 5f;
			}
		} else {
			// slowly changes from pulling up to pulling down
			if (increasingWorldGravY) {
				worldGravityY += delta / 10;
			} else {
				worldGravityY -= delta / 12;
			}

			// flips the direction of the pull when the max value is reached
			if (worldGravityY > worldGravMax) {
				increasingWorldGravY = false;
			} else if (worldGravityY < worldGravMin) {
				increasingWorldGravY = true;
			}

			// update world gravity
			// slowly changes from pulling up to pulling down
			if (increasingWorldGravX) {
				worldGravityX += delta / 15;
			} else {
				worldGravityX -= delta / 13;
			}

			// flips the direction of the pull when the max value is reached
			if (worldGravityX > worldGravMax) {
				increasingWorldGravX = false;
			} else if (worldGravityX < worldGravMin) {
				increasingWorldGravX = true;
			}
		}
	}

	public void updateGravConstant(float delta) {
		if (IS_G_CHANGING) {
			// update constant
			if (increasingGrav) {
				G += delta / 2f;
			} else {
				G -= delta / 2f;
			}

			if (G > gravMax) {
				increasingGrav = false;
			} else if (G < gravMin) {
				increasingGrav = true;
			}
		}
	}

}
