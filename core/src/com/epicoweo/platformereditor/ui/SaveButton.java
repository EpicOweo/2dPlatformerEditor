package com.epicoweo.platformereditor.ui;

import java.io.IOException;

import com.epicoweo.platformereditor.screens.EditorScreen;

public class SaveButton extends Button {

	public SaveButton(float x, float y) {
		super(x, y, Button.splitTextures[0][0], Button.splitTextures[0][1]);
	}

	@Override
	public void execute() {
		System.out.println("Saving...");
		try {
			EditorScreen.saveLevel();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.pressed = false;
		System.out.println("Save successful.");
	}
	
}
