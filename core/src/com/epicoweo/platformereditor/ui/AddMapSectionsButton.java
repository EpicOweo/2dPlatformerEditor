package com.epicoweo.platformereditor.ui;

public class AddMapSectionsButton extends Button {
	
	public AddMapSectionsButton(int x, int y) {
		super(x, y, 4*Button.basicButtonSize, Button.basicButtonSize);
	}
	
	@Override
	public void execute() {
		ButtonFunctions.editMapSections();
	}

}