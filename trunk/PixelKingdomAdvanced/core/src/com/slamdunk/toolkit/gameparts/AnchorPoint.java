package com.slamdunk.toolkit.gameparts;

import com.badlogic.gdx.math.Vector3;

public enum AnchorPoint {
	BOTTOM_LEFT {
		@Override
		public void computeAlignSpotPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x, worldPosition.y, worldPosition.z);
		}
		@Override
		public void computeAlignedPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x, worldPosition.y, worldPosition.z);
		}
	},
	BOTTOM_CENTER {
		@Override
		public void computeAlignSpotPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x + width / 2, worldPosition.y, worldPosition.z);
		}
		@Override
		public void computeAlignedPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x - width / 2, worldPosition.y, worldPosition.z);
		}
	},
	BOTTOM_RIGHT {
		@Override
		public void computeAlignSpotPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x + width, worldPosition.y, worldPosition.z);
		}
		@Override
		public void computeAlignedPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x - width, worldPosition.y, worldPosition.z);
		}
	},
	MIDDLE_LEFT {
		@Override
		public void computeAlignSpotPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x, worldPosition.y + height / 2, worldPosition.z);
		}
		@Override
		public void computeAlignedPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x, worldPosition.y - height / 2, worldPosition.z);
		}
	},
	MIDDLE_CENTER {
		@Override
		public void computeAlignSpotPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x + width / 2, worldPosition.y + height / 2, worldPosition.z);
		}
		@Override
		public void computeAlignedPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x - width / 2, worldPosition.y - height / 2, worldPosition.z);
		}
	},
	MIDDLE_RIGHT {
		@Override
		public void computeAlignSpotPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x + width, worldPosition.y + height / 2, worldPosition.z);
		}
		@Override
		public void computeAlignedPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x - width, worldPosition.y - height / 2, worldPosition.z);
		}
	},
	TOP_LEFT {
		@Override
		public void computeAlignSpotPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x, worldPosition.y + height, worldPosition.z);
		}
		@Override
		public void computeAlignedPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x, worldPosition.y - height, worldPosition.z);
		}
	},
	TOP_CENTER {
		@Override
		public void computeAlignSpotPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x + width / 2, worldPosition.y + height, worldPosition.z);
		}
		@Override
		public void computeAlignedPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x - width / 2, worldPosition.y - height, worldPosition.z);
		}
	},
	TOP_RIGHT {
		@Override
		public void computeAlignSpotPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x + width, worldPosition.y + height, worldPosition.z);
		}
		@Override
		public void computeAlignedPosition(Vector3 worldPosition, float width, float height, Vector3 result) {
			result.set(worldPosition.x - width, worldPosition.y - height, worldPosition.z);
		}		
	};
	
	/**
	 * Calcule la position du point d'alignement par rapport à la position et la taille indiquées
	 * @param worldBottomLeftPosition
	 * @param size
	 * @param alignSpot
	 * @param result
	 */
	public abstract void computeAlignSpotPosition(Vector3 worldBottomLeftPosition, float width, float height, Vector3 result);
	
	/**
	 * Calcule la position du nouveau point bas-gauche après que l'alignement ait été effectué.
	 * Ce calcul revient à faire :
	 *  1. anchor.computeAlignSpotPosition(Vector3.Zero, width, height, tmpOffset);
		2. alignedPosition.set(worldBottomLeftPosition);
		3. alignedPosition.sub(tmpOffset);
	 * En revanche, cela va plus vite que ces 3 appels.
	 */
	public abstract void computeAlignedPosition(Vector3 worldBottomLeftPosition, float width, float height, Vector3 result);
}