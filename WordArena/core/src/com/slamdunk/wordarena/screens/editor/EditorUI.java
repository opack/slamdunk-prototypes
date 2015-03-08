package com.slamdunk.wordarena.screens.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.slamdunk.toolkit.screen.overlays.UIOverlay;
import com.slamdunk.wordarena.Assets;
import com.slamdunk.wordarena.WordArenaGame;
import com.slamdunk.wordarena.actors.ArenaZone;
import com.slamdunk.wordarena.data.Player;
import com.slamdunk.wordarena.enums.CellTypes;
import com.slamdunk.wordarena.enums.Letters;
import com.slamdunk.wordarena.screens.editor.tools.CellTypeTool;
import com.slamdunk.wordarena.screens.editor.tools.LetterTool;
import com.slamdunk.wordarena.screens.editor.tools.OwnerTool;
import com.slamdunk.wordarena.screens.editor.tools.PowerTool;
import com.slamdunk.wordarena.screens.editor.tools.ZoneTool;
import com.slamdunk.wordarena.utils.Overlap2DUtils;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.SelectBoxItem;
import com.uwsoft.editor.renderer.actor.TextBoxItem;

public class EditorUI extends UIOverlay {
	private EditorScreen screen;
	
	public EditorUI(EditorScreen screen) {
		this.screen = screen;
		
		// Par défaut, on travaillera dans un Stage qui prend tout l'écran
		createStage(new FitViewport(WordArenaGame.SCREEN_WIDTH, WordArenaGame.SCREEN_HEIGHT));
		
		// Charge les éléments de la scène Overlap2D
		loadScene();
	}
	
	private void loadScene() {
		SceneLoader sceneLoader = new SceneLoader(Assets.overlap2dResourceManager);
		sceneLoader.loadScene("Editor");
		getStage().addActor(sceneLoader.sceneActor);
		
		// Bouton Change Size
		final TextBoxItem txtWidth = (TextBoxItem)sceneLoader.sceneActor.getItemById("txtWidth");
		final TextBoxItem txtHeight = (TextBoxItem)sceneLoader.sceneActor.getItemById("txtHeight");
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnChangeSize", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				int width = Integer.parseInt(txtWidth.getText());
				int height = Integer.parseInt(txtHeight.getText());
				screen.changeArenaSize(width, height);
			}
		});
		
		// Choix du type de cellule
		@SuppressWarnings("unchecked")
		final SelectBoxItem<CellTypes> selType = (SelectBoxItem<CellTypes>) sceneLoader.sceneActor.getItemById("selType");
		selType.setWidth(150);
		selType.setItems(CellTypes.values());
		selType.setSelected(screen.getTool(CellTypeTool.class).getValue());
		selType.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				CellTypeTool tool = screen.getTool(CellTypeTool.class);
				tool.setValue(selType.getSelected());
			}
		});
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnToolType", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				screen.setCurrentTool(CellTypeTool.class);
			}
		});
		
		// Choix de la lettre
		@SuppressWarnings("unchecked")
		final SelectBoxItem<Letters> selLetter = (SelectBoxItem<Letters>)sceneLoader.sceneActor.getItemById("selLetter");
		selLetter.setWidth(150);
		selLetter.setItems(Letters.values());
		selLetter.setSelected(screen.getTool(LetterTool.class).getValue());
		selLetter.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				LetterTool tool = screen.getTool(LetterTool.class);
				tool.setValue(selLetter.getSelected());
			}
		});
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnToolLetter", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				screen.setCurrentTool(LetterTool.class);
			}
		});
		
		// Bouton Power
		final TextBoxItem txtPower = (TextBoxItem)sceneLoader.sceneActor.getItemById("txtPower");
		txtPower.setText(screen.getTool(PowerTool.class).getValue().toString());
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnToolPower", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				int power = Integer.parseInt(txtPower.getText());
				PowerTool tool = screen.getTool(PowerTool.class);
				tool.setValue(power);
				screen.setCurrentTool(PowerTool.class);
			}
		});
		
		// Bouton Owner
		Array<Player> players = new Array<Player>();
		players.add(Player.NEUTRAL);
		players.add(new Player(1, Assets.i18nBundle.get("ui.editor.player.1"), "blue"));
		players.add(new Player(2, Assets.i18nBundle.get("ui.editor.player.2"), "orange"));
		players.add(new Player(3, Assets.i18nBundle.get("ui.editor.player.3"), "green"));
		players.add(new Player(4, Assets.i18nBundle.get("ui.editor.player.4"), "purple"));
		@SuppressWarnings("unchecked")
		final SelectBoxItem<Player> selOwner = (SelectBoxItem<Player>)sceneLoader.sceneActor.getItemById("selOwner");
		selOwner.setWidth(150);
		selOwner.setItems(players);
		selOwner.setSelected(screen.getTool(OwnerTool.class).getValue());
		selOwner.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				OwnerTool tool = screen.getTool(OwnerTool.class);
				tool.setValue(selOwner.getSelected());
			}
		});
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnToolOwner", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				screen.setCurrentTool(OwnerTool.class);
			}
		});
		
		// Bouton Zone
		final TextBoxItem txtZone = (TextBoxItem)sceneLoader.sceneActor.getItemById("txtZone");
		txtZone.addListener(new ChangeListener() {
			// DBG NE FONCTIONNE PAS !
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				ArenaZone zone = screen.getOrCreateZone(txtZone.getText());
				ZoneTool tool = screen.getTool(ZoneTool.class);
				tool.setValue(zone);
			}
		});
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnToolZone", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				screen.setCurrentTool(ZoneTool.class);
			}
		});
		
		// Bouton Create wall
		Overlap2DUtils.createSimpleButtonScript(sceneLoader, "btnToolCreateWall", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
			}
		});
	}
}
