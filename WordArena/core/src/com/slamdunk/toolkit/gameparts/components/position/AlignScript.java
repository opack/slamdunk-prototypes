package com.slamdunk.toolkit.gameparts.components.position;

import com.badlogic.gdx.math.Vector3;
import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;

/**
 * Aligne le GameObject conteneur par rapport à une ancre.
 * Un point d'alignement est choisi sur le GameObject conteneur et l'ancre ;
 * le GameObject conteneur sera alors déplacé pour que les 2 points correspondent. 
 * Les scripts de Layout doivent être exécutés en premier (juste après
 * le TransformPart) et doivent donc être ajoutés en premier dans le GameObject.
 */
public class AlignScript extends Component {
	public enum AlignSpots {
		BOTTOM_LEFT,
		BOTTOM_CENTER,
		BOTTOM_RIGHT,
		MIDDLE_LEFT,
		MIDDLE_CENTER,
		MIDDLE_RIGHT,
		TOP_LEFT,
		TOP_CENTER,
		TOP_RIGHT
	}
	
	/**
	 * L'ancre par rapport à laquelle doit être effectué l'alignement
	 */
	public GameObject anchor;
	
	/**
	 * Point du GameObject à aligner
	 */
	public AlignSpots alignSpot;
	
	/**
	 * Point de l'ancre à aligner
	 */
	public AlignSpots anchorAlignSpot;
	
	/**
	 * Si true, l'alignement sera réeffectué à chaque
	 * physics().
	 * Sinon, il ne sera effectué que lorsque le flag
	 * align est positionné à true.
	 */
	public boolean maintainAlignment;
	
	/**
	 * Indique s'il faut refaire l'alignement au prochain
	 * appel à physics()
	 */
	public boolean layout;
	
	private Vector3 anchorWorldPosition;
	private SizePart anchorSize;
	private SizePart size;
	
	private Vector3 tmpAlignSpotPosition;
	private Vector3 tmpAnchorAlignSpotPosition;
	
	public AlignScript() {
		tmpAlignSpotPosition = new Vector3();
		tmpAnchorAlignSpotPosition = new Vector3();
	}
	
	@Override
	public void createDependencies() {
		if (!gameObject.hasComponent(SizePart.class)) {
			gameObject.addComponent(SizePart.class);
		}
	}
	
	@Override
	public void reset() {
		alignSpot = AlignSpots.MIDDLE_CENTER;
		anchorAlignSpot = AlignSpots.MIDDLE_CENTER;
		maintainAlignment = true;
		layout = true;

		size = null;
		anchorWorldPosition = null;
		anchorSize = null;
	}
	
	@Override
	public void init() {
		size = gameObject.getComponent(SizePart.class);
		if (size == null) {
			throw new IllegalStateException("Missing SizePart component. The AlignScript cannot work propertly");
		}
		
		if (anchor == null) {
			throw new IllegalArgumentException("Missing anchor parameter. The AlignScript cannot work propertly");
		}
		anchorWorldPosition = anchor.transform.worldPosition;
		anchorSize = anchor.getComponent(SizePart.class);
	}
	
	@Override
	public void physics(float deltaTime) {
		if (layout || maintainAlignment) {
			// Récupère la taille du parent si elle existe
			float anchorWidth;
			float anchorHeight;
			if (anchorSize != null) {
				anchorWidth = anchorSize.width;
				anchorHeight = anchorSize.height;
			} else {
				anchorWidth = 0;
				anchorHeight = 0;
			}
			
			// Calcule la position des points d'alignement
			computeAlignSpotPosition(gameObject.transform.worldPosition, size.width, size.height, alignSpot, tmpAlignSpotPosition);
			computeAlignSpotPosition(anchorWorldPosition, anchorWidth, anchorHeight, anchorAlignSpot, tmpAnchorAlignSpotPosition);
			
			// Calcule le décalage à effectuer pour que le point d'alignement
			// du GameObject conteneur soit aligné avec celui de l'ancre
			tmpAlignSpotPosition.sub(tmpAnchorAlignSpotPosition);
			
			// Déplace les coordonnées relatives du GameObject conteneur
			// pour que le point d'alignement se trouve au bon endroit
			gameObject.transform.relativePosition.sub(tmpAlignSpotPosition);
			
			layout = false;
		}
	}

	/**
	 * Calcule la position du point d'alignement
	 * @param worldPosition
	 * @param size
	 * @param alignSpot
	 * @param result
	 */
	private void computeAlignSpotPosition(Vector3 worldPosition, float width, float height, AlignSpots alignSpot, Vector3 result) {
		switch (alignSpot) {
		case BOTTOM_LEFT:
			result.set(worldPosition.x, worldPosition.y, worldPosition.z);
			break;
		case BOTTOM_CENTER:
			result.set(worldPosition.x + width / 2, worldPosition.y, worldPosition.z);
			break;
		case BOTTOM_RIGHT:
			result.set(worldPosition.x + width, worldPosition.y, worldPosition.z);
			break;
		case MIDDLE_LEFT:
			result.set(worldPosition.x, worldPosition.y + height / 2, worldPosition.z);
			break;
		case MIDDLE_CENTER:
			result.set(worldPosition.x + width / 2, worldPosition.y + height / 2, worldPosition.z);
			break;
		case MIDDLE_RIGHT:
			result.set(worldPosition.x + width, worldPosition.y + height / 2, worldPosition.z);
			break;
		case TOP_LEFT:
			result.set(worldPosition.x, worldPosition.y + height, worldPosition.z);
			break;
		case TOP_CENTER:
			result.set(worldPosition.x + width / 2, worldPosition.y + height, worldPosition.z);
			break;
		case TOP_RIGHT:
			result.set(worldPosition.x + width, worldPosition.y + height, worldPosition.z);
			break;
		}
	}
}
