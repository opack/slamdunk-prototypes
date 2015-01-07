package com.slamdunk.pixelkingdomadvanced.gameparts.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.components.renderers.SpriteRendererPart;
import com.slamdunk.toolkit.lang.DoubleEntryArray;

public class LetterChooserScript extends Component {
	private static final DoubleEntryArray<Letters, LetterCaseState, TextureRegion> letterData;
	static {
		letterData = new DoubleEntryArray<Letters, LetterCaseState, TextureRegion>();
		final TextureRegion[][] textures = splitLetters();
		for (LetterCaseState state : LetterCaseState.values()) {
			for (Letters letter : Letters.values()) {
				letterData.put(letter, state, textures[state.ordinal()][letter.ordinal()]);
			}
		}
	}
	
	private static TextureRegion[][] splitLetters() {
		Texture spriteSheet = new Texture(Gdx.files.internal("textures/letters.png"));
		return TextureRegion.split(
				spriteSheet,
				spriteSheet.getWidth() / 26,
				spriteSheet.getHeight() / 2);
	}
	
	
	public Letters letter;
	public LetterCaseState state;
	
	private Letters currentLetter;
	private LetterCaseState currentState;
	private SpriteRendererPart spriteRenderer;
	
	@Override
	public void createDependencies() {
		if (!gameObject.hasComponent(SpriteRendererPart.class)) {
			gameObject.addComponent(SpriteRendererPart.class);
		}
	}
	
	@Override
	public void reset() {
		letter = Letters.A;
		currentLetter = null;
		
		state = LetterCaseState.NORMAL;
		currentState = null;
	}
	
	@Override
	public void init() {
		spriteRenderer = gameObject.getComponent(SpriteRendererPart.class);
		if (spriteRenderer == null) {
			throw new IllegalArgumentException("Missing SpriteRendererPart in GameObject. LetterChooserScript cannot work properly.");
		}
	}
	
	@Override
	public void update(float deltaTime) {
		if (currentLetter != letter
		|| currentState != state) {
			spriteRenderer.textureRegion = letterData.get(letter, state);
			currentLetter = letter;
			currentState = state;
		}
	}
}
