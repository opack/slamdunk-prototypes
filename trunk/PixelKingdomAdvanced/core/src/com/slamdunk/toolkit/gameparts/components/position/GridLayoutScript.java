package com.slamdunk.toolkit.gameparts.components.position;

import java.util.List;

import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;

/**
 * Place les enfants du GameObject conteneur dans une grille.
 * Les scripts de Layout doivent être exécutés en premier (juste après
 * le TransformPart) et doivent donc être ajoutés en premier dans le GameObject.
 */
public class GridLayoutScript extends Component {
	public int nbColumns;
	public int nbRows;
	
	public float columnWidth;
	public float rowHeight;
	
	/**
	 * Direction dans laquelle on remplit la grille :
	 * true -> (fill upward) Vers le haut (donc le y du parent sera en bas de la grille)
	 * false -> (fill downward)  Vers le bas (donc le y du parent sera en haut de la grille)
	 */
	public boolean fillUpward;
	
	public boolean layout;
	
	/**
	 * Direction dans laquelle on remplit la grille :
	 * 1 -> Vers le haut (donc le y du parent sera en bas de la grille)
	 * 2 -> Vers le bas (donc le y du parent sera en haut de la grille)
	 */
	private int direction;
	
	@Override
	public void reset() {
		nbColumns = 1;
		nbRows = 1;
		
		columnWidth = 10;
		rowHeight = 10;
		
		fillUpward = true;
		direction = 1;
		
		layout = true;
	}
	
	@Override
	public void init() {
		if (nbColumns < 1
		|| nbRows < 1) {
			throw new IllegalArgumentException("There must be at least 1 column and 1 row in the grid");
		}
		
		if (columnWidth <= 0
		|| rowHeight <= 0) {
			throw new IllegalArgumentException("Column width and row height must be positive");
		}
	}
	
	@Override
	public void physics(float deltaTime) {
		if (layout) {
			if (fillUpward) {
				direction = 1;
			} else {
				direction = -1;
			}
			
			final List<GameObject> children = gameObject.getChildren();
			final int nbChildren = children.size();
			
			GameObject child;
			GridPositionPart position;
			int curChild = 0;
			doLayout: for (int row = 0; row < nbRows; row++) {
				for (int col = 0; col < nbColumns; col++) {
					if (curChild < nbChildren) {
						child = children.get(curChild);
						child.transform.relativePosition.x = col * columnWidth;
						child.transform.relativePosition.y = direction * row * rowHeight;
						
						position = child.getComponent(GridPositionPart.class);
						if (position != null) {
							position.column = col;
							position.row = row;
						}
						
						curChild++;
					} else {
						break doLayout;
					}
				}
			}
			
			layout = false;
		}
	}
}
