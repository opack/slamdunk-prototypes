package com.slamdunk.wordarena;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.slamdunk.wordarena.components.BoundsComponent;
import com.slamdunk.wordarena.components.CameraComponent;
import com.slamdunk.wordarena.components.ColliderComponent;
import com.slamdunk.wordarena.components.InputComponent;
import com.slamdunk.wordarena.components.LetterCellComponent;
import com.slamdunk.wordarena.components.TextureComponent;
import com.slamdunk.wordarena.components.TransformComponent;
import com.slamdunk.wordarena.systems.RenderingSystem;

public class Arena {
	private int width;
	private int height;
	
	public int score;
	public GameStates state;
	private Engine engine;

	public Arena(Engine engine, int width, int height) {
		this.engine = engine;
		this.width = width;
		this.height = height;

		createBackground();
		createArena();
		createCamera();

		this.score = 0;
		this.state = GameStates.RUNNING;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private void createArena() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				createCell(CellStates.NORMAL, x, y);
			}
		}
	}
	
	private void createCell(CellStates type, float x, float y) {
		TransformComponent transform = new TransformComponent();
		transform.pos.set(x, y, Layers.LETTERS.ordinal());
		
		BoundsComponent bounds = new BoundsComponent();
		bounds.bounds.width = 1;
		bounds.bounds.height = 1;
		
		// Création d'un collider placé au milieu de la case et faisant la moitié
		// de sa taille
		ColliderComponent collider = new ColliderComponent();
		collider.relativeOrigin.x = 0.25f;
		collider.relativeOrigin.x = 0.25f;
		collider.bounds.width = 0.5f;
		collider.bounds.height = 0.5f;
		
		InputComponent input = new InputComponent();
		
		LetterCellComponent letterCell = new LetterCellComponent();
		letterCell.type = type;
		letterCell.letter = Letters.values()[MathUtils.random(25)];
		
		TextureComponent texture = new TextureComponent();
		texture.region = Assets.letterData.get(letterCell.letter, letterCell.type);
		
		Entity entity = new Entity();
		entity.add(transform);
		entity.add(bounds);
		entity.add(collider);
		entity.add(texture);
		entity.add(input);
		entity.add(letterCell);
		
		engine.addEntity(entity);
	}
	
	private void createCamera() {
		CameraComponent camera = new CameraComponent();
		camera.camera = engine.getSystem(RenderingSystem.class).getCamera();
		
		Entity entity = new Entity();
		entity.add(camera);
		
		engine.addEntity(entity);
	}
	
	private void createBackground() {
		TransformComponent transform = new TransformComponent();
		transform.pos.set(0, 0, Layers.BACKGROUND.ordinal());
		
		TextureComponent texture = new TextureComponent();
		texture.region = Assets.backgroundRegion;
		
		Entity entity = new Entity();
		entity.add(transform);
		entity.add(texture);
		
		engine.addEntity(entity);
	}
}
