package com.slamdunk.wordarena.gameparts.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.gameparts.components.position.PathScript;
import com.slamdunk.toolkit.gameparts.components.position.TrackerScript;
import com.slamdunk.toolkit.gameparts.components.renderers.ParticleRendererPart;
import com.slamdunk.toolkit.gameparts.components.renderers.SpriteRendererPart;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;
import com.slamdunk.toolkit.gameparts.gameobjects.Sprite;
import com.slamdunk.toolkit.gameparts.scene.Scene;
import com.slamdunk.wordarena.gameparts.creators.CustomSVGLoader;
import com.slamdunk.wordarena.gameparts.prefabs.Castle;
import com.slamdunk.wordarena.gameparts.prefabs.Paladin;

public class GamePartsTestScreen implements Screen {
	private Scene scene;
	private Stage stage;
	
	public GamePartsTestScreen() {
		createScene();
		createUI();
	}
	
	private void createScene() {
		scene = new Scene(800, 480);
		
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
		paladin.getComponent(SpriteRendererPart.class).origin.set(0.5f,0.5f); // L'origine des rotations est le centre
		paladin.getComponent(SpriteRendererPart.class).anchor.set(0.5f,0.5f); // Position toujours exprimée par rapport au centre
		paladin.getComponent(PathScript.class).path = sceneLoader.paths.get(0);
		paladin.getComponent(PathScript.class).speed = 50;
		
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
		
		scene.observationPoint.getComponent(TrackerScript.class).target = paladin;
		scene.observationPoint.getComponent(TrackerScript.class).leech = 100;
		scene.observationPoint.getComponent(TrackerScript.class).reachTime = 1;
		
		scene.init();
	}
	
	private void createUI() {
		stage = new Stage();
		Skin skin = new Skin(Gdx.files.internal("skins/uiskin/uiskin.json"));
		stage.addActor(new Button(skin));
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		scene.render(delta);
		stage.act(delta);
		stage.draw();
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
