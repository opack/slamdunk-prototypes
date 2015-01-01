package com.slamdunk.toolkit.gameparts.components.position;

import com.badlogic.gdx.math.Vector3;
import com.slamdunk.toolkit.gameparts.AnchorPoint;
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
	/**
	 * Point du GameObject à aligner
	 */
	public AnchorPoint gameObjectPointToAlign;
	
	/**
	 * L'ancre par rapport à laquelle doit être effectué l'alignement
	 */
	public GameObject anchor;
	
	/**
	 * Point de l'ancre sur laquelle s'aligner
	 */
	public AnchorPoint anchorReferencePoint;
	
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
		gameObjectPointToAlign = AnchorPoint.MIDDLE_CENTER;
		anchorReferencePoint = AnchorPoint.MIDDLE_CENTER;
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
			gameObjectPointToAlign.computeAlignSpotPosition(gameObject.transform.worldPosition, size.width, size.height, tmpAlignSpotPosition);
			anchorReferencePoint.computeAlignSpotPosition(anchorWorldPosition, anchorWidth, anchorHeight, tmpAnchorAlignSpotPosition);
			
			// Calcule le décalage à effectuer pour que le point d'alignement
			// du GameObject conteneur soit aligné avec celui de l'ancre
			tmpAlignSpotPosition.sub(tmpAnchorAlignSpotPosition);
			
			// Déplace les coordonnées relatives du GameObject conteneur
			// pour que le point d'alignement se trouve au bon endroit
			gameObject.transform.relativePosition.sub(tmpAlignSpotPosition);
			
			layout = false;
		}
	}
}
