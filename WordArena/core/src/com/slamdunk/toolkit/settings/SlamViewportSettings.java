package com.slamdunk.toolkit.settings;

import com.badlogic.gdx.Gdx;

// DBG TODO Supprimer : la classe Viewport et ses filles permettent une meilleure gestion
/**
 * Contient les réglages de l'application. Ces réglages peuvent être définis
 * à l'aide d'une des méthodes setup().
 * @author Yed
 * @see #setup()
 * @see #setup(float, float, float, float)
 * @see #setup(float, float, float, float, float, float)
 */
public class SlamViewportSettings {
	public static float SCREEN_W = 0.0f;
	public static float SCREEN_H = 0.0f;
	public static float WORLD_TARGET_WIDTH = 0.0f;
	public static float WORLD_TARGET_HEIGHT = 0.0f;
	public static float WORLD_WIDTH = 0.0f;
	public static float WORLD_HEIGHT = 0.0f;

	public static boolean viewportSet = false;

	/**
	 * Manual app settings, not using DIPActive
	 * 
	 * @param screenWidth
	 *            sets SCREEN_W for AbstractScreen Stage width
	 * @param screenHeight
	 *            sets SCREEN_H for AbstractScreen Stage height
	 * @param worldWidth
	 *            sets WORLD_WIDTH
	 * @param worldHeight
	 *            sets WORLD_HEIGHT
	 * 
	 * */
	public static void setup(float screenWidth, float screenHeight, float worldWidth, float worldHeight) {
		reset();
		//
		SCREEN_W = screenWidth;
		SCREEN_H = screenHeight;
		WORLD_WIDTH = worldWidth;
		WORLD_HEIGHT = worldHeight;
		WORLD_TARGET_WIDTH = worldWidth;
		WORLD_TARGET_HEIGHT = worldHeight;
		viewportSet = true;
	}

	/**
	 * Manual app settings, using DIPActive
	 * 
	 * @param screenWidth
	 *            sets SCREEN_W for AbstractScreen Stage width
	 * @param screenHeight
	 *            sets SCREEN_H for AbstractScreen Stage height
	 * @param worldWidth
	 *            sets WORLD_WIDTH
	 * @param worldHeight
	 *            sets WORLD_HEIGHT
	 * @param worldTargetWidth
	 *            sets WORLD_TARGET_WIDTH for DIPActive calculations
	 * @param worldHeight
	 *            sets WORLD_TARGET_HEIGHT for DIPActive calculations
	 * 
	 * */
	public static void setup(
			float screenWidth, float screenHeight,
			float worldWidth, float worldHeight, 
			float worldTargetWidth,	float worldTargetHeight) {
		reset();
		//
		SCREEN_W = screenWidth;
		SCREEN_H = screenHeight;
		WORLD_WIDTH = worldWidth;
		WORLD_HEIGHT = worldHeight;
		WORLD_TARGET_WIDTH = worldTargetWidth;
		WORLD_TARGET_HEIGHT = worldTargetHeight;
		viewportSet = true;
	}

	/**
	 * Default set up for DIPActive, default values are:
	 * <p>
	 * SCREEN_W = Gdx.graphics.getWidth(); <br>
	 * SCREEN_H = Gdx.graphics.getHeight(); <br>
	 * WORLD_WIDTH = Gdx.graphics.getWidth(); <br>
	 * WORLD_HEIGHT = Gdx.graphics.getHeight(); <br>
	 * WORLD_TARGET_WIDTH = 960; <br>
	 * WORLD_TARGET_HEIGHT = 540; <br>
	 * 
	 * */
	public static void setup() {
		// Reset before setUp
		reset();

		// ####################### ONLY CHANGE THIS PART ######################
		//
		// DEVICE SCREEN RESOLUTION
		//
		// - This mainly used for Scene 2D stage creation
		// - Think this is as ViewPort
		// - If you want fixed size (Not using DIPActive) set as fixed size
		//
		
		SCREEN_W = Gdx.graphics.getWidth();
		SCREEN_H = Gdx.graphics.getHeight();

		// WORLD TARGET WIDTH & HEIGHT (Virtual)
		//
		// - This is for actor/texture resizing
		// - This will help the prevent stretching for different resolutions
		// - Create your textures in PhotoShop for this dimensions (this world
		// size), then it will be scaled for other resolutions
		//
		WORLD_TARGET_WIDTH = 960;
		WORLD_TARGET_HEIGHT = 540;

		// WORLD WIDTH & HEIGHT (Real)
		//
		// - Our real world size
		// - Generally same with device dimensions
		// - If you want fixed size (Not using DIPActive) set as fixed size
		// - if you need bigger world than ViewPort like Angry Birds, give some
		// bigger values then you can swipe world just like in Angry Birds
		//
		WORLD_WIDTH = Gdx.graphics.getWidth();
		WORLD_HEIGHT = Gdx.graphics.getHeight();
		//
		// ####################### END OF CHANGE PART ######################
		viewportSet = true;
	}

	/**
	 * Get size ratio to scale actors (only for DIPactive true)
	 * <p>
	 * EXAMPLE: <br>
	 * if WORLD_TARGET_WIDTH = 480, and WORLD_WIDTH = 480, there wont be scaling
	 * for DIPactive actors, but if WORLD_WIDTH = 960, so actors will be scaled
	 * by 2.0f to get best fitting for different resolution devices
	 * */
	public static float getWorldSizeRatio() {
		//Return the more restrictive ratio
		if(getWorldPositionXRatio() < getWorldPositionYRatio())
			return getWorldPositionXRatio();
		else
			return getWorldPositionYRatio();
	}

	/**
	 * Get position X ratio to re-position actors (only for DIPactive true)
	 * <p>
	 * EXAMPLE: <br>
	 * if WORLD_TARGET_WIDTH = 480, and we set x position 20 for actor. We
	 * designed this for 480 WORLD_WIDTH, but a device with 960 width,
	 * WORLD_WIDTH will be 960, so new position should be 40 in this world, so
	 * position ratio is 2.0f
	 * */
	public static float getWorldPositionXRatio() {
		return WORLD_WIDTH / WORLD_TARGET_WIDTH;
	}

	/**
	 * Get position Y ratio to re-position actors (only for DIPactive true)
	 * <p>
	 * EXAMPLE: <br>
	 * if WORLD_TARGET_HEIGHT = 480, and we set y position 20 for actor. We
	 * designed this for 480 WORLD_HEIGHT, but a device with 960 height,
	 * WORLD_HEIGHT will be 960, so new position should be 40 in this world, so
	 * position ratio is 2.0f
	 * */
	public static float getWorldPositionYRatio() {
		return WORLD_HEIGHT / WORLD_TARGET_HEIGHT;
	}

	/**
	 * Remet l'ensemble des valeurs à 0.
	 */
	private static void reset() {
		SCREEN_W = 0.0f;
		SCREEN_H = 0.0f;
		WORLD_TARGET_WIDTH = 0.0f;
		WORLD_TARGET_HEIGHT = 0.0f;
		WORLD_WIDTH = 0.0f;
		WORLD_HEIGHT = 0.0f;
		viewportSet = false;
	}
}
