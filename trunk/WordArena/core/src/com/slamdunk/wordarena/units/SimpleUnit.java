package com.slamdunk.wordarena.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.slamdunk.toolkit.world.SlamActor;
import com.slamdunk.toolkit.world.pathfinder.Path;
import com.slamdunk.toolkit.world.pathfinder.PathCursor;
import com.slamdunk.toolkit.world.point.Point;
import com.slamdunk.wordarena.ai.States;
import com.slamdunk.wordarena.screens.GameScreen;

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
	 * Le camp dans lequel se trouve l'unité
	 */
	private Factions faction;
	
	private float speed;
	private PathCursor pathCursor;
	
	private States state;
	private int hp;
	
	public SimpleUnit(GameScreen game, Factions faction, String textureFile) {
		this.game = game;
		this.faction = faction;
		speed = 1;
		state = States.IDLE;
		
		initUnit(textureFile);
	}
	
	private void initUnit(String textureFile) {
		TextureRegion textureRegion = new TextureRegion(new Texture(Gdx.files.internal("textures/" + textureFile)));
		
		createDrawers(true, false, false);
		
		getTextureDrawer().setTextureRegion(textureRegion);
		getTextureDrawer().setActive(true);
		
		final float pixelsByUnit = game.getPixelsByUnit();
		setSize(textureRegion.getRegionWidth() / pixelsByUnit, textureRegion.getRegionHeight() / pixelsByUnit);
	}
	
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
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
	
	public PathCursor getPathCursor() {
		return pathCursor;
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
	}

	/**
	 * Fait suivre le chemin. Si path == null , alors
	 * l'acteur ne suit plus aucun chemin.
	 * @param path
	 */
	public void follow(Path path) {
		if (path == null) {
			pathCursor = null;
		} else {
			pathCursor = new PathCursor(path);
			setState(States.MOVING);
		}
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
		game.removeUnit(this);
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
		if (pathCursor != null && getActions().size == 0) {
			if (pathCursor.isArrivalReached()) {
				handleEventArrivedAtDestination();
			} else {
				handleEventMovedOnePosition();
				
				Point destination = pathCursor.current();
				if (getX() == destination.getX()
				&& getY() == destination.getY()){
					destination = pathCursor.next();
				}
				
				//Point nextPosition = pathCursor.next();
				addAction(Actions.moveTo(destination.getX(), destination.getY(), 1 / speed));
			}
		}
	}

	/**
	 * Appelée lorsque l'unité arrive à destination
	 */
	protected void handleEventArrivedAtDestination() {
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
	protected void handleEventReceiveDamage(SimpleUnit attacker, int damage) {
		hp -= damage;
		if (hp <= 0) {
			setState(States.DYING);
		}
	}

	/**
	 * Retourne la position de l'unité
	 * @return
	 */
	public Point getPosition() {
		if (pathCursor == null) {
			return null;
		}
		return pathCursor.current();
	}

	@Override
	public String toString() {
		return "Unit " + hp + "HP" + getPosition();
	}
}
