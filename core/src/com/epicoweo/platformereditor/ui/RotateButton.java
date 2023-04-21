package com.epicoweo.platformereditor.ui;

import com.epicoweo.platformereditor.screens.EditorScreen;

public class RotateButton extends Button {
	
	public RotateButton(int x, int y) {
		super(x, y, Button.splitTextures[0][2], Button.splitTextures[0][3]);
	}
	
	@Override
	public void execute() {
		EditorScreen.rotate();
		this.pressed = false;
	}

}
