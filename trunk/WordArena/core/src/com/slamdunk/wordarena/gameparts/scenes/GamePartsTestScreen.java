package com.slamdunk.wordarena.gameparts.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.gameparts.components.ParticleRendererComponent;
import com.slamdunk.toolkit.gameparts.components.PathComponent;
import com.slamdunk.toolkit.gameparts.components.SpriteRendererComponent;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;
import com.slamdunk.toolkit.gameparts.gameobjects.Sprite;
import com.slamdunk.toolkit.gameparts.scene.Scene;
import com.slamdunk.wordarena.gameparts.SVGLoader;
import com.slamdunk.wordarena.gameparts.prefabs.Castle;
import com.slamdunk.wordarena.gameparts.prefabs.Paladin;
import com.slamdunk.wordarena.gameparts.scripts.FollowScript;

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
		SVGLoader sceneLoader = new SVGLoader();
		sceneLoader.load("battlefields/battlefield0.svg");
		
		// Place la caméra au centre de l'écran
		scene.observationPoint.transform.relativePosition.x = scene.observationPoint.camera.viewportWidth / 2;
		scene.observationPoint.transform.relativePosition.y = scene.observationPoint.camera.viewportHeight / 2;
		scene.observationPoint.addComponent(FollowScript.class);
		
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
		paladin.getComponent(SpriteRendererComponent.class).origin.set(0.5f,0.5f); // L'origine des rotations est le centre
		paladin.getComponent(SpriteRendererComponent.class).anchor.set(0.5f,0.5f); // Position toujours exprimée par rapport au centre
		paladin.getComponent(PathComponent.class).path = sceneLoader.paths.get(0);
		paladin.getComponent(PathComponent.class).speed = 50;
		
		GameObject dot = paladin.addChild();
		dot.addComponent(SpriteRendererComponent.class);
		dot.getComponent(SpriteRendererComponent.class).spriteFile = "textures/dot.png";
		dot.transform.relativePosition.set(16, 16, 0);
		dot.addComponent(ParticleRendererComponent.class);
		dot.getComponent(ParticleRendererComponent.class).effectFile = "particles/firebeam.p";
		dot.getComponent(ParticleRendererComponent.class).imagesDirectory = "particles";
		dot.getComponent(ParticleRendererComponent.class).active = false;
		
		GameObject dot2 = scene.addGameObject();
		dot2.addComponent(SpriteRendererComponent.class);
		dot2.getComponent(SpriteRendererComponent.class).spriteFile = "textures/dot.png";
		dot2.transform.relativePosition.set(160, 160, 0);
		
		scene.observationPoint.getComponent(FollowScript.class).target = paladin;
		scene.observationPoint.getComponent(FollowScript.class).leech = 100;
		scene.observationPoint.getComponent(FollowScript.class).reachTime = 1;
		
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
