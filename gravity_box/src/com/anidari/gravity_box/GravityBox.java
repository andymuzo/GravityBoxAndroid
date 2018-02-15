package com.anidari.gravity_box;

import com.anidari.gravity_box.backgrounds.Background;
import com.anidari.gravity_box.backgrounds.StarFieldBackground;
import com.anidari.gravity_box.controllers.TouchController;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class GravityBox implements ApplicationListener, InputProcessor {

	private GameWorld world;
	private WorldRenderer worldRenderer;
	private TouchController touchController;
	public Background background;

	@Override
	public void create() {
		float w = Gdx.app.getGraphics().getWidth();
		float h = Gdx.app.getGraphics().getHeight();

		world = new GameWorld(w, h);		
		worldRenderer = new WorldRenderer(world, w, h);
		background = new StarFieldBackground(world, 55, worldRenderer.getStarTextureRegion());
		worldRenderer.setBackground(background);
		
		touchController = new TouchController(world, w, h);
		
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void dispose() {
		worldRenderer.disposeAllTextures();
	}

	@Override
	public void render() {
		float delta = Math.min(Gdx.graphics.getDeltaTime(), 0.05f);
		world.update(delta);
		background.update(delta);
		worldRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	// Controller methods ***********************************************

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touchController.addNewTouchDown(screenX, screenY, pointer);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touchController.addNewTouchUp(screenX, screenY, pointer);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		touchController.dragTouch(screenX, screenY, pointer);
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
