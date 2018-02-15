package com.anidari.gravity_box.controllers;

import java.util.HashMap;

import com.anidari.gravity_box.GameWorld;

public class TouchController {

	public static final int NO_TOUCH_ID = -1;

	GameWorld gameWorld;
	private HashMap<Integer, TouchEvent> touchEvents;

	public TouchController(GameWorld gameWorld, float screenWidth,
			float screenHeight) {
		this.gameWorld = gameWorld;
		touchEvents = new HashMap<Integer, TouchEvent>();
	}

	/**
	 * adds a new TouchEvent to the Hash Map
	 * 
	 * @param screenX
	 * @param screenY
	 * @param pointer
	 */
	public void addNewTouchDown(int screenX, int screenY, int pointer) {
		// get world coords
		float worldX = screenToWorldCoordsX(screenX);
		float worldY = screenToWorldCoordsY(screenY);

		// check to see if a menu icon is selected
		if (!gameWorld.menu.select(worldX, worldY)) {
			// if not then create a new touch event
			TouchEvent touchEvent = new TouchEvent(gameWorld.menu.currentAction, worldX, worldY);
			// add it to the hash map
			touchEvents.put(pointer, touchEvent);
			// add the correct game piece to the game world
			gameWorld.gamePieces.add(touchEvent.gamePiece);
			gameWorld.sortPiecesBySize();
		}
	}

	/**
	 * performs the touch up action of the touch event then moves it from the
	 * hash map
	 * 
	 * @param screenX
	 * @param screenY
	 * @param pointer
	 */
	public void addNewTouchUp(int screenX, int screenY, int pointer) {
		// convert to game world coordinates
		float worldX = screenToWorldCoordsX(screenX);
		float worldY = screenToWorldCoordsY(screenY);
		// check to see if the pointer relates to a touch event. It wont if the
		// touch down was on the menu
		if (touchEvents.containsKey(pointer)) {
			touchEvents.get(pointer).touchUp(worldX, worldY, gameWorld);
			touchEvents.remove(pointer);
		}
	}

	public void dragTouch(int screenX, int screenY, int pointer) {
		// convert to game world coordinates
		float worldX = screenToWorldCoordsX(screenX);
		float worldY = screenToWorldCoordsY(screenY);
		// check to see if the pointer relates to a touch event. It wont if the
		// touch down was on the menu
		if (touchEvents.containsKey(pointer)) {
			touchEvents.get(pointer).touchDrag(worldX, worldY, gameWorld);
		}
	}

	public float screenToWorldCoordsX(int screenX) {
		float worldX = screenX * 0.5f;
		worldX -= gameWorld.worldWidth;
		return worldX;
	}

	public float screenToWorldCoordsY(int screenY) {
		float worldY = screenY * -0.5f;
		worldY += gameWorld.worldHeight;
		return worldY;
	}
}
