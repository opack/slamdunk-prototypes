package com.slamdunk.wordarena.systems;

import java.util.Comparator;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.components.BoundsComponent;
import com.slamdunk.wordarena.components.TextureComponent;
import com.slamdunk.wordarena.components.TransformComponent;

public class RenderingSystem extends IteratingSystem {
	private static final float FRUSTUM_WIDTH = 15;
	private static final float FRUSTUM_HEIGHT = FRUSTUM_WIDTH / ((float)WordArenaGame.SCREEN_WIDTH / WordArenaGame.SCREEN_HEIGHT);
	/**
	 * Multiplier par un nombre de cellules pour savoir combien de pixels cela fait
	 */
	private static final float CELLS_TO_PIXELS = 48.0f;
	/**
	 * Multiplier par un nombre de pixels pour savoir combien de cellules cela fait
	 */
	private static final float PIXELS_TO_CELLS = 1.0f / CELLS_TO_PIXELS;
	
	private SpriteBatch batch;
	private Array<Entity> renderQueue;
	private Comparator<Entity> comparator;
	private OrthographicCamera cam;
	private Stage stage;
	
	@SuppressWarnings("unchecked")
	public RenderingSystem(SpriteBatch batch) {
		super(Family.all(TransformComponent.class, TextureComponent.class).get());
		
		renderQueue = new Array<Entity>();
		
		comparator = new Comparator<Entity>() {
			@Override
			public int compare(Entity entityA, Entity entityB) {
				return (int)Math.signum(ComponentMappers.TRANSFORM.get(entityA).pos.z - ComponentMappers.TRANSFORM.get(entityB).pos.z);
			}
		};
		
		this.batch = batch;
		
		cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		cam.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		renderQueue.sort(comparator);
		
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		
		for (Entity entity : renderQueue) {
			TextureComponent texture = ComponentMappers.TEXTURE.get(entity);
			
			if (texture.region == null) {
				continue;
			}
			
			TransformComponent transform = ComponentMappers.TRANSFORM.get(entity);
			BoundsComponent bounds = ComponentMappers.BOUNDS.get(entity);

			// Calcule la taille de l'image en dimensions du monde
			float width = texture.region.getRegionWidth() * PIXELS_TO_CELLS;
			float height = texture.region.getRegionHeight() * PIXELS_TO_CELLS;
			// Si l'entité a une taille, on convertit l'utilise
			if (bounds != null) {
				width *= bounds.bounds.width;
				height *= bounds.bounds.height;
			}
			float originX = width * 0;
			float originY = height * 0;
			
			batch.draw(
				texture.region,
				transform.pos.x - originX, transform.pos.y - originY,
				originX, originY,
				width, height,
				transform.scale.x, transform.scale.y,
				MathUtils.radiansToDegrees * transform.rotation);
		}
		
		batch.end();
		renderQueue.clear();
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		renderQueue.add(entity);
	}
	
	public OrthographicCamera getCamera() {
		return cam;
	}
}
