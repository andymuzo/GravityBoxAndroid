package com.anidari.gravity_box;

import java.util.ArrayList;

import com.anidari.gravity_box.menu.Menu;
import com.anidari.gravity_box.pieces.GamePiece;
import com.anidari.gravity_box.pieces.StandardOrbiter;
import com.anidari.gravity_box.tools.Vector2d;
import com.badlogic.gdx.Gdx;

public class GameWorld {

	public static final float PI = 3.141592654f;

	public static final int STATE_RUNNING = 0;
	public static final int STATE_PAUSED = 1;
	public static final int STATE_MENU = 2;
	public static final int STATE_READY = 3;
	public static final int STATE_LEVEL_END = 4;
	public static final int STATE_GAME_OVER = 5;

	public static float minPieceSize = 5f;
	public static float maxPieceSize = 30f;

	public static int maxGamePieces = 120;
	public int antiGravPieces;

	public ArrayList<GamePiece> gamePieces;
	public ArrayList<GamePiece> piecesToDelete;
	public ArrayList<GamePiece> piecesToAdd;
	int level;
	public float worldWidth;
	public float worldHeight;

	public Menu menu;
	public WorldGravity worldGravity;
	
	public int state;

	public float lastDelta;

	/**
	 * creates a test world, eventually will need to load levels properly
	 */
	public GameWorld(float cameraWidth, float cameraHeight) {

		// initialise world dimensions
		this.worldWidth = cameraWidth / 4f; // +/- this from origin for frame of
											// world
		this.worldHeight = cameraHeight / 4f;

		// populate the game pieces
		gamePieces = new ArrayList<GamePiece>();
		// create the empty delete list
		piecesToDelete = new ArrayList<GamePiece>();
		// create the empty add new list
		piecesToAdd = new ArrayList<GamePiece>();

		level = 1;

		getPiecesForLevel(level);

		// initialise the menu
		menu = new Menu(this, worldWidth, worldHeight);

		// initialise the world gravity variables
		worldGravity = new WorldGravity(menu);
		
		lastDelta = 0.1f;

		state = STATE_READY;
	}

	public void update(float delta) {

		switch (state) {
		case STATE_READY:
			updateReady();
			break;
		case STATE_RUNNING:
			updateRunning(delta);
			break;
		case STATE_PAUSED:
			updatePaused();
			break;
		default:
			state = STATE_READY;
			break;
		}
	}
	
	/**
	 * waits until its been touched then un-pauses
	 */
	private void updateReady() {
		if (Gdx.input.justTouched()) {
			state = STATE_RUNNING;
		}
	}

	private void updateRunning(float delta) {
		lastDelta = delta;

		worldGravity.updateGravConstant(delta);

		worldGravity.updateWorldGravity(delta);

		// update gravity
		for (GamePiece piece : gamePieces) {
			for (int i = 0; i < gamePieces.size(); i++) {
				if (piece != gamePieces.get(i) && piece.isActive
						&& gamePieces.get(i).isActive) {
					piece.updateGravity(gamePieces.get(i));
				}
			}
			// get the list of pieces to be deleted by the current piece
			if (piece.canDelete) {
				piecesToDelete.addAll(piece.getDeleteList());
			}
			if (piece.addNewOrbiter) {
				piecesToAdd.add(getRandomOrbiter(piece.position.x,
						piece.position.y, piece.velocity.x, piece.velocity.y));
			}
		}

		// delete those needed
		gamePieces.removeAll(piecesToDelete);
		piecesToDelete.clear();

		// add those needed
		if (!piecesToAdd.isEmpty()) {
			gamePieces.addAll(piecesToAdd);
			piecesToAdd.clear();
		}

		// update position
		for (GamePiece piece : gamePieces) {
			piece.update(delta, this.worldWidth, this.worldHeight);
		}
	}

	
	private void updatePaused() {
		// quit or continue buttons will render
		// collision detection on them to determine action
		
		// for now touch to resume
		if (Gdx.input.justTouched()) {
			state = STATE_RUNNING;
		}
	}

	public void addRandomOrbiter(ArrayList<GamePiece> pieces) {
		float diameter = (((float) Math.random()) * (maxPieceSize - minPieceSize))
				+ minPieceSize;
		pieces.add(new StandardOrbiter(
				new Vector2d(
						((float) Math.random() * (2f * (worldWidth - (diameter * 2f))))
								- worldWidth + (diameter * 2f),
						((float) Math.random() * (2f * (worldHeight - (diameter * 2f))))
								- worldHeight + (diameter * 2f)), new Vector2d(
						(float) Math.random() * 30f,
						(float) Math.random() * 30f), diameter));
	}

	/**
	 * adds a new orbiter to the main array at the given positions
	 * 
	 * @param positionX
	 * @param positionY
	 */
	public void addRandomOrbiter(float positionX, float positionY) {
		gamePieces.add(getRandomOrbiter(positionX, positionY));
	}

	public StandardOrbiter getRandomOrbiter(float positionX, float positionY) {
		float diameter = (((float) Math.random()) * (maxPieceSize - minPieceSize))
				+ minPieceSize;
		return new StandardOrbiter(new Vector2d(positionX, positionY),
				new Vector2d((float) Math.random() * 30f,
						(float) Math.random() * 30f), diameter);
	}

	public StandardOrbiter getRandomOrbiter(float positionX, float positionY,
			float velocityX, float velocityY) {
		float diameter = (((float) Math.random()) * (maxPieceSize - minPieceSize))
				+ minPieceSize;
		return new StandardOrbiter(new Vector2d(positionX, positionY),
				new Vector2d(velocityX, velocityY), diameter);
	}

	public void addRandomAntiOrbiter(ArrayList<GamePiece> pieces) {
		float diameter = (((float) Math.random()) * -1f * (maxPieceSize - minPieceSize))
				- minPieceSize;
		float absDiameter = Math.abs(diameter);
		pieces.add(new StandardOrbiter(
				new Vector2d(
						((float) Math.random() * (2f * (worldWidth - (absDiameter * 2f))))
								- worldWidth + (absDiameter * 2f),
						((float) Math.random() * (2f * (worldHeight - (absDiameter * 2f))))
								- worldHeight + (absDiameter * 2f)),
				new Vector2d((float) Math.random() * 30f,
						(float) Math.random() * 30f), diameter));
	}

	public ArrayList<GamePiece> sortBySize(ArrayList<GamePiece> pieces) {

		ArrayList<GamePiece> tempPieces = new ArrayList<GamePiece>();
		// order into size then add to the game pieces array
		while (pieces.size() > 0) {
			float maxSize = 0f;
			GamePiece biggestOrb = pieces.get(0);
			for (GamePiece orb : pieces) {
				if (orb.radius >= maxSize) {
					maxSize = orb.radius;
					biggestOrb = orb;
				}
			}

			tempPieces.add(biggestOrb);
			pieces.remove(biggestOrb);
		}

		return tempPieces;
	}

	public void sortPiecesBySize() {
		ArrayList<GamePiece> tempPieces = new ArrayList<GamePiece>();
		// order into size then add to the game pieces array
		while (gamePieces.size() > 0) {
			float maxSize = 0f;
			GamePiece biggestOrb = gamePieces.get(0);
			for (GamePiece orb : gamePieces) {
				if (orb.radius >= maxSize) {
					maxSize = orb.radius;
					biggestOrb = orb;
				}
			}

			tempPieces.add(biggestOrb);
			gamePieces.remove(biggestOrb);
		}

		gamePieces = tempPieces;

	}

	/**
	 * populates the gamePieces array list depending on level. Eventually when
	 * level isn't appropriate will fetch patterns of enemies
	 * 
	 */
	public void getPiecesForLevel(int level) {

		switch (level) {
		case 0:
			loadTestLevel();
			break;
		default:
			// load blank sandbox
			loadSandboxLevel();
			break;
		}

	}

	public void loadSandboxLevel() {
		addRandomOrbiter(gamePieces);
		addRandomOrbiter(gamePieces);
		gamePieces = sortBySize(gamePieces);
	}

	public void loadTestLevel() {
		antiGravPieces = (int) (Math.random() * maxGamePieces);

		int numOfPieces = (int) maxGamePieces - antiGravPieces;

		ArrayList<GamePiece> tempPieces = new ArrayList<GamePiece>();

		for (int i = 0; i < numOfPieces; i++) {
			float diameter = ((float) Math.random() * 38f) + 8f;
			tempPieces
					.add(new StandardOrbiter(new Vector2d(((float) Math
							.random() * (2f * (worldWidth - (diameter * 2f))))
							- worldWidth + (diameter * 2), ((float) Math
							.random() * (2f * (worldHeight - (diameter * 2f))))
							- worldHeight + (diameter * 2)), new Vector2d(
							(float) Math.random() * 30f,
							(float) Math.random() * 30f), diameter));
		}

		for (int i = 0; i < antiGravPieces; i++) {
			float diameter = ((float) Math.random() * -38f) - 8f;
			float absDiameter = Math.abs(diameter);
			tempPieces
					.add(new StandardOrbiter(
							new Vector2d(
									((float) Math.random() * (2f * (worldWidth - (absDiameter * 2f))))
											- worldWidth + (absDiameter * 2),
									((float) Math.random() * (2f * (worldHeight - (absDiameter * 2f))))
											- worldHeight + (absDiameter * 2)),
							new Vector2d((float) Math.random() * 30f,
									(float) Math.random() * 30f), diameter));
		}

		// order into size then add to the game pieces array
		while (tempPieces.size() > 0) {
			float maxSize = 0f;
			GamePiece biggestOrb = tempPieces.get(0);
			for (GamePiece orb : tempPieces) {
				if (orb.radius >= maxSize) {
					maxSize = orb.radius;
					biggestOrb = orb;
				}
			}

			gamePieces.add(biggestOrb);
			tempPieces.remove(biggestOrb);
		}
	}
}
