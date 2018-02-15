package com.anidari.gravity_box.menu;

import java.util.ArrayList;

import com.anidari.gravity_box.GameWorld;

/**
 * holds the menu icons and the currently selected settings and tools
 * @author ajrog_000
 *
 */
public class Menu {
	
	// the classes of icons that are shown all the time unexpanded
	public static enum MENU_ICONS {
		INTERACTION, PIECES, WHITE_HOLES, BLACK_HOLES, GOALS, SHOOTERS, WORLD_GRAVITY, SETTING_MENU
	}
	
	// these types of the main icons expand when the icons are held down
	// INTERACTION mode
	public final int I_EXPLODE = 0;
	public final int I_GRAV_WELL = 1; 
	public final int I_GRAV_HILL = 2;
	public final int I_REMOVER = 3;
	public final int I_MOVER = 4;

	// PIECES mode
	public final int P_RANDOM_ORBITER = 0;
	public final int P_RANDOM_ANTI_MATTER = 1;
	public final int P_TYPE_0 = 2;
	public final int P_TYPE_1 = 3;
	public final int P_TYPE_2 = 4;
	public final int P_TYPE_3 = 5;
	public final int P_TYPE_4 = 6;
	public final int P_TYPE_5 = 7;
	public final int P_TYPE_6 = 8;
	public final int P_TYPE_7 = 9;
	public final int P_TYPE_8 = 10;
	
	// WHITE_HOLE mode
	public final int WH_RANDOM_ORBITER = 0;
	public final int WH_RANDOM_ANTI_MATTER = 1;
	public final int WH_TYPE_0 = 2;
	public final int WH_TYPE_1 = 3;
	public final int WH_TYPE_2 = 4;
	public final int WH_TYPE_3 = 5;
	public final int WH_TYPE_4 = 6;
	public final int WH_TYPE_5 = 7;
	public final int WH_TYPE_6 = 8;
	public final int WH_TYPE_7 = 9;
	public final int WH_TYPE_8 = 10;
	
	// BLACK_HOLE mode
	public final int BH_STATIC = 0;
	public final int BH_MOVING = 1;
	public final int BH_GROWING_STATIC = 2;
	public final int BH_GROWING_MOVING = 3;
	
	// GOAL mode
	public final int G_OMNI = 0;
	public final int G_TYPE_0 = 1;
	public final int G_TYPE_1 = 2;
	public final int G_TYPE_2 = 3;
	public final int G_TYPE_3 = 4;
	public final int G_TYPE_4 = 5;
	public final int G_TYPE_5 = 6;
	public final int G_TYPE_6 = 7;
	public final int G_TYPE_7 = 8;
	public final int G_TYPE_8 = 9;
	
	// SHOOTER mode
	public final int S_OMNI = 0;
	public final int S_TYPE_0 = 1;
	public final int S_TYPE_1 = 2;
	public final int S_TYPE_2 = 3;
	public final int S_TYPE_3 = 4;
	public final int S_TYPE_4 = 5;
	public final int S_TYPE_5 = 6;
	public final int S_TYPE_6 = 7;
	public final int S_TYPE_7 = 8;
	public final int S_TYPE_8 = 9;
	
	// WORLD_GRAVITY mode
	public final int WG_TILT = 0;
	public final int WG_OFF = 1;
	public final int WG_GRADUAL_CHANGE = 2;
	public final int WG_RANDOM = 3;
	
	// OPTIONS mode
	public final int O_BACKGROUND = 0;
	public final int O_BLEND_FUNCTION = 1;
	
	public ArrayList<MenuIcon> icons;
	// this holds whatever action is currently selected
	public MenuIcon currentAction;
	// used by the renderer to get the current blend function
	public MenuIcon blendFunction;
	// used by the world to decide the world grav settings
	public MenuIcon worldGravity;
	// the size of the menu in world units, it automatically fills the landscape height
	public float size;
	
	public GameWorld gameWorld;
	
	/**
	 * the worldHeight is used to decide how big the menu needs to be to stay in proportion
	 * @param worldHeight measured in distance from centre to top of screen in landscape
	 */
	public Menu(GameWorld gameWorld, float worldWidth, float worldHeight) {
		
		this.gameWorld = gameWorld;
		// needs to fit an icon for each value in ACTION
		size = worldHeight * 2f / (float) MENU_ICONS.values().length;
		
		icons = new ArrayList<MenuIcon>();
		
		float xPos = worldWidth * -1;
		int i = 0;
		for (MENU_ICONS menuIcons : MENU_ICONS.values()) {
			icons.add(new MenuIcon(gameWorld, menuIcons, size, xPos, 
					(worldHeight * -1f) + ((float) i++ * size), false));
		}

		// set current action
		activate(MENU_ICONS.INTERACTION);
		
		// finds the blend function icon
		for (MenuIcon icon : icons) {
			if (icon.menuCategory == MENU_ICONS.SETTING_MENU) {
				blendFunction = icon;
			}
			if (icon.menuCategory == MENU_ICONS.WORLD_GRAVITY) {
				worldGravity = icon;
			}
		}
	}
	
	/**
	 *  activates an menu icon using enum value
	 * @param action
	 */
	public void activate(MENU_ICONS action) {
		for (MenuIcon icon : icons) {
			if (icon.menuCategory == action) {
				if (icon.pressButton()) {
					currentAction = icon;
					currentAction.isActive = true;
				}
				
			}
		}
	}
	
	/**
	 * checks for collision with the menu icons and activates the appropriate one
	 * @param x world coordinates
	 * @param y world coordinates
	 * @return false if nothing is selected
	 */
	public boolean select(float x, float y) {
		MenuIcon collidedIcon = null;
		// because the x position is the same for them all
		if (x < icons.get(0).xPos + icons.get(0).size) {
			for (MenuIcon icon : icons) {
				if (y < icon.yPos + size && y > icon.yPos) {
					// collides with the icon
					collidedIcon = icon;
				}
			}
		}
		
		// selects the right one
		if (collidedIcon != null) {
			if (collidedIcon.pressButton()) {
				clearSelections();
				currentAction = collidedIcon;
				currentAction.isActive = true;
			}
			return true;
		} else return false;	
	}
	
	/**
	 * deactivates all icons, doesn't clear the currentAction variable though
	 */
	public void clearSelections() {
		for (MenuIcon icon : icons) {
			icon.isActive = false;
		}
	}
}
