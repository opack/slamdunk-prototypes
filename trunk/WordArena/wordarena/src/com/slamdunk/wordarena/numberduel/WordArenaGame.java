package com.slamdunk.wordarena.numberduel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.slamdunk.utils.ui.ButtonClickListener;
import com.slamdunk.wordarena.numberduel.Bonus;
import com.slamdunk.wordarena.numberduel.GridCell;
import com.slamdunk.wordarena.numberduel.Numbers;
import com.slamdunk.wordarena.numberduel.Player;
import com.slamdunk.wordarena.numberduel.Rules;

public class WordArenaGame implements ApplicationListener {
	private static final int NB_ROUNDS_BY_MATCH = 6;
	private static final int NB_TURNS_BY_ROUND = 3;
	private static final float TIMER_UPDATE_INTERVAL = 1;
	private static final int FAVORITE_NUMBER_BONUS = 10;
	private static final int PLAYER_HP = 200;
	private static final int[/*COLONNE*/][/*LIGNE*/] NEIGHBORS = new int[][] {
		{-1, -1},
		{+1, -1},
		{0, -1},
		{-1, 0},
		{+1, 0},
		{-1, +1},
		{0, +1},
		{+1, +1},
	};
	
	private Stage stage;
	private Skin skin;
	
	private int gridWidth;
	private int gridHeight;
	private GridCell[][] cells;
	private LinkedList<GridCell> selected;
	
	private float timer;
	private float elapsed;
	private boolean counting;
	
	private Player p1;
	private Player p2;
	private int round;
	private int turn;
	private int score;
	
	private Rules rule;
	private int[] favoriteNumber;
	
	@Override
	public void create() {
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("skins/default-skin/default-skin.json"));
		
		selected = new LinkedList<GridCell>();
		
		p1 = new Player("Joueur 1");
		p2 = new Player("Joueur 2");

		favoriteNumber = new int[2];
		
		createUI(skin);
		initInput();
	}
	
	private void initInput() {
		InputProcessor proc = new InputAdapter() {
			private Vector2 screenCoords = new Vector2();
			private Vector2 stageCoords;
			
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				// Calcul des coordonnées pour le stage
				screenCoords.set(screenX, screenY);
				stageCoords = stage.screenToStageCoordinates(screenCoords);
				
				// Récupération de l'éventuel bouton
				Actor hit = stage.hit(stageCoords.x, stageCoords.y, true);
				if (hit != null && (hit.getParent() instanceof TextButton)) {
					// Récupère la cellule associée et sélectionne la lettre
					GridCell cell = getCell((TextButton)hit.getParent());
					if (cell != null) {
						select(cell, true);
						return true;
					}
				}
				return false;
			}
		};
		Gdx.input.setInputProcessor(new InputMultiplexer(proc, stage));
	}

	private void newGame(int gridWidth, int gridHeight) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		
		cells = new GridCell[gridWidth][gridHeight];
		round = 0;
		turn = 0;
		score = 0;
		
		p1.hp = PLAYER_HP;
		p1.updateLabel();
		p2.hp = PLAYER_HP;
		p2.updateLabel();
		
		rule = Rules.FREE;
		nextTurn();
		showGrid(false);
	}
	
	private void endGame() {
		showGrid(false);
		TextButton start = (TextButton)stage.getRoot().findActor("start");
		start.setVisible(false);
		
		resetTimer();
	}

	public GridCell getCell(Button button) {
		if (button != null && cells != null) {
			for (GridCell[] line : cells) {
				for (GridCell cell : line) {
					if (button.equals(cell.button)) {
						return cell;
					}
				}
			}
		}
		return null;
	}

	private void createUI(Skin skin) {
		Label round = new Label("Round X", skin, "text");
		round.setName("round");
		round.setWidth(480);
		round.setPosition(0, 735);
		round.setAlignment(Align.center);
		stage.addActor(round);
		
		Label time = new Label("01:00", skin, "text");
		time.setName("time");
		time.setWidth(480);
		time.setPosition(0, 680);
		time.setAlignment(Align.center);
		stage.addActor(time);
		
		p1.label = new Label(p1.name + "\n" + p1.hp, skin, "text");
		p1.label.setName("j1HP");
		p1.label.setWidth(240);
		p1.label.setPosition(0, 650);
		p1.label.setAlignment(Align.left);
		stage.addActor(p1.label);
		
		p2.label = new Label(p2.name + "\n" + p2.hp, skin, "text");
		p2.label.setName("j2HP");
		p2.label.setWidth(240);
		p2.label.setPosition(240, 650);
		p2.label.setAlignment(Align.right);
		stage.addActor(p2.label);
		
		Label score = new Label("", skin, "text");
		score.setName("score");
		score.setWidth(480);
		score.setPosition(0, 630);
		score.setAlignment(Align.center);
		stage.addActor(score);
		
		TextButton validate = new TextButton("Valider", skin, "validate");
		validate.setName("validate");
		validate.setPosition(240 - validate.getWidth() - 20, 550);
		validate.addListener(new ButtonClickListener() {
			@Override
			public void clicked(Button button) {
				validate();
			}
		});
		validate.setVisible(false);
		stage.addActor(validate);
		
		TextButton cancel = new TextButton("Annuler", skin, "cancel");
		cancel.setName("cancel");
		cancel.setPosition(240 + 20, 550);
		cancel.addListener(new ButtonClickListener() {
			@Override
			public void clicked(Button button) {
				reset();
			}
		});
		cancel.setVisible(false);
		stage.addActor(cancel);
		
		final TextButton start = new TextButton("GO !", skin, "validate");
		start.setName("start");
		start.setPosition(240 - start.getWidth() / 2, 550);
		start.addListener(new ButtonClickListener() {
			@Override
			public void clicked(Button button) {
				counting = true;
				showGrid(true);
			}
		});
		start.setVisible(false);
		stage.addActor(start);
		
		Label rule = new Label("", skin, "text-small");
		rule.setName("rule");
		rule.setWidth(480);
		rule.setPosition(0, 500);
		stage.addActor(rule);
		
		Label favorite = new Label("", skin, "text-small");
		favorite.setName("favorite");
		favorite.setWidth(480);
		favorite.setPosition(0, 480);
		stage.addActor(favorite);
		
		Table gridTable = new Table(skin);
		gridTable.setName("grid");
		stage.addActor(gridTable);
		
		TextButton newGame5 = new TextButton("5", skin, "new_game");
		newGame5.setName("new_game_5");
		newGame5.setPosition(5, 731);
		newGame5.addListener(new ButtonClickListener() {
			@Override
			public void clicked(Button button) {
				newGame(5, 5);
			}
		});
		stage.addActor(newGame5);
		
		TextButton newGame6 = new TextButton("6", skin, "new_game");
		newGame6.setName("new_game_6");
		newGame6.setPosition(74, 731);
		newGame6.addListener(new ButtonClickListener() {
			@Override
			public void clicked(Button button) {
				newGame(6, 6);
			}
		});
		stage.addActor(newGame6);
	}

	private void initGrid(Skin skin) {
		// Crée une liste de nombre en tenant compte
		// de la représentativité de chacun
		List<Numbers> numbers = new ArrayList<Numbers>();
		for (Numbers number : Numbers.values()) {
			for (int count = 0; count < number.getRepresentation(); count++) {
				numbers.add(number);
			}
		}
		
		// Mélange la liste
		Collections.shuffle(numbers);
		
		// Remplit la grille
		int idx = 0;
		for (int y = 0; y < gridHeight; y++) {
			for (int x = 0; x < gridWidth; x++) {
				final GridCell cell = new GridCell();
				cell.x = x;
				cell.y = y;
				cell.value = numbers.get(idx);
				cell.bonus = Bonus.SIMPLE_LETTER;
				cell.button = new TextButton(String.valueOf(cell.value.getValue()), skin, "grid-number-1L");
				cell.button.addListener(new ButtonClickListener() {
					@Override
					public void clicked(Button button) {
						select(cell, false);
					}
				});
				cells[x][y] = cell;
				idx++;
			}
		}
		
		// Choisit des bonus et les place dans la grille
		int nbBonus = MathUtils.random(3, gridWidth);
		Bonus[] bonuses = Bonus.values();
		for (int curBonus = 0; curBonus < nbBonus; curBonus++) {
			// Choix d'un bonus au hasard
			Bonus bonus = bonuses[MathUtils.random(bonuses.length - 1)];
			
			// Choix d'une position sans bonus au hasard
			do {
				int x = MathUtils.random(gridWidth - 1);
				int y = MathUtils.random(gridHeight - 1);
				GridCell cell = cells[x][y];
				if (cell.bonus == Bonus.SIMPLE_LETTER) {
					cell.bonus = bonus;
					cell.button.setStyle(skin.get(bonus.getNormalStyle(), TextButtonStyle.class));
					break;
				}
			} while (true);
		}
		
		// Ajoute les boutons au stage
		Table gridTable = (Table)stage.getRoot().findActor("grid");
		gridTable.clear();
		for (int y = 0; y < gridHeight; y++) {
			for (int x = 0; x < gridWidth; x++) {
				gridTable.add(cells[x][y].button).pad(3);
			}
			gridTable.row();
		}
		gridTable.pack();
		gridTable.setPosition((stage.getWidth() - gridTable.getWidth()) / 2, 10);
	}
	
	private void select(GridCell cell, boolean swiping) {
		// Si on sélectionne une cellule déjà sélectionnée, on regarde
		// s'il n'y a rien à faire ou si on la désélectionne
		String style = cell.bonus.getNormalStyle();
		if (selected.contains(cell)) {
			// Si on clique sur la dernière case sélectionnée, on la désélectionne
			if (!swiping && cell.equals(selected.getLast())) {
				// Retire la cellule
				selected.remove(cell);
			} else {
				// On s'assure que le style restera selected
				style = cell.bonus.getSelectedStyle();
			}
		}
		// Si on sélectionne une case valide, on l'ajoute à la liste des sélections
		// et on change le style du bouton
		else if (!cell.button.isDisabled() && rule.isValidCell(cell)) {
			// On ajoute cette case à la sélection si c'est la première ou qu'on a
			// le droit de la sélectionner après la précédente
			if (selected.isEmpty() || rule.isValidNextCell(selected.getLast(), cell)){
				selected.add(cell);
				style = cell.bonus.getSelectedStyle();
			}
		}
		
		// Met à jour le style du bouton
		TextButtonStyle buttonStyle = skin.get(style, TextButtonStyle.class);
		cell.button.setStyle(buttonStyle);
		
		// Mise à jour du score
		updateScore();
	}
	
	private void validate() {
		// Retire le score à l'HP de l'autre joueur
		// ou le rajoute au joueur courant
		Player taker = p1;
		if ((score > 0 && turn % 2 == 0)
		|| (score < 0 && turn % 2 != 0)) {
			taker = p2;
		}
		
		taker.hp += score;
		if (taker.hp <= 0) {
			taker.label.setText(taker.name + "\nKO !");
			
			// La partie est terminée, on cache la grille
			endGame();
		} else {
			// Met à jour le libellé de HP
			taker.updateLabel();
			
			// Cache la grille
			showGrid(false);
			
			// Désactive les cases utilisées
			for (GridCell cell : selected) {
				cell.button.setDisabled(true);
			}
			
			// Prépare le prochain tour
			nextTurn();
		}
	}
	
	private void nextTurn() {
		// Arrêt du chrono
		counting = false;
		
		// Raz de la sélection
		reset();
		
		// Passe au tour suivant et si nécessaire au round suivant
		turn++;
		if ((turn - 1) % NB_TURNS_BY_ROUND + 1 == 1) {
			if (!nextRound()) {
				// Si on ne passe pas au prochain round, la partie est finie
				return;
			}
		}
		updateRoundLabel();
		
		// Nouveau nombre fétiche
		chooseFetichNumber();
		
		// Réinitialise le timer
		resetTimer();
	}
	
	private void updateRoundLabel() {
		String player = turn % 2 != 0 ? p1.name : p2.name;
		Label roundLabel = (Label)stage.getRoot().findActor("round");
		roundLabel.setText("Round " + round + " - Coup " + ((turn - 1) % NB_TURNS_BY_ROUND + 1) + "\n" + player);
	}

	private boolean nextRound() {
		// Nouveau round
		round++;
		
		if (round <= NB_ROUNDS_BY_MATCH) {
			// Nouvelle règle de jeu pour chaque round
			chooseRule();
			
			// Nouvelle grille
			initGrid(skin);
			
			return true;
		} else {
			// Déclare le TKO
			if (p1.hp > p2.hp) {
				p2.label.setText(p2.name + "\nTKO !");
			} else if (p2.hp > p1.hp) {
				p1.label.setText(p1.name + "\nTKO !");
			} else {
				Label roundLabel = (Label)stage.getRoot().findActor("round");
				roundLabel.setText("MATCH NUL !");
			}
			// La partie est terminée, on cache la grille et le bouton start
			endGame();
			return false;
		}
	}

	private void chooseFetichNumber() {
		// Récupère toutes les cellules (hormis les bords) et les mélange
		List<GridCell> cellsList = new ArrayList<GridCell>();
		for (int x = 1; x < gridWidth - 1; x++) {
			for (int y = 1; y < gridHeight - 1; y++) {
				cellsList.add(cells[x][y]);
			}
		}
		Collections.shuffle(cellsList);
		
		// RAZ nombre fétiche
		favoriteNumber[0] = -1;
		favoriteNumber[1] = -1;
		
		// Choix d'une nouveau nombre fétiche
		GridCell secondCell;
		searchFavorite : for (GridCell firstCell : cellsList) {
			// On s'assure que la première cellule est valide pour la règle
			if (firstCell.button.isDisabled()
			|| !rule.isValidCell(firstCell)) {
				continue;
			}
			
			// Récupère les voisins de cette cellule
			for (int[] neighbor : NEIGHBORS) {
				secondCell = cells[firstCell.x + neighbor[0]][firstCell.y + neighbor[1]];
				if (rule.isValidCell(secondCell)
				&& rule.isValidNextCell(firstCell, secondCell)) {
					favoriteNumber[0] = firstCell.value.getValue();
					favoriteNumber[1] = secondCell.value.getValue();
					Arrays.sort(favoriteNumber);
					break searchFavorite;
				}
			}
		}

		// Affichage du nombre fétiche
		Label label = (Label)stage.getRoot().findActor("favorite");
		if (favoriteNumber[0] != -1) {
			label.setText("Nombre fétiche (+10pts) : " + favoriteNumber[0] + favoriteNumber[1]);
		} else {
			label.setText("Nombre fétiche (+10pts) : Aucun possible respectant la règle");
		}
	}

	private void chooseRule() {
		// Construit une liste en prenant en compte la représentation de chaque règle
		List<Rules> rules = new ArrayList<Rules>();
		for (Rules rule : Rules.values()) {
			for (int count = 0; count < rule.getRepresentation(); count++) {
				rules.add(rule);
			}
		}
		Collections.shuffle(rules);
		
		// Choisit une règle aléatoirement dans la liste
		rule = rules.get(0);
		
		// Met à jour le libellé
		Label label = (Label)stage.getRoot().findActor("rule");
		label.setText("Règle du round : " + rule.getRule());
	}

	private void reset() {
		selected.clear();
		for (int y = 0; y < gridHeight; y++) {
			for (int x = 0; x < gridWidth; x++) {
				GridCell cell = cells[x][y];
				if (cell != null) {
					TextButtonStyle style = skin.get(cell.bonus.getNormalStyle(), TextButtonStyle.class);
					cell.button.setStyle(style);
				}
			}
		}
		updateScore();
	}
	
	private void updateScore() {
		score = 0;
		GridCell previous = null;
		for (GridCell cell : selected) {
			// Applique le bonus sur le chiffre
			score -= cell.bonus.getLetterValue(cell.value.getValue());
			
			// Vérifie si on a le nombre fétiche
			if (previous != null
			&& previous.value.getValue() == favoriteNumber[0]
			&& cell.value.getValue() == favoriteNumber[1]) {
				score -= FAVORITE_NUMBER_BONUS;
			}
			previous = cell;
		}
		// Applique les bonus sur le total
		for (GridCell cell : selected) {
			score = cell.bonus.getTotalValue(score);
		}
		
		Label label = (Label)stage.getRoot().findActor("score");
		label.setText(score > 0 ? "+" + score : String.valueOf(score));
		int align = Align.center;
		if (score < 0) {
			// Si on fait des dégâts et que J1 joue, alors les dégâts sont pour le J2 donc le texte sera à droite, et inversement.
			align = turn % 2 != 0 ? Align.right : Align.left;
		} else {
			// Si on récupère de la vie et que J1 joue, alors le texte sera à gauhe, sinon à droite
			align = turn % 2 != 0 ? Align.left : Align.right;
		}
		label.setAlignment(align);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		updateTimer();
		
		stage.draw();
	}

	private void updateTimer() {
		if (!counting || timer <= 0) {
			return;
		}
		elapsed += Gdx.graphics.getDeltaTime();
		if (elapsed >= TIMER_UPDATE_INTERVAL) {
			timer -= elapsed;
			elapsed = 0;
			
			updateTimerLabel();
			
			if (timer <= 0) {
				validate();
			}
		}
	}

	private void showGrid(boolean show) {
		Table gridTable = (Table)stage.getRoot().findActor("grid");
		gridTable.setVisible(show);
		TextButton start = (TextButton)stage.getRoot().findActor("start");
		start.setVisible(!show);
		TextButton validate = (TextButton)stage.getRoot().findActor("validate");
		validate.setVisible(show);
		TextButton cancel = (TextButton)stage.getRoot().findActor("cancel");
		cancel.setVisible(show);
	}

	private void resetTimer() {
		timer = 60;
		elapsed = 0;
		counting = false;
		updateTimerLabel();
	}
	
	private void updateTimerLabel() {
		int minutes = (int)(timer / 60);
		int seconds = (int)(timer % 60);
		
		Label label = (Label)stage.getRoot().findActor("time");
		label.setText(String.format("%02d:%02d", minutes, seconds));
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(480, 800, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
