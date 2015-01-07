package com.slamdunk.wordarena;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.slamdunk.wordarena.components.BoundsComponent;
import com.slamdunk.wordarena.components.CameraComponent;
import com.slamdunk.wordarena.components.CellComponent;
import com.slamdunk.wordarena.components.TextureComponent;
import com.slamdunk.wordarena.components.TransformComponent;
import com.slamdunk.wordarena.systems.RenderingSystem;

public class Arena {
	public static final float ARENA_WIDTH = 25;
	public static final float ARENA_HEIGHT = 15;
	public static final int ARENA_STATE_RUNNING = 0;
	public static final int ARENA_STATE_NEXT_LEVEL = 1;
	public static final int ARENA_STATE_GAME_OVER = 2;

	public int score;
	public int state;
	private Engine engine;

	public Arena(Engine engine) {
		this.engine = engine;

		createBackground();
		createArena();
		createCamera();

		this.score = 0;
		this.state = ARENA_STATE_RUNNING;
	}
	
	private void createArena() {
		for (int y = 0; y < ARENA_HEIGHT; y++) {
			for (int x = 0; x < ARENA_WIDTH; x++) {
				createCell(CellStates.NORMAL, x, y);
			}
		}
	}
	
	private void createCell(CellStates type, float x, float y) {
		Entity entity = new Entity();
		
		CellComponent cell = new CellComponent();
		BoundsComponent bounds = new BoundsComponent();
		TransformComponent position = new TransformComponent();
		TextureComponent texture = new TextureComponent();
		
		cell.type = type;
		cell.letter = Letters.values()[MathUtils.random(25)];
		
		texture.region = Assets.letterData.get(cell.letter, cell.type);
		
		bounds.bounds.width = 1;
		bounds.bounds.height = 1;
		
		position.pos.set(x, y, Layers.CELLS.ordinal());
		
		entity.add(cell);
		entity.add(bounds);
		entity.add(position);
		entity.add(texture);
		
		engine.addEntity(entity);
	}
	
	private void createCamera() {
		Entity entity = new Entity();
		
		CameraComponent camera = new CameraComponent();
		camera.camera = engine.getSystem(RenderingSystem.class).getCamera();
		
		entity.add(camera);
		
		engine.addEntity(entity);
	}
	
	private void createBackground() {
		Entity entity = new Entity();
		
		TransformComponent position = new TransformComponent();
		TextureComponent texture = new TextureComponent();
		
		texture.region = Assets.backgroundRegion;
		
		position.pos.set(0, 0, Layers.BACKGROUND.ordinal());
		
		entity.add(position);
		entity.add(texture);
		
		engine.addEntity(entity);
	}
}
