package com.slamdunk.wordarena;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.slamdunk.toolkit.lang.Deck;
import com.slamdunk.wordarena.components.BoundsComponent;
import com.slamdunk.wordarena.components.CameraComponent;
import com.slamdunk.wordarena.components.ColliderComponent;
import com.slamdunk.wordarena.components.InputComponent;
import com.slamdunk.wordarena.components.LetterCellComponent;
import com.slamdunk.wordarena.components.TextureComponent;
import com.slamdunk.wordarena.components.TransformComponent;
import com.slamdunk.wordarena.systems.ComponentMappers;
import com.slamdunk.wordarena.systems.RenderingSystem;

public class Arena {
	public static final float CELL_GAP = 0.05f;
	private static final int MIN_WORD_LENGTH = 3;
	
	private static final Set<String> words;
	static {
		words = new HashSet<String>();
		FileHandle file = Gdx.files.internal("words.txt");
		BufferedReader reader = new BufferedReader(file.reader("UTF-8"));
		String extracted = null;
		try {
			while ((extracted = reader.readLine()) != null) {
				if (extracted.length() >= MIN_WORD_LENGTH) {
					words.add(extracted);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int width;
	private int height;
	private Entity[][] letterCells;
	private Deck<Letters> lettersDeck;
	
	public int score;
	public GameStates state;
	private Engine engine;

	public Arena(Engine engine, int width, int height) {
		this.engine = engine;
		this.width = width;
		this.height = height;
		letterCells = new Entity[width][height];

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
		// Génère des lettres en tenant compte de leur représentation
		lettersDeck = new Deck<Letters>(Letters.values(), 1);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				letterCells[x][y] = createCell(CellStates.NORMAL, x, y, lettersDeck.draw());
			}
		}
	}
	
	private Entity createCell(CellStates type, float x, float y, Letters letter) {
		TransformComponent transform = new TransformComponent();
		transform.pos.set(x + x * CELL_GAP, y + y * CELL_GAP, Layers.LETTERS.ordinal());
		
		BoundsComponent bounds = new BoundsComponent();
		bounds.bounds.width = 1;
		bounds.bounds.height = 1;
		
		// Création d'un collider placé au milieu de la case
		// et faisant les 3/4 de sa taille
		ColliderComponent collider = new ColliderComponent();
		collider.relativeOrigin.x = 0.125f;
		collider.relativeOrigin.x = 0.125f;
		collider.bounds.width = 0.75f;
		collider.bounds.height = 0.75f;
		
		InputComponent input = new InputComponent();
		
		LetterCellComponent letterCell = new LetterCellComponent();
		letterCell.type = type;
		letterCell.letter = letter;
		
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
		return entity;
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

	/**
	 * Tente de valider le mot. Si le mot est valide :
	 *  - le score est modifié
	 *  - de nouvelles lettres sont choisies
	 *  - les lettres sont désélectionnées
	 * @param selectedLetters
	 */
	public boolean validateWord(List<Entity> selectedLetters) {
		// Vérifie si le mot est valide
		StringBuilder wordLetters = new StringBuilder();
		for (Entity entity : selectedLetters) {
			wordLetters.append(ComponentMappers.LETTER_CELL.get(entity).letter.label);
		}
		final String word = wordLetters.toString();
		if (!words.contains(word)) {
			System.out.println(word + " n'est pas un mot valide.");
			return false;
		}
		
		// Modifie le score
		score += word.length();
		System.out.println(word + " rapporte " + word.length() + " points. Nouveau score : " + score);
		
		// Choisie de nouvelles lettres et réinitialise leur état
		for (Entity entity : selectedLetters) {
			ComponentMappers.LETTER_CELL.get(entity).letter = lettersDeck.draw();
		}
		return true;
	}

	public Entity getEntityAt(int x, int y) {
		return letterCells[x][y];
	}
}
