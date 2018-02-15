package com.anidari.gravity_box.backgrounds;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Background {
	
	/**
	 * called to draw the contents of the background
	 * @param spriteBatch
	 */
	public abstract void render(SpriteBatch spriteBatch);
	
	/**
	 * called to update the background details
	 * @param tiltX
	 * @param tiltY
	 * @param delta
	 */
	public abstract void update(float delta);
	
}
