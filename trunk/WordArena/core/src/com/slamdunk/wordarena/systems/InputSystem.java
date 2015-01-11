package com.slamdunk.wordarena.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.slamdunk.wordarena.Arena;
import com.slamdunk.wordarena.CellStates;
import com.slamdunk.wordarena.GameStates;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.components.ColliderComponent;
import com.slamdunk.wordarena.components.InputComponent;
import com.slamdunk.wordarena.components.LetterCellComponent;
import com.slamdunk.wordarena.components.TextureComponent;
import com.slamdunk.wordarena.components.TransformComponent;

public class InputSystem extends IteratingSystem implements InputProcessor {
	private Vector3 tmp;
	private Vector2 lastTouch;
	
	private Entity lastEntity;
	private List<Entity> selectedLetters;
	
	private boolean isDragging;
	
	private Arena arena;
	private Camera camera;
	
	private Rectangle screenBounds;
	private Rectangle arenaBounds;
	
	@SuppressWarnings("unchecked")
	public InputSystem(Camera camera) {
		super(Family.all(InputComponent.class, TextureComponent.class, ColliderComponent.class, TransformComponent.class).get());

		this.camera = camera;
		
		tmp = new Vector3();
		lastTouch = new Vector2();
		
		selectedLetters = new ArrayList<Entity>();
		
		screenBounds = new Rectangle(0, 0, WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT);
		arenaBounds = new Rectangle(0, 0, 0, 0);
	}

	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
		arenaBounds.width = arena.getWidth();
		arenaBounds.height = arena.getHeight();
	}

	/**
	 * 
	 * @param screenX
	 * @param screenY
	 * @return true si la touche a été gérée
	 */
	private boolean updateLastTouch(int screenX, int screenY) {
		// Vérifie que les coordonnées sont bien sur la zone d'affichage
		if (!screenBounds.contains(screenX, screenY)) {
			return false;
		}
		
		// Convertit les coordonnées "écran" en coordonnées "monde"
		tmp.set(screenX, screenY, 1);
		camera.unproject(tmp);
		
		// Vérifie que les coordonnées sont bien au-dessus de l'arène
		if (!arenaBounds.contains(tmp.x, tmp.y)) {
			return false;
		}
		
		// Vérifier si la nouvelle case est bien autour de la dernière case sélectionnée
		if (lastEntity != null) {
			TransformComponent lastTransform = ComponentMappers.TRANSFORM.get(lastEntity);
			if (Math.abs(tmp.x - lastTransform.pos.x) > 2
			|| Math.abs(tmp.y - lastTransform.pos.y) > 2) {
				System.out.println(tmp + " / " + lastTransform.pos);
				return false;
			}
		}
		
		// Tout est bon, la touche est valide ! On l'enregistre pour que la lettre
		// correspondante soit sélectionnée lors du prochain update
		lastTouch.set(tmp.x, tmp.y);
		return true;
	}
	
	private void updateLetterCellState(Entity entity, CellStates state) {
		LetterCellComponent letterCell = ComponentMappers.LETTER_CELL.get(entity);
		letterCell.type = state;
		
		TextureComponent texture = ComponentMappers.TEXTURE.get(entity);
		//texture.region = Assets.letterData.get(letterCell.letter, letterCell.type);
		switch (state) {
		case NORMAL:
			texture.tint.set(Color.WHITE);
			break;
		case SELECTED:
			texture.tint.set(Color.YELLOW);
			break;
		}
	}
	
	@Override
	public void update(float deltaTime) {
		if (!isDragging) {
			return;
		}
		super.update(deltaTime);
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		ColliderComponent collider = ComponentMappers.COLLIDER.get(entity);

		if ((lastEntity == null	|| lastEntity.getId() != entity.getId())
		&& collider.bounds.contains(lastTouch.x, lastTouch.y)) {
			// Conserve l'entité de la dernière lettre sélectionnée
			lastEntity = entity;
			
			// Marque la lettre comme sélectionnée
			updateLetterCellState(entity, CellStates.SELECTED);
			
			// Ajoute la lettre aux lettres sélectionnées pour constituer un mot
			selectedLetters.add(entity);
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (arena.state == GameStates.RUNNING
		&& updateLastTouch(screenX, screenY)) {
			isDragging = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		updateLastTouch(screenX, screenY);
		isDragging = true;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (!selectedLetters.isEmpty()) {
			// Teste si le mot est valide
			StringBuilder word = new StringBuilder();
			for (Entity entity : selectedLetters) {
				word.append(ComponentMappers.LETTER_CELL.get(entity).letter.label);
			}
			arena.validateWord(word.toString());
			
			// Réinitialise les lettres sélectionnées
			for (Entity entity : selectedLetters) {
				updateLetterCellState(entity, CellStates.NORMAL);
			}
			selectedLetters.clear();
			lastEntity = null;
		}
		
		// Fin de la sélection
		isDragging = false;
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
