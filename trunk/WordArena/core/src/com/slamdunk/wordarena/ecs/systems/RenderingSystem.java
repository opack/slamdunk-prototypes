package com.slamdunk.wordarena.ecs.systems;

import java.util.Comparator;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.wordarena.ecs.WordArenaGame;
import com.slamdunk.wordarena.ecs.components.BoundsComponent;
import com.slamdunk.wordarena.ecs.components.ShapeComponent;
import com.slamdunk.wordarena.ecs.components.TextureComponent;
import com.slamdunk.wordarena.ecs.components.TransformComponent;

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
	
	private SpriteBatch textureRenderer;
	private ShapeRenderer shapeRenderer;
	private Object currentRenderer;
	
	private Array<Entity> renderQueue;
	private Comparator<Entity> comparator;
	private OrthographicCamera cam;
	
	@SuppressWarnings("unchecked")
	public RenderingSystem(SpriteBatch batch) {
		super(Family.all(TransformComponent.class)
					.one(TextureComponent.class, ShapeComponent.class)
					.get());
		
		renderQueue = new Array<Entity>();
		
		comparator = new Comparator<Entity>() {
			@Override
			public int compare(Entity entityA, Entity entityB) {
				return (int)Math.signum(ComponentMappers.TRANSFORM.get(entityA).pos.z - ComponentMappers.TRANSFORM.get(entityB).pos.z);
			}
		};
		
		this.textureRenderer = batch;
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		
		cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		cam.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		renderQueue.sort(comparator);
		
		updateProjectionMatrixes();
		
		for (Entity entity : renderQueue) {
			// S'il y a une texture à dessiner, on la dessine
			TextureComponent texture = ComponentMappers.TEXTURE.get(entity);
			if (texture != null
			&& texture.region != null) {
				enableTextureRenderer();
				renderTexture(texture, entity);
			}
			// S'il y a une forme à dessiner, on la dessine
			ShapeComponent shape = ComponentMappers.SHAPE.get(entity);
			if (shape != null
			&& shape.polygon.getTransformedVertices().length > 0) {
				enableShapeRenderer();
				renderShape(shape, entity);
			}
		}
		
		endRenderers();
		renderQueue.clear();
	}
	
	/**
	 * Effectue le rendu de la forme de l'entité indiquée
	 * @param shape
	 * @param entity
	 */
	private void renderShape(ShapeComponent shape, Entity entity) {
		shapeRenderer.set(shape.drawStyle);
		if (shape.color != null) {
			shapeRenderer.setColor(shape.color);
		}
		shapeRenderer.polygon(shape.polygon.getTransformedVertices());
	}

	/**
	 * Effectue le rendu de la texture de l'entité indiquée
	 * @param texture
	 * @param entity
	 */
	private void renderTexture(TextureComponent texture, Entity entity) {
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
		
		textureRenderer.setColor(texture.tint);
		textureRenderer.draw(
			texture.region,
			transform.pos.x - originX, transform.pos.y - originY,
			originX, originY,
			width, height,
			transform.scale.x, transform.scale.y,
			MathUtils.radiansToDegrees * transform.rotation);
	}

	/**
	 * Met à jour la caméra et la matrice de projection
	 * des 2 renderers
	 */
	private void updateProjectionMatrixes() {
		cam.update();
		textureRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.setProjectionMatrix(cam.combined);
		currentRenderer = null;
	}
	
	/**
	 * Active le textureRenderer si nécessaire.
	 * Cela implique que si c'est le shapeRenderer qui est
	 * actif, on termine le dessin actuel et on commence
	 * un dessin de texture.
	 */
	private void enableTextureRenderer() {
		if (currentRenderer == textureRenderer) {
			return;
		}
		if (shapeRenderer.isDrawing()) {
			shapeRenderer.end();
		}
		textureRenderer.begin();
		currentRenderer = textureRenderer;
	}
	
	/**
	 * Active le shapeRenderer si nécessaire.
	 * Cela implique que si c'est le textureRenderer qui est
	 * actif, on termine le dessin actuel et on commence
	 * un dessin de formes.
	 */
	private void enableShapeRenderer() {
		if (currentRenderer == shapeRenderer) {
			return;
		}
		if (textureRenderer.isDrawing()) {
			textureRenderer.end();
		}
		shapeRenderer.begin();
		currentRenderer = shapeRenderer;
	}
	
	/**
	 * Termine les 2 renderers
	 */
	private void endRenderers() {
		if (currentRenderer == textureRenderer) {
			textureRenderer.end();
		} else if (currentRenderer == shapeRenderer){
			shapeRenderer.end();
		}
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		renderQueue.add(entity);
	}
	
	public OrthographicCamera getCamera() {
		return cam;
	}
}
