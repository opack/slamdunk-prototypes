/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.slamdunk.wordarena.systems;

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
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
	
	private ComponentMapper<TextureComponent> textureMapper;
	private ComponentMapper<TransformComponent> transformMapper;
	private ComponentMapper<BoundsComponent> boundsMapper;
	
	@SuppressWarnings("unchecked")
	public RenderingSystem(SpriteBatch batch) {
		super(Family.all(TransformComponent.class, TextureComponent.class).get());
		
		textureMapper = ComponentMapper.getFor(TextureComponent.class);
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
		boundsMapper = ComponentMapper.getFor(BoundsComponent.class);
		
		renderQueue = new Array<Entity>();
		
		comparator = new Comparator<Entity>() {
			@Override
			public int compare(Entity entityA, Entity entityB) {
				return (int)Math.signum(transformMapper.get(entityA).pos.z - transformMapper.get(entityB).pos.z);
			}
		};
		
		this.batch = batch;
		
		cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		cam.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0);
		
		stage = new Stage(new FitViewport(FRUSTUM_WIDTH, FRUSTUM_HEIGHT, cam), batch);
		Skin skin = new Skin(Gdx.files.internal("skins/uiskin/uiskin.json"));
		TextButton btn = new TextButton("DBG", skin);
		btn.setPosition(20, 30);
		stage.addActor(btn);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		renderQueue.sort(comparator);
		
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		
		for (Entity entity : renderQueue) {
			TextureComponent tex = textureMapper.get(entity);
			
			if (tex.region == null) {
				continue;
			}
			
			TransformComponent transform = transformMapper.get(entity);
			BoundsComponent bounds = boundsMapper.get(entity);

			// Calcule la taille de l'image en dimensions du monde
			float width = tex.region.getRegionWidth() * PIXELS_TO_CELLS;
			float height = tex.region.getRegionHeight() * PIXELS_TO_CELLS;
			// Si l'entité a une taille, on convertit l'utilise
			if (bounds != null) {
				width *= bounds.bounds.width;
				height *= bounds.bounds.height;
			}
			float originX = width * 0.5f;
			float originY = height * 0.5f;
			
			batch.draw(
				tex.region,
				transform.pos.x - originX, transform.pos.y - originY,
				originX, originY,
				width, height,
				transform.scale.x, transform.scale.y,
				MathUtils.radiansToDegrees * transform.rotation);
		}
		
		batch.end();
		renderQueue.clear();
		
//		stage.act(deltaTime);
//		stage.draw();
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		renderQueue.add(entity);
	}
	
	public OrthographicCamera getCamera() {
		return cam;
	}
}
