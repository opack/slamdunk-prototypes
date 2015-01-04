package com.slamdunk.toolkit.gameparts.components;

public class TouchHandlerPart extends Component {
	public interface TouchHandler {
		/**
		 * Retourne true s'il est nécessaire de garder
		 * le focus sur ce contrôle et donc de lui envoyer les futurs
		 * touchDragged et touchUp.
		 * @param x
		 * @param y
		 * @param pointer
		 * @param button
		 * @return
		 */
		boolean touchDown(float x, float y, int pointer, int button);
		
		/**
		 * Retourne true s'il n'est plus nécessaire de garder
		 * le focus sur ce contrôle et donc de lui envoyer les futurs
		 * touchDragged et touchUp.
		 * @param x
		 * @param y
		 * @param pointer
		 * @return
		 */
		boolean touchDragged(float x, float y, int pointer);
		
		/**
		 * Retourne true s'il n'est plus nécessaire de garder
		 * le focus sur ce contrôle et donc de lui envoyer les futurs
		 * touchDragged et touchUp.
		 * @param x
		 * @param y
		 * @param pointer
		 * @param button
		 * @return
		 */
		boolean touchUp(float x, float y, int pointer, int button);
	}
	
	public TouchHandler handler;
	
	@Override
	public void reset() {
		handler = null;
	}
	
	@Override
	public void init() {
		if (handler == null) {
			throw new IllegalArgumentException("Missing handler parameter. The TouchHandlerPart cannot work properly.");
		}
	}
}
