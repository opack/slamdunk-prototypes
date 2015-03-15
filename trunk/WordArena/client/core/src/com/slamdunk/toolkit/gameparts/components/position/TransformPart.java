package com.slamdunk.toolkit.gameparts.components.position;

import com.badlogic.gdx.math.Vector3;
import com.slamdunk.toolkit.gameparts.components.Component;

/**
 * Exprime la position, rotation et mise à l'échelle de façon relative par rapport
 * aux transformations du TransformComponent du GameObject conteneur, ou absolues.
 * Les positions relatives sont utilisées pour placer le GameObject par rapport à
 * son parent, afin de créer une hiérarchie de GameObjects qui sera toujours cohérente.
 * En revanche, dans la boucle de jeu, ce sont les positions absolues qui sont utilisées
 * pour placer et dessiner le GameObject.
 * Donc : édition -> positions relatives, calcul & rendu -> positions absolues. 
 */
public class TransformPart extends Component {
	/**
	 * Position dans le monde, calculée lors de l'appel
	 * à physics().
	 */
	public Vector3 worldPosition;
	
	/**
	 * Rotation dans le monde, calculée lors de l'appel
	 * à physics().
	 * Lecture seule
	 */
	public Vector3 worldRotation;
	
	/**
	 * Mise à l'échelle dans le monde, calculée lors de l'appel
	 * à physics().
	 * Lecture seule
	 */
	public Vector3 worldScale;
	
	/**
	 * Position relative à celle du parent.
	 */
	public Vector3 relativePosition;
	
	/**
	 * Rotation relative à celle du parent.
	 */
	public Vector3 relativeRotation;
	
	/**
	 * Mise à l'échelle relative à celle du parent.
	 */
	public Vector3 relativeScale;
	
	private TransformPart parentTransform;
	
	public TransformPart() {
		worldPosition = new Vector3();
		worldRotation = new Vector3();
		worldScale = new Vector3();
		
		relativePosition = new Vector3();
		relativeRotation = new Vector3();
		relativeScale = new Vector3();
	}
	
	@Override
	public void reset() {
		worldPosition.set(0, 0, 0);
		worldRotation.set(0, 0, 0);
		worldScale.set(1, 1, 1);
		
		relativePosition.set(0, 0, 0);
		relativeRotation.set(0, 0, 0);
		relativeScale.set(1, 1, 1);
	}
	
	@Override
	public void init() {
		if (gameObject.parent != null) {
			parentTransform = gameObject.parent.transform;
		} else {
			parentTransform = null;
		}
		physics(0);
	}
	
	@Override
	public void physics(float deltaTime) {
		if (parentTransform != null) {
			worldPosition.set(
				relativePosition.x + parentTransform.worldPosition.x,
				relativePosition.y + parentTransform.worldPosition.y,
				relativePosition.z + parentTransform.worldPosition.z);
			worldRotation.set(
				relativeRotation.x + parentTransform.worldRotation.x,
				relativeRotation.y + parentTransform.worldRotation.y,
				relativeRotation.z + parentTransform.worldRotation.z);
			worldScale.set(
				relativeScale.x * parentTransform.worldScale.x,
				relativeScale.y * parentTransform.worldScale.y,
				relativeScale.z * parentTransform.worldScale.z);
		}
	}
}
