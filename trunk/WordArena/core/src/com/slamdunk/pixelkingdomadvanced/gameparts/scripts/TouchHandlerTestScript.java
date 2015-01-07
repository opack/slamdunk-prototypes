package com.slamdunk.pixelkingdomadvanced.gameparts.scripts;

import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.components.TouchHandlerPart.TouchHandler;

public class TouchHandlerTestScript extends Component implements TouchHandler {

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		System.out.println("TouchHandlerTestScript.touchDown()");
		return true;
	}

	@Override
	public boolean touchDragged(float x, float y, int pointer) {
		System.out.println("TouchHandlerTestScript.touchDragged()");
		return false;
	}
	
	@Override
	public boolean touchUp(float x, float y, int pointer, int button) {
		System.out.println("TouchHandlerTestScript.touchUp()");
		return false;
	}
}
