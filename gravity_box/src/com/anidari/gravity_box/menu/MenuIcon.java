package com.anidari.gravity_box.menu;

import com.anidari.gravity_box.GameWorld;
import com.anidari.gravity_box.WorldRenderer;
import com.anidari.gravity_box.menu.Menu.MENU_ICONS;

/**
 * contains the state of each individual icon button
 * @author ajrog_000
 *
 */
public class MenuIcon {
	public GameWorld gameWorld;
	public MENU_ICONS menuCategory;
	public float size;
	public float xPos;
	public float yPos;
	public boolean isActive;
	public boolean isExpanded;
	public int subMenuItems;
	public int mode; // any action could have a number of different modes that
						// dictate how it works, mode numbers are defined in Menu class

	public MenuIcon(GameWorld gameWorld, MENU_ICONS menuCategory, float size, float xPos,
			float yPos, boolean isActive) {
		this.gameWorld = gameWorld;
		this.menuCategory = menuCategory;
		this.size = size;
		this.xPos = xPos;
		this.yPos = yPos;
		this.isActive = isActive;
		this.isExpanded = false;
		this.mode = 0; // initialised, will be set when activated
		setNumberOfSubMenuItems();
	}

	/**
	 * this has a number of effects depending on the button type, could
	 * activate, could toggle
	 * 
	 * @return flags true if it should be set to the active icon and the others
	 *         deactivated
	 */
	public boolean pressButton() {
		boolean isExclusive = true;
		switch (menuCategory) {
		case INTERACTION: 
		case PIECES: 
		case WHITE_HOLES: 
		case BLACK_HOLES: 
		case GOALS: 
		case SHOOTERS: 
			break;
		case WORLD_GRAVITY:
		case SETTING_MENU:
			isExclusive = false;
			break;
		default:
			break;
		}
		return isExclusive;
	}
	
	/**
	 * expands the button and shows what is currently selected
	 * highlights the
	 * @return
	 */
	public boolean holdButton() {
		this.isExpanded = true;
		boolean isExclusive = true;
		return isExclusive;
	}

	private void nextRenderer() {
		mode = (mode + 1) % WorldRenderer.NUMBER_OF_BLEND_FUNCTIONS;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	private void setNumberOfSubMenuItems() {
		
		switch (menuCategory) {
		case INTERACTION: 
			subMenuItems = 5;
		case PIECES: 
			subMenuItems = 11;
		case WHITE_HOLES: 
			subMenuItems = 11;
		case BLACK_HOLES: 
			subMenuItems = 4;
		case GOALS: 
			subMenuItems = 10;
		case SHOOTERS: 
			subMenuItems = 9;
		case WORLD_GRAVITY:
			subMenuItems = 4;
		case SETTING_MENU:
			subMenuItems = 1;
		default:
			subMenuItems = 0;
		}
	}
}