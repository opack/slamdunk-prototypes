package com.slamdunk.toolkit.gameparts.gameobjects;

import java.util.LinkedHashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.components.TransformComponent;
import com.slamdunk.toolkit.gameparts.scene.Layer;

/**
 * Objet du jeu. C'est un simple agrégat de composants.
 */
public class GameObject {
	private static final String DEFAULT_NAME_PREFIX = "GameObject";
	private static long gameObjectsCount;
	
	/**
	 * Indique si le GameObject doit être unique dans la scène.
	 * Si true, une erreur sera levée si un autre GameObject de cette
	 * classe tente d'être ajouté.
	 */
	public static boolean unique;
	
	private Map<Class<? extends Component>, Component> components;
	
	public Layer layer;
	
	public String name;
	
	public boolean active;
	
	public TransformComponent transform;
	
	public GameObject() {
		// Détermine un nom par défaut
		gameObjectsCount++;
		name = DEFAULT_NAME_PREFIX + gameObjectsCount;
		
		// Par défaut, le gameObject est actif
		active = true;
		
		// Crée la table de composants
		components = new LinkedHashMap<Class<? extends Component>, Component>();
		
		// Ajoute le premier composant, qui permet de positionner le GameObject dans le monde
		transform = new TransformComponent();
		addComponent(transform);
	}
	
	public boolean isUnique() {
		return unique;
	}

	public void addComponent(Component component) {
		if (component.isUnique()
		&& components.get(component.getClass()) != null) {
			throw new IllegalStateException("There must only be one instance of " + component.getClass() + " in a GameObject, as this Component is marked as unique.");
		}
		
		component.gameObject = this;
		components.put(component.getClass(), component);
	}
	
	public <T extends Component> T getComponent(Class<T> componentClass) {
		return componentClass.cast(components.get(componentClass));
	}
	
	/**
	 * Initialise les composant du GameObject en appelant la méthode
	 * init() de chacun des composants, dans l'ordre où ils ont
	 * été ajoutés.
	 * Attention : cette méthode appelle init() sur chaque composant
	 * même s'il est inactif.
	 */
	public void init() {
		for (Component component : components.values()) {
			component.init();
		}
	}
	
	/**
	 * Met à jour la logique du GameObject en appelant la méthode
	 * update() de chacun des composants, dans l'ordre où ils ont
	 * été ajoutés.
	 * Attention : cette méthode appelle update() uniquement sur 
	 * les composants actifs.
	 */
	public void update(float deltaTime) {
		for (Component component : components.values()) {
			if (component.active) {
				component.update(deltaTime);
			}
		}
	}
	
	/**
	 * Dessine le GameObject en appelant la méthode
	 * render() de chacun des composants, dans l'ordre où ils ont
	 * été ajoutés.
	 * Attention : cette méthode appelle draw() uniquement sur 
	 * les composants actifs.
	 */
	public void render(Batch batch) {
		for (Component component : components.values()) {
			if (component.active) {
				component.render(batch);
			}
		}
	}
}
