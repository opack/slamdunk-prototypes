package com.slamdunk.wordarena.gameparts.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.gameparts.components.position.PathFollowerScript;
import com.slamdunk.toolkit.gameparts.components.position.TrackerScript;
import com.slamdunk.toolkit.gameparts.components.renderers.ParticleRendererPart;
import com.slamdunk.toolkit.gameparts.components.renderers.SpriteRendererPart;
import com.slamdunk.toolkit.gameparts.components.ui.UIButtonPart;
import com.slamdunk.toolkit.gameparts.components.ui.UIComponent;
import com.slamdunk.toolkit.gameparts.components.ui.UIProgressBarPart;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;
import com.slamdunk.toolkit.gameparts.gameobjects.Sprite;
import com.slamdunk.toolkit.gameparts.scene.Scene;
import com.slamdunk.wordarena.gameparts.creators.CustomSVGLoader;
import com.slamdunk.wordarena.gameparts.prefabs.Castle;
import com.slamdunk.wordarena.gameparts.prefabs.Paladin;
import com.slamdunk.wordarena.gameparts.scripts.ButtonClickTestScript;
import com.slamdunk.wordarena.gameparts.scripts.PathSpeedTweakerScript;

public class GamePartsTestScreen implements Screen {
	private Scene scene;
	
	public GamePartsTestScreen() {
		createScene();
	}
	
	private void createScene() {
		scene = new Scene(800, 480, true);
		
		// Charge le SVG contenant le level design
		CustomSVGLoader sceneLoader = new CustomSVGLoader("battlefields/battlefield0.svg");
		
		// Place la caméra au centre de l'écran
		scene.observationPoint.transform.relativePosition.x = scene.observationPoint.camera.viewportWidth / 2;
		scene.observationPoint.transform.relativePosition.y = scene.observationPoint.camera.viewportHeight / 2;
		scene.observationPoint.addComponent(TrackerScript.class);
		
		// Ajoute une couche à la scène
		scene.addLayer("background", 0);
		
		// Ajoute un sprite à la scène
		Sprite map = new Sprite();
		map.spriteRenderer.spriteFile = "maps/battlefield0.png";
		scene.addGameObject(map, "background");
		
		// Ajoute un château
		scene.addGameObject(Castle.class);
		
		// Ajoute un paladin
		Paladin paladin = scene.addGameObject(Paladin.class);
		paladin.name = "Paladin";
		paladin.getComponent(SpriteRendererPart.class).origin.set(0.5f,0.5f); // L'origine des rotations est le centre
		paladin.getComponent(SpriteRendererPart.class).anchor.set(0.5f,0.5f); // Position toujours exprimée par rapport au centre
		paladin.getComponent(PathFollowerScript.class).path = sceneLoader.paths.get(0);
		paladin.getComponent(PathFollowerScript.class).speed = 50;
		
		GameObject dot = paladin.addChild();
		dot.addComponent(SpriteRendererPart.class);
		dot.getComponent(SpriteRendererPart.class).spriteFile = "textures/dot.png";
		dot.transform.relativePosition.set(16, 16, 0);
		dot.addComponent(ParticleRendererPart.class);
		dot.getComponent(ParticleRendererPart.class).effectFile = "particles/firebeam.p";
		dot.getComponent(ParticleRendererPart.class).imagesDirectory = "particles";
		dot.getComponent(ParticleRendererPart.class).active = false;
		
		GameObject dot2 = scene.addGameObject();
		dot2.addComponent(SpriteRendererPart.class);
		dot2.getComponent(SpriteRendererPart.class).spriteFile = "textures/dot.png";
		dot2.transform.relativePosition.set(160, 160, 0);
		
		Skin skin = new Skin(Gdx.files.internal("skins/uiskin/uiskin.json"));
		GameObject button = scene.addGameObject();
		button.addComponent(UIButtonPart.class);
		button.getComponent(UIButtonPart.class).skin = skin;
		button.getComponent(UIButtonPart.class).script = new ButtonClickTestScript();
		button.getComponent(UIButtonPart.class).text = "Test bouton";
		button.transform.relativePosition.set(10, 240, 0);
		
		GameObject slider = scene.addGameObject();
		slider.addComponent(UIProgressBarPart.class);
		slider.getComponent(UIProgressBarPart.class).skin = skin;
		slider.getComponent(UIProgressBarPart.class).minValue = 0;
		slider.getComponent(UIProgressBarPart.class).currentValue = 50;
		slider.getComponent(UIProgressBarPart.class).maxValue = 100;
		slider.getComponent(UIProgressBarPart.class).stepSize = 1;
		slider.getComponent(UIProgressBarPart.class).verticalOriented = false;
		slider.getComponent(UIProgressBarPart.class).isSlider = true;
		slider.transform.relativePosition.set(100, 120, 0);
		slider.addComponent(PathSpeedTweakerScript.class);
		slider.getComponent(PathSpeedTweakerScript.class).pathFollower = paladin.getComponent(PathFollowerScript.class);
		
		GameObject dbg = scene.addGameObject();
		dbg.addComponent(UIComponent.class);
		dbg.getComponent(UIComponent.class).skin = skin;
		dbg.getComponent(UIComponent.class).actor = new Label("Test label", skin);
		dbg.transform.relativePosition.set(100, 150, 0);
		
		scene.observationPoint.getComponent(TrackerScript.class).target = paladin;
		scene.observationPoint.getComponent(TrackerScript.class).leech = 100;
		scene.observationPoint.getComponent(TrackerScript.class).reachTime = 1;
		
		scene.init();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	float dbg;
	@Override
	public void render(float delta) {
		scene.render(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
}
