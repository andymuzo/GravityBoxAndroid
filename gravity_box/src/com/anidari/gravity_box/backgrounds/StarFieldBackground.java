package com.anidari.gravity_box.backgrounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.anidari.gravity_box.GameWorld;
import com.anidari.gravity_box.WorldGravity;
import com.anidari.gravity_box.tools.Vector2d;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Collections;

public class StarFieldBackground extends Background {

	private List<Star> stars;
	private Random rand;
	private GameWorld gameWorld;
	private TextureRegion starTexture;

	public StarFieldBackground(GameWorld gameWorld, int amountOfStars,
			TextureRegion starTexture) {
		this.gameWorld = gameWorld;
		// create a load of randomly positioned stars
		rand = new Random();
		stars = new ArrayList<Star>();
		this.starTexture = starTexture;
		populateStars(amountOfStars);
	}

	private void populateStars(int amountOfStars) {
		// add all the stars to the right places
		for (int i = 0; i < amountOfStars; i++) {
			stars.add(new Star(gameWorld, new Vector2d(
					(rand.nextFloat() - 0.5f) * gameWorld.worldWidth * 2f,
					(rand.nextFloat() - 0.5f) * gameWorld.worldHeight * 2f),
					rand.nextFloat() * Star.MAX_DEPTH, 
					new Vector2d((rand.nextFloat() - 0.5f) * Star.MAX_SPEED, 
							(rand.nextFloat() - 0.5f) * Star.MAX_SPEED)));
		}
		Collections.sort(stars);
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		for (Star star : stars) {
			spriteBatch.draw(starTexture, star.getPosition().x,
					star.getPosition().y, star.getSize(), star.getSize());
		}
	}

	@Override
	public void update(float delta) {
		// update the tilt movement
		float tiltX = (WorldGravity.worldGravityXOn ? WorldGravity.worldGravityX * 2f : 0f);
		float tiltY = (WorldGravity.worldGravityYOn ? WorldGravity.worldGravityY * 2f : 0f);
	
		// update normal movement
		for (Star star : stars) {
			star.update(tiltX, tiltY, delta);
		}
	}

}
