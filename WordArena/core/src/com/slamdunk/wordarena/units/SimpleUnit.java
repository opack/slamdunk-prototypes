package com.slamdunk.wordarena.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.slamdunk.toolkit.lang.DoubleEntryArray;
import com.slamdunk.toolkit.world.Directions4;
import com.slamdunk.toolkit.world.SlamActor;
import com.slamdunk.toolkit.world.path.ComplexPath;
import com.slamdunk.toolkit.world.path.ComplexPathCursor;
import com.slamdunk.toolkit.world.path.CursorMode;
import com.slamdunk.toolkit.world.path.PathUtils;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.game.GameScreen;

/**
 * Une unité lambda. Son IA est gérée de la façon suivante :
 *   - handleEvent*() gère les évènements survenus sur cette unité.
 *   Ces évènements peuvent provoquer un changement d'état de l'unité.
 *   Ils peuvent être envoyés par l'unité elle-même ou une autre unité.
 *   - act() est appelée périodiquement et effectue une action en
 *   fonction de l'état de l'unité. Cette méthode appelle les méthodes
 *   perform*() associées à chaque état.
 */
public class SimpleUnit extends SlamActor {

	/**
	 * L'écran de jeu
	 */
	private GameScreen game;
	
	/**
	 * Le type d'unité
	 */
	private Units type;
	
	/**
	 * Le camp dans lequel se trouve l'unité
	 */
	private Factions faction;
	
	/**
	 * Vitesse de déplacement (en cases par secondes)
	 */
	private float speed;
	
	/**
	 * Curseur de déplacement qui permet de suivre un chemin
	 */
	private ComplexPathCursor pathCursor;
	
	/**
	 * Le chemin sur lequel se déplace l'unité
	 */
	private ComplexPath path;
	
	/**
	 * La direction dans laquelle regarde l'unité
	 */
	private Directions4 direction;
	
	/**
	 * L'état qui identifie l'action en cours
	 */
	private States state;
	
	/**
	 * L'état au moment du précédent appel à act()
	 */
	private States previousState;
	
	/**
	 * Nombre de points de vie restants
	 */
	private float hp;
	
	/**
	 * Table stockant les animations associées à chaque direction pour
	 * chacun des état possibles
	 */
	private DoubleEntryArray<States, Directions4, Animation> animations;
	
	/**
	 * Vecteurs de travail pour la méthode performMove()
	 */
	private Vector2 tmpMoveCurrent;
	private Vector2 tmpMoveDestination;
	
	public SimpleUnit(GameScreen game, Units type) {
		this.game = game;
		this.type = type;
		direction = Directions4.RIGHT;
		speed = 1;
		state = States.IDLE;
		previousState = States.IDLE;
		animations = new DoubleEntryArray<States, Directions4, Animation>();
		
		tmpMoveCurrent = new Vector2();
		tmpMoveDestination = new Vector2();
	}
	
	public Units getType() {
		return type;
	}

	/**
	 * Définit l'animation à utiliser pour une direction donnée et l'état donné
	 * @param direction
	 * @param animation
	 */
	public void setAnimation(States state, Directions4 direction, Animation animation) {
		animations.put(state, direction, animation);
	}
	
	public void initTextureRendering(String textureFile) {
		TextureRegion textureRegion = new TextureRegion(new Texture(Gdx.files.internal("textures/" + textureFile)));
		
		createDrawers(true, false, false);
		
		getTextureDrawer().setTextureRegion(textureRegion);
		getTextureDrawer().setActive(true);
		
		final float pixelsByUnit = 1;//DBGgame.getPixelsByUnit();
		setSize(textureRegion.getRegionWidth() / pixelsByUnit, textureRegion.getRegionHeight() / pixelsByUnit);
	}
	
	public void initAnimationRendering(float preferedPixelsWidth, float preferedPixelsHeight) {
		createDrawers(false, true, false);
		getAnimationDrawer().setActive(true);
		final float pixelsByUnit = 1;//DBGgame.getPixelsByUnit();
		setSize(preferedPixelsWidth / pixelsByUnit, preferedPixelsHeight / pixelsByUnit);
	}
	
	public Directions4 getDirection() {
		return direction;
	}

	public States getPreviousState() {
		return previousState;
	}

	/**
	 * Change la direction de l'unité et donc éventuellement l'animation
	 * en cours
	 * @param direction
	 */
	public void setDirection(Directions4 direction) {
		this.direction = direction;
		chooseAnimation();
	}

	public float getHp() {
		return hp;
	}

	public void setHp(float hp) {
		this.hp = hp;
	}

	public GameScreen getGame() {
		return game;
	}

	public Factions getFaction() {
		return faction;
	}

	public void setFaction(Factions faction) {
		this.faction = faction;
	}

	public float getSpeed() {
		return speed;
	}

	/**
	 * Définit la vitesse de l'acteur, en unités du monde par seconde
	 * @param speed
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public ComplexPathCursor getPathCursor() {
		return pathCursor;
	}
	
	public ComplexPath getPath() {
		return path;
	}
	
	/**
	 * Fait suivre le chemin. Si path == null , alors
	 * l'acteur ne suit plus aucun chemin.
	 * @param path
	 * @param b 
	 */
	public void setPath(ComplexPath path) {
		setPath(path, 0);
	}
	
	/**
	 * Fait suivre le chemin en partant de l'extremité
	 * ayant le nom indiqué.
	 * Si aucune extrémité n'a ce nom, l'unité part
	 * de l'extrémité t=0.
	 * @param path
	 * @param endName
	 */
	public void setPath(ComplexPath path, String extremityName) {
		// Récupère la valeur t (donc l'extrémité) depuis laquelle on part
		float startT = PathUtils.getExtremityByName(path, extremityName);
		if (startT == -1) {
			return;
		}
		
		// Envoie l'unité vers cette destination
		setPath(path, startT);
	}
	
	/**
	 * Fait suivre le chemin. Si path == null , alors
	 * l'acteur ne suit plus aucun chemin.
	 * @param path
	 * @param startIndexInPath 
	 */
	public void setPath(ComplexPath path, float startT) {
		setPath(path, startT, null);
	}

	/**
	 * Fait suivre le chemin. Si path == null , alors
	 * l'acteur ne suit plus aucun chemin.
	 * @param path
	 * @param startIndexInPath 
	 */
	public void setPath(ComplexPath path, float startT, CursorMode cursorMode) {
		this.path = path;
		if (path == null) {
			pathCursor = null;
		} else {
			// Récupère l'indice du segment correspondant à ce t
			int segmentIndex = path.getSegmentIndexFromGlobalT(startT);
			
			// Récupère la valeur de t localisée à ce segment
			float localT = path.convertToLocalT(startT, segmentIndex);
			
			// Place le curseur à l'endroit indiqué sur le chemin
			if (cursorMode == null) {
				cursorMode = CursorMode.FORWARD;
			}
			pathCursor = new ComplexPathCursor(path, getSpeed(), cursorMode);
			pathCursor.setCurrentSegmentIndex(segmentIndex);
			pathCursor.setPosition(localT);
			
			// Place l'unité à l'endroit du curseur
			pathCursor.valueAt(tmpMoveCurrent);
			setPosition(tmpMoveCurrent.x, tmpMoveCurrent.y);
			
			// Place l'unité en mouvement
			setState(States.MOVING);
		}
	}

	public States getState() {
		return state;
	}

	/**
	 * Attention ! Un appel à cette méthode annule toutes les actions
	 * en cours !
	 * @param state
	 */
	public void setState(States state) {
		this.state = state;
		clearActions();
		chooseAnimation();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		switch (state) {
			case ATTACKING:
				performAttack(delta);
				break;
			case DYING:
				performDeath(delta);
				break;
			case IDLE:
				performIdle(delta);
				break;
			case MOVING:
				performMove(delta);
				break;
			default:
				System.err.println("Unhandled state (" + state + ") !");
				break;			
		}
		
		previousState = state;
	}
	
	/**
	 * Agit sur l'état ATTACKING
	 * @param delta 
	 */
	protected void performAttack(float delta) {
	}
	
	/**
	 * Agit sur l'état DYING
	 * @param delta 
	 */
	protected void performDeath(float delta) {
		if (getAnimationDrawer().isAnimationFinished()) {
			UnitManager.getInstance().removeUnit(this);
		}
	}
	
	/**
	 * Agit sur l'état IDLE
	 */
	protected void performIdle(float delta) {
	}

	/**
	 * Agit sur l'état MOVING
	 */
	protected void performMove(float delta) {
		// S'il reste un chemin à suivre, on le suit
		if (pathCursor != null) {
			tmpMoveCurrent.set(getCenterX(), getCenterY());

			// Avance l'unité
			pathCursor.move(delta, tmpMoveDestination);
			setCenterPosition(tmpMoveDestination.x, tmpMoveDestination.y);
			handleEventMovedOnePosition();
			
			// Applique une rotation à l'unité en fonction de la direction
			Directions4 newDirection = Directions4.getDirection(tmpMoveCurrent, tmpMoveDestination);
			if (newDirection != direction) {
				direction = newDirection;
				chooseAnimation();
			}
				
				
			// Teste si l'unité est arrivée
			if (pathCursor.isArrivalReached()) {
				handleEventArrivedAtDestination();
			}
		}
	}

	/**
	 * Choisit l'animation à utiliser en fonction de l'état de l'unité 
	 * et de la direction dans laquelle elle regarde.
	 * Si une animation existe pour ces paramètres, elle est utilisée.
	 */
	protected void chooseAnimation() {
		Animation animation = animations.get(state, direction);
		if (animation != null) {
			getAnimationDrawer().setAnimation(animation, true, true);
			
			// Place l'origine pour que les rotations se fassent
			// autour du centre de l'unité
			setOrigin(getWidth() / 2, getHeight() / 2);
		}
	}

	/**
	 * Appelée lorsque l'unité arrive à destination
	 */
	protected void handleEventArrivedAtDestination() {
		// Par défaut, on passe en idle
		setState(States.IDLE);
	}

	/**
	 * Appelée lorsque s'est déplacée d'une position
	 */
	protected void handleEventMovedOnePosition() {
	}

	/**
	 * Appelée lorsqu'une unité attaque cette unité
	 * @param attacker
	 * @param damage
	 */
	protected void handleEventReceiveDamage(SimpleUnit attacker, float damage) {
		hp -= damage;
		if (hp <= 0) {
			setState(States.DYING);
		}
	}
	
	/**
	 * Appelée lorsque l'unité pénètre dans un bâtiment
	 * @param building
	 */
	public void onBuildingEntered(SimpleUnit building) {
		// Par défaut, l'unité disparaît
		UnitManager.getInstance().removeUnit(this);
	}
	
	/**
	 * Retourne la position de l'unité
	 * @return
	 */
	public Vector2 getPosition() {
		return new Vector2(getX(), getY());
	}
	
	/**
	 * Retourne la position du centre de l'unité
	 * @return
	 */
	public Vector2 getCenterPosition() {
		return new Vector2(getCenterX(), getCenterY());
	}

	@Override
	public String toString() {
		return "Unit " + hp + "HP" + getPosition();
	}

	/**
	 * Indique si cette unité n'a plus d'HP
	 * @return
	 */
	public boolean isDead() {
		return hp <= 0;
	}
	
	/**
	 * Met à jour le rectangle spécifié avec les coordonnées de l'unité
	 * @param bounds
	 * @param unit
	 */
	public void updateBounds(Rectangle bounds) {
		bounds.x = getX();
		bounds.y = getY();
		bounds.width = getWidth();
		bounds.height = getHeight();
	}
}
