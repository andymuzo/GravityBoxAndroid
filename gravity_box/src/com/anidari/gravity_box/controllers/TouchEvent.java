package com.anidari.gravity_box.controllers;

import com.anidari.gravity_box.GameWorld;
import com.anidari.gravity_box.menu.MenuIcon;
import com.anidari.gravity_box.pieces.BlackHole;
import com.anidari.gravity_box.pieces.ExploderImploder;
import com.anidari.gravity_box.pieces.GamePiece;
import com.anidari.gravity_box.pieces.GravWellHill;
import com.anidari.gravity_box.pieces.MovingBlackHole;
import com.anidari.gravity_box.pieces.Remover;
import com.anidari.gravity_box.pieces.StandardOrbiter;
import com.anidari.gravity_box.pieces.WhiteHole;
import com.anidari.gravity_box.tools.Vector2d;

/**
 * links the game piece created/selected with the menu icon that was selected at
 * the time of creation, also stores the pointer for the touch down event so the
 * touch up event can locate the right touch item
 * 
 * @author ajrog_000
 * 
 */
public class TouchEvent {
	public GamePiece gamePiece;
	public MenuIcon menuIcon;
	public Vector2d oldPosition;

	/**
	 * used to create the blank touch items in the array
	 */
	public TouchEvent() {
		this.gamePiece = null;
		this.menuIcon = null;
		this.oldPosition = new Vector2d(0f, 0f);
	}

	/**
	 * used to create a fully fledged touch item
	 * 
	 * @param touchPointerId
	 * @param gamePiece
	 * @param menuIcon
	 */
	public TouchEvent(MenuIcon menuIcon, float posX, float posY) {
		this.gamePiece = getGamePiece(posX, posY, menuIcon);
		this.menuIcon = menuIcon;
		this.oldPosition = new Vector2d(posX, posY);
	}

	/**
	 * populate the touchItem with all needed objects, returns the GamePiece
	 * 
	 * @param touchPointerId
	 * @param gamePiece
	 * @param menuIcon
	 */
	public GamePiece TouchDown(MenuIcon menuIcon, float posX, float posY) {
		this.gamePiece = getGamePiece(posX, posY, menuIcon);
		this.menuIcon = menuIcon;

		return gamePiece;
	}

	/**
	 * this returns a game piece of the type determined by the action type of
	 * the button
	 * 
	 * @return
	 */
	private GamePiece getGamePiece(float posX, float posY, MenuIcon menuIcon) {
		GamePiece gamePiece;
		
		switch (menuIcon.menuCategory) {
		case EXPLODE_IMPLODE:
			// short lived high gravity + or -, varies with G to always have an
			// effect
			// mode 0 is explode
			gamePiece = new ExploderImploder(menuIcon.mode == 0, posX, posY);
			break;
		case GRAV_WELL_HILL:
			// permanent gravity + or - in static location
			gamePiece = new GravWellHill(menuIcon.mode == 0, posX, posY);
			break;
		case RANDOM_ORBITER:
			// add new, modes for gravity ball or antigrav ball. Drag to
			// set initial velocity. Size determined in settings
			float diameter = menuIcon.mode == 0 ? (((float) Math.random()) * (GameWorld.maxPieceSize - GameWorld.minPieceSize))
					+ GameWorld.minPieceSize
					: (((float) Math.random()) * -1f * (GameWorld.maxPieceSize - GameWorld.minPieceSize))
							- GameWorld.minPieceSize;
			gamePiece = new StandardOrbiter(new Vector2d(posX, posY),
					new Vector2d(0f, 0f), diameter, false);
			break;
		case REMOVER:
			// tap and or hold, anything colliding with the point of touch gets
			// deleted
			gamePiece = new Remover(posX, posY);
			break;
		case WHITE_HOLE:
			// white hole autonomously creates new balls
			// up to max number defined in options
			gamePiece = new WhiteHole(posX, posY);

			// set the initial position for calculating the starting velocity
			oldPosition = new Vector2d(posX, posY);
			break;
		case BLACK_HOLE:
			// black hole shrinks anything that
			// touches it, deletes it if its below a minimum size
			gamePiece = new BlackHole(posX, posY, false);

			// set the initial position for calculating the size
			oldPosition = new Vector2d(posX, posY);
			break;
		case CURRENT_PLACER:
			// current gives nearby balls a specific velocity in the current
			// direction, hold to rotate?
			gamePiece = new MovingBlackHole(posX, posY, true);

			// gamePiece = new ExploderImploder(menuIcon.mode == 0, posX, posY);
			break;
		default:
			gamePiece = new ExploderImploder(menuIcon.mode == 0, posX, posY);
			break;
		}
		return gamePiece;
	}

	/**
	 * does the right thing depending on the action of the MenuIcon
	 * 
	 * @param posX
	 * @param posY
	 * @param gameWorld
	 */
	public void touchUp(float posX, float posY, GameWorld gameWorld) {
		switch (gameWorld.menu.currentAction.menuCategory) {
		case EXPLODE_IMPLODE:
		case GRAV_WELL_HILL:
			// remove the piece
			gameWorld.gamePieces.remove(gamePiece);
			break;
		case RANDOM_ORBITER:
			// release it after calculating the velocity
			gamePiece.velocity.averageFromPoints(oldPosition.x,
					oldPosition.y, posX, posY, gameWorld.lastDelta);
			gamePiece.moveTo(posX, posY);
			gamePiece.isActive = true;
			break;
		case REMOVER:
			// flag the remover for removal
			gameWorld.gamePieces.remove(gamePiece);
			break;
		case WHITE_HOLE:
			// white hole gets starting velocity for its children orbiters set
			gamePiece.velocity = Vector2d.fromPoints(oldPosition.x,
					oldPosition.y, posX, posY);
			break;
		case BLACK_HOLE:
			// black hole gets resized on drag
			gamePiece.setSizeByRadius(Vector2d.fromPoints(oldPosition.x,
					oldPosition.y, posX, posY).getMagnitude() / 2f);
			break;
		case CURRENT_PLACER:
			// release the location
			gamePiece.setSizeByRadius(Vector2d.fromPoints(oldPosition.x,
					oldPosition.y, posX, posY).getMagnitude() / 2f);
			gamePiece.isActive = true;
			break;
		default:
			break;
		}
	}

	/**
	 * call to drag the touch event
	 * 
	 * @param posX
	 * @param posY
	 * @param gameWorld
	 */
	public void touchDrag(float posX, float posY, GameWorld gameWorld) {
		switch (gameWorld.menu.currentAction.menuCategory) {
		case GRAV_WELL_HILL:
			// move it
			gamePiece.moveTo(posX, posY);
			break;
		case RANDOM_ORBITER:
			// move it and update the last velocity
			oldPosition.x = gamePiece.position.x;
			oldPosition.y = gamePiece.position.y;
			gamePiece.velocity.averageFromPoints(oldPosition.x,
					oldPosition.y, posX, posY, gameWorld.lastDelta);
			gamePiece.moveTo(posX, posY);
			break;
		case REMOVER:
			// move it
			gamePiece.moveTo(posX, posY);
			break;
		case CURRENT_PLACER:
			// move it
			// black hole gets resized on drag
			gamePiece.setSizeByRadius(Vector2d.fromPoints(oldPosition.x,
					oldPosition.y, posX, posY).getMagnitude() / 2f);
			break;
		case WHITE_HOLE:
			// white hole gets starting velocity for its children orbiters set
			gamePiece.velocity = Vector2d.fromPoints(oldPosition.x,
					oldPosition.y, posX, posY);
			break;
		case BLACK_HOLE:
			// black hole gets resized on drag
			gamePiece.setSizeByRadius(Vector2d.fromPoints(oldPosition.x,
					oldPosition.y, posX, posY).getMagnitude() / 2f);
			break;
		default:
			break;
		}
	}
}
