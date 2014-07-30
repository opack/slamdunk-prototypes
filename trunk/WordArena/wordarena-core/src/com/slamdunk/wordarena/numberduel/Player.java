package com.slamdunk.wordarena.numberduel;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Player {
	public String name;
	public int hp;
	public Label label;
	
	public Player(String name) {
		this.name = name;
	}

	public void updateLabel() {
		label.setText(name + "\n" + hp);
	}
}
