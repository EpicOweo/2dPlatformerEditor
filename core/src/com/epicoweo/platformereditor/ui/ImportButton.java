package com.epicoweo.platformereditor.ui;

public class ImportButton extends Button {

	public ImportButton(float x, float y) {
		super(x, y, Button.basicButtonSize*4, Button.basicButtonSize);
		setLabelText("Import from /import");
	}
	
	@Override
	public void execute() {
		ButtonFunctions.importLevel();
	}

}
