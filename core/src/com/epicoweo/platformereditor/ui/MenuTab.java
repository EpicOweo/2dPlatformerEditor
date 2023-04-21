package com.epicoweo.platformereditor.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.epicoweo.platformereditor.etc.Refs;
import com.epicoweo.platformereditor.screens.EditorScreen;

public class MenuTab extends SideBar {
	
	public TextField mapWidthTextField;
	public TextField mapHeightTextField;
	
	public Button updateDimensions;
	
	public MenuTab(float x, float y, float width, float height) {
		this.x = (int)x;
		this.y = (int)y;
		this.width = width;
		this.height = height;
		
		createButtons();
		createTextFields();
	}
	
	void createButtons() {
		SaveButton saveButton = new SaveButton(this.x + 10, Refs.APP_HEIGHT - 10 - Button.basicButtonSize);
		RotateButton rotateButton = new RotateButton(this.x + 10, Refs.APP_HEIGHT - 2 *(10 + Button.basicButtonSize));
		
		Button updateDimensions = new Button(this.x + 180, Refs.APP_HEIGHT - 10 - Button.basicButtonSize);
		updateDimensions.method = () -> {
			ButtonFunctions.updateDimensions(mapWidthTextField, mapHeightTextField);
		};
		
		AddMapSectionsButton addMapSections = new AddMapSectionsButton(this.x + 10, Refs.APP_HEIGHT - 3 *(10 + Button.basicButtonSize));
		addMapSections.setLabelText("Add Map Sections");
		addMapSections.setToggleable(true);
		
		Button removePreviousSection = new Button(this.x + 10, Refs.APP_HEIGHT - 4 *(10 + Button.basicButtonSize),
				4*Button.basicButtonSize, Button.basicButtonSize);
		removePreviousSection.method = () -> {
			ButtonFunctions.removePreviousSection();
		};
		removePreviousSection.setLabelText("Remove Section");
		
		ImportButton importLevel = new ImportButton(this.x + 10, Refs.APP_HEIGHT - 5 *(10 + Button.basicButtonSize));
		addMapSections.setLabelText("Add Map Sections");
		addMapSections.setToggleable(true);
		
		
		Refs.buttons.add(saveButton);
		Refs.buttons.add(rotateButton);
		Refs.buttons.add(updateDimensions);
		Refs.buttons.add(addMapSections);
		Refs.buttons.add(removePreviousSection);
		Refs.buttons.add(importLevel);
	}
	
	void createTextFields() {
		TextFieldStyle tfstyle = new TextFieldStyle();
		tfstyle.font = new BitmapFont();
		tfstyle.fontColor = Color.BLACK;
		
		Label.LabelStyle lstyle = new Label.LabelStyle();
		lstyle.font = new BitmapFont();
		lstyle.fontColor = Color.BLACK;
		
		Label widthLabel = new Label("Map Width: ", lstyle);
		Label heightLabel = new Label("Map Height: ", lstyle);
		
		widthLabel.setPosition(this.x + 60, Refs.APP_HEIGHT - 10 - Button.basicButtonSize);
		heightLabel.setPosition(this.x + 60, Refs.APP_HEIGHT - 2 *(10 + Button.basicButtonSize));
		
		mapWidthTextField = new TextField(""+EditorScreen.mapWidth, tfstyle);
		mapHeightTextField = new TextField(""+EditorScreen.mapHeight, tfstyle);
		
		mapWidthTextField.setPosition(this.x + 140, Refs.APP_HEIGHT - 10 - Button.basicButtonSize);
		mapHeightTextField.setPosition(this.x + 140, Refs.APP_HEIGHT - 2 *(10 + Button.basicButtonSize));
		
		EditorScreen.stage.addActor(widthLabel);
		EditorScreen.stage.addActor(heightLabel);
		EditorScreen.stage.addActor(mapWidthTextField);
		EditorScreen.stage.addActor(mapHeightTextField);
	}
	
	@Override
	public void render() {
		super.render();
	}
}
