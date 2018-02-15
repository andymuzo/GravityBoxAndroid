package com.anidari.gravity_box;

import com.anidari.gravity_box.backgrounds.Background;
import com.anidari.gravity_box.menu.MenuIcon;
import com.anidari.gravity_box.pieces.GamePiece;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WorldRenderer {

	// creates the reference to gameBoard and the camera
	private GameWorld gameWorld;
	private Background background;
	private OrthographicCamera cam;
	private SpriteBatch spriteBatch;
	public float cameraWidth;
	public float cameraHeight;

	// The following are used to extract the sprite from the texture sheet
	private final static int SQUARE_SIZE = 96; // pixels per block
	private final static int BROKEN_COLUMN = 9;
	private final static int BROKEN_ROW = 2;
	private final static int RING_AMOUNT = 6;
	private final static int RING_ROW = 3;
	private final static int MAIN_COLOUR_AMOUNT = 9;
	private final static int MAIN_COLOUR_COLUMN = 1;
	private final static int MAIN_COLOUR_ROW = 0;
	private final static int SPECIALS_AMOUNT = 16;
	private final static int SPECIALS_ROW_ONE_AMOUNT = 10;
	private final static int SPECIALS_ROW_TWO = 2;
	private final static int ENEMY_COLOURS_AMOUNT = 9;
	private final static int ENEMY_BASE_ROW = 4;
	private final static int ENEMY_HALF_ROW = 6;
	private final static int ENEMY_THIRD_ROW = 7;
	private final static int ENEMY_RIM_ROW = 5;
	private final static int ENEMY_CONNECTORS_AMOUNT = 3;
	private final static int ENEMY_CONNECTORS_ROW = 3;
	private final static int ENEMY_CONNECTORS_COLUMN = 6;
	private final static int ACTIVATED_COLUMN = 6;
	private final static int HIGHLIGHTED_COLUMN = 7;
	private final static int DEPRESSED_BUTTON = 8;
	private final static int CAROUSEL_ROW = 3;
	private final static int CAROUSEL_COLUMN = 9;
	private final static int CAROUSEL_PIXELS = 2;
	private final static int FIX_BUTTON_ROW = 8;
	private final static int FIX_BUTTON_COLUMN = 3;
	private final static int WILD_BUTTON_ROW = 8;
	private final static int WILD_BUTTON_COLUMN = 2;
	private final static int EMPTY_BUTTON_ROW = 8;
	private final static int EMPTY_BUTTON_COLUMN = 0;

	public final static int NUMBER_OF_BLEND_FUNCTIONS = 8;

	private Texture spriteSheet01;
	private TextureRegion[] rings;
	private TextureRegion[] mainColour;
	private TextureRegion[] specials;
	private TextureRegion[] enemyBaseColours;
	private TextureRegion[] enemyHalfColours;
	private TextureRegion[] enemyThirdColours;
	private TextureRegion[] enemyRimColours;
	private TextureRegion[] enemyConnectors;
	private TextureRegion[] carousel;

	public WorldRenderer(GameWorld gameWorld, float cameraWidth,
			float cameraHeight) {
		this.cameraWidth = cameraWidth / 2f;
		this.cameraHeight = cameraHeight / 2f;
		this.gameWorld = gameWorld;
		this.setCam(new OrthographicCamera(this.cameraWidth, this.cameraHeight));
		this.getCam().position.set(0, 0, 0);
		spriteBatch = new SpriteBatch();
		loadTextures();
	}

	private void loadTextures() {
		// loads the sprite sheet
		spriteSheet01 = new Texture("data/buttonsspritesheet02.png");

		new TextureRegion(spriteSheet01, BROKEN_COLUMN * SQUARE_SIZE,
				BROKEN_ROW * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		new TextureRegion(spriteSheet01, EMPTY_BUTTON_COLUMN * SQUARE_SIZE,
				EMPTY_BUTTON_ROW * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		new TextureRegion(spriteSheet01, ACTIVATED_COLUMN * SQUARE_SIZE,
				SPECIALS_ROW_TWO * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		new TextureRegion(spriteSheet01, DEPRESSED_BUTTON * SQUARE_SIZE,
				SPECIALS_ROW_TWO * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		new TextureRegion(spriteSheet01, HIGHLIGHTED_COLUMN * SQUARE_SIZE,
				SPECIALS_ROW_TWO * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		new TextureRegion(spriteSheet01, CAROUSEL_COLUMN * SQUARE_SIZE,
				(CAROUSEL_ROW * SQUARE_SIZE) + CAROUSEL_PIXELS, SQUARE_SIZE,
				CAROUSEL_PIXELS);

		new TextureRegion(spriteSheet01, FIX_BUTTON_COLUMN * SQUARE_SIZE,
				FIX_BUTTON_ROW * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		new TextureRegion(spriteSheet01, WILD_BUTTON_COLUMN * SQUARE_SIZE,
				WILD_BUTTON_ROW * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		carousel = new TextureRegion[MAIN_COLOUR_AMOUNT];
		for (int i = 0; i < MAIN_COLOUR_AMOUNT; i++) {
			carousel[i] = new TextureRegion(spriteSheet01,
					(CAROUSEL_COLUMN * SQUARE_SIZE) + (i * CAROUSEL_PIXELS),
					CAROUSEL_ROW * SQUARE_SIZE, CAROUSEL_PIXELS,
					CAROUSEL_PIXELS);
		}

		rings = new TextureRegion[RING_AMOUNT];
		for (int i = 0; i < RING_AMOUNT; i++) {
			rings[i] = new TextureRegion(spriteSheet01, i * SQUARE_SIZE,
					RING_ROW * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
		}

		mainColour = new TextureRegion[MAIN_COLOUR_AMOUNT];
		for (int i = 0; i < MAIN_COLOUR_AMOUNT; i++) {
			mainColour[i] = new TextureRegion(spriteSheet01,
					(i + MAIN_COLOUR_COLUMN) * SQUARE_SIZE, MAIN_COLOUR_ROW,
					SQUARE_SIZE, SQUARE_SIZE);
		}

		specials = new TextureRegion[SPECIALS_AMOUNT];
		for (int i = 0; i < SPECIALS_ROW_ONE_AMOUNT; i++) {
			specials[i] = new TextureRegion(spriteSheet01, i * SQUARE_SIZE,
					SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
		}
		for (int i = SPECIALS_ROW_ONE_AMOUNT; i < SPECIALS_AMOUNT; i++) {
			specials[i] = new TextureRegion(spriteSheet01,
					(i - SPECIALS_ROW_ONE_AMOUNT) * SQUARE_SIZE,
					SPECIALS_ROW_TWO * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
		}

		enemyBaseColours = new TextureRegion[ENEMY_COLOURS_AMOUNT];
		for (int i = 0; i < ENEMY_COLOURS_AMOUNT; i++) {
			enemyBaseColours[i] = new TextureRegion(spriteSheet01, i
					* SQUARE_SIZE, ENEMY_BASE_ROW * SQUARE_SIZE, SQUARE_SIZE,
					SQUARE_SIZE);
		}

		enemyHalfColours = new TextureRegion[ENEMY_COLOURS_AMOUNT];
		for (int i = 0; i < ENEMY_COLOURS_AMOUNT; i++) {
			enemyHalfColours[i] = new TextureRegion(spriteSheet01, i
					* SQUARE_SIZE, ENEMY_HALF_ROW * SQUARE_SIZE, SQUARE_SIZE,
					SQUARE_SIZE);
		}

		enemyThirdColours = new TextureRegion[ENEMY_COLOURS_AMOUNT];
		for (int i = 0; i < ENEMY_COLOURS_AMOUNT; i++) {
			enemyThirdColours[i] = new TextureRegion(spriteSheet01, i
					* SQUARE_SIZE, ENEMY_THIRD_ROW * SQUARE_SIZE, SQUARE_SIZE,
					SQUARE_SIZE);
		}

		enemyRimColours = new TextureRegion[ENEMY_COLOURS_AMOUNT];
		for (int i = 0; i < ENEMY_COLOURS_AMOUNT; i++) {
			enemyRimColours[i] = new TextureRegion(spriteSheet01, i
					* SQUARE_SIZE, ENEMY_RIM_ROW * SQUARE_SIZE, SQUARE_SIZE,
					SQUARE_SIZE);
		}

		enemyConnectors = new TextureRegion[ENEMY_CONNECTORS_AMOUNT];
		for (int i = 0; i < ENEMY_CONNECTORS_AMOUNT; i++) {
			enemyConnectors[i] = new TextureRegion(spriteSheet01,
					(i + ENEMY_CONNECTORS_COLUMN) * SQUARE_SIZE,
					ENEMY_CONNECTORS_ROW * SQUARE_SIZE, SQUARE_SIZE,
					SQUARE_SIZE);
		}
	}

	public void render() {
		// this is the main method that gets called every cycle to draw
		// everything, it's called from the render method in GameScreen after
		// getting the inputs etc.

		// camera needs to have update called to show changes made
		cam.update();
		cam.apply(Gdx.gl10);

		// TODO: encapsulate in the background class
		/*
		 * float gravColour = (WorldGravity.G - WorldGravity.gravMin) /
		 * (WorldGravity.gravMax - WorldGravity.gravMin);
		 * 
		 * float red = gravColour; float blue = (1 - gravColour);
		 * 
		 * Gdx.graphics.getGL10().glClearColor(red, 0f, blue, 1f);
		 */

		Gdx.graphics.getGL10().glClearColor(0.3f, 0f, 0.4f, 1f);
		Gdx.graphics.getGL10().glClear(
				GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// spriteBatch has its own camera so the code below sets its projection
		// matrix to that of the camera
		spriteBatch.setProjectionMatrix(cam.combined);
		
		// all drawing using spriteBatch needs to be surrounded with begin() and
		// end()
		spriteBatch.begin();
		// put all the drawing methods here
		// *************************************
		drawBackground();
		
		drawButtons();
		
		drawMenu();

		spriteBatch.end();
	}

	public void setBlendFunction(int mode) {
		// setting blending function
		switch (mode) {
		case 0:
			spriteBatch.setBlendFunction(GL10.GL_SRC_ALPHA,
					GL10.GL_ONE_MINUS_SRC_ALPHA);
			break;
		case 1:
			spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE);
			break;
		case 2:
			spriteBatch.setBlendFunction(GL10.GL_ZERO,
					GL10.GL_ONE_MINUS_SRC_ALPHA);
			break;
		case 3:
			spriteBatch.setBlendFunction(GL10.GL_ONE_MINUS_SRC_ALPHA,
					GL10.GL_ONE_MINUS_SRC_COLOR);
			break;
		case 4:
			spriteBatch.setBlendFunction(GL10.GL_ONE_MINUS_DST_COLOR,
					GL10.GL_ONE);
			break;
		case 5:
			spriteBatch.setBlendFunction(GL10.GL_ONE_MINUS_DST_COLOR,
					GL10.GL_ONE_MINUS_SRC_COLOR);
			break;
		case 6:
			spriteBatch.setBlendFunction(GL10.GL_ONE_MINUS_SRC_ALPHA,
					GL10.GL_ONE_MINUS_DST_COLOR);
			break;
		case 7:
			spriteBatch.setBlendFunction(GL10.GL_ONE_MINUS_SRC_COLOR,
					GL10.GL_ONE_MINUS_SRC_COLOR);
			break;
		default:
			spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE);
		}
	}

	private void drawBackground() {
		setBlendFunction(0);
		background.render(spriteBatch);
	}

	private void drawButtons() {
		setBlendFunction(gameWorld.menu.blendFunction.mode);
		// iterates through the button array and uses the button coordinates
		// saved in each Button object along with offset amounts to draw the
		// buttons to the game grid
		// draw call arguments are Xpos, Ypos, Width, Height
		// get the current carousel colour:

		for (GamePiece orbiter : gameWorld.gamePieces) {
			if (orbiter.colour != GamePiece.NO_COLOUR) {
				spriteBatch
						.draw(orbiter.colour <= MAIN_COLOUR_AMOUNT ? mainColour[orbiter.colour]
								: enemyBaseColours[orbiter.colour
										- MAIN_COLOUR_AMOUNT],
								orbiter.position.x - orbiter.radius,
								orbiter.position.y - orbiter.radius,
								orbiter.diameter, orbiter.diameter);
			}
		}
	}

	private void drawMenu() {
		setBlendFunction(0);
		// draw the icon options
		int i = 0;
		for (MenuIcon icon : gameWorld.menu.icons) {
			spriteBatch.draw(specials[i++], icon.xPos, icon.yPos, icon.size,
					icon.size);
			// draw the ring around the currently active one
			if (icon.isActive) {
				spriteBatch.draw(rings[icon.mode == 0 ? 0 : 1], icon.xPos,
						icon.yPos, icon.size, icon.size);
			}
		}
	}

	public OrthographicCamera getCam() {
		return cam;
	}

	public void setCam(OrthographicCamera cam) {
		this.cam = cam;
	}

	public void setBackground(Background background) {
		this.background = background;
	}

	public TextureRegion getStarTextureRegion() {
		return enemyBaseColours[0];
	}

	public void disposeAllTextures() {
		// all disposable textures get destroyed here
		spriteSheet01.dispose();
	}

}
