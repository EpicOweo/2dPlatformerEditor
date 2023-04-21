package com.epicoweo.platformereditor.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.epicoweo.platformereditor.etc.Assets;
import com.epicoweo.platformereditor.etc.Drawable;
import com.epicoweo.platformereditor.etc.Refs;
import com.epicoweo.platformereditor.screens.EditorScreen;

public class Button extends Drawable {
	
	public boolean pressed = false;
	TextureRegion texture;
	TextureRegion selectedTexture;
	String buttonText = "";
	public boolean toggleable = false;
	public boolean toggled = false;
	
	public static int basicButtonSize = 32;
	
	public static TextureRegion icons;
	public static TextureRegion[][] splitTextures;
	
	public Runnable method;
	
	public Button(float x, float y, float width, float height, TextureRegion texture, TextureRegion selectedTexture) {
		super(x, y, width, height);
		
		super.textureRegion = icons;
		
		this.texture = texture; 
		this.selectedTexture = selectedTexture;
	}
	
	public Button(float x, float y, float width, float height, TextureRegion texture) {
		this(x, y, width, height, texture, texture);
	}
	
	public Button(float x, float y, TextureRegion texture, TextureRegion selectedTexture) {
		this(x, y, Button.basicButtonSize, Button.basicButtonSize, texture, selectedTexture);
	}
	
	public Button(float x, float y, float width, float height) {
		this(x, y, width, height, new TextureRegion(new Texture(getDefaultPixmap(width, height))));
	}
	
	public Button(float x, float y) {
		this(x, y, Button.basicButtonSize, Button.basicButtonSize, new TextureRegion(new Texture(getDefaultPixmap())));
	}
	
	public static void setupButtons() {
		icons = new TextureRegion((Texture)Refs.game.manager.get(Assets.ICONS));
		splitTextures = icons.split(32, 32);
	}
	
	public static Pixmap getDefaultPixmap(float width, float height) {
		Pixmap defaultPixmap = new Pixmap((int)width, (int)height, Format.RGB888);
		defaultPixmap.setColor(Color.DARK_GRAY);
		defaultPixmap.fillRectangle(0, 0, (int)width, (int)height);
		return defaultPixmap;
	}
	
	public static Pixmap getDefaultPixmap() {
		return getDefaultPixmap(Button.basicButtonSize, Button.basicButtonSize);
	}
	
	public boolean check() {
		Vector2 mousePosition = new Vector2(Gdx.input.getX(), Refs.APP_HEIGHT - Gdx.input.getY());
		if(x <= mousePosition.x && mousePosition.x <= x + width) {
			if(y <= mousePosition.y && mousePosition.y <= y + height) {
				return true;
			}
		}
		return false;
	}
	
	public void execute() {
		method.run();
	}
	
	public void setLabelText(String text) {
		this.buttonText = text;
	}
	
	public void setToggleable(boolean toggleable) {
		this.toggleable = toggleable;
	}
	
	public void render() {
		SpriteBatch b = Refs.game.batch;
		ShapeRenderer r = Refs.game.renderer;
		BitmapFont f = Refs.game.font;
		
		r.begin(ShapeType.Filled);
		
		r.setColor(0.2f, 0.2f, 0.2f, 1);
		
		if(pressed) {
			r.setColor(46/255, 150/255, 255/255, 1);
		} else if(check()) {
			r.setColor(255/255, 255/255, 255/255, 1);
		} else {
			r.setColor(0.2f, 0.2f, 0.2f, 1);
		}
		
		r.rect(x, y, width, height);
		
		r.end();
		
		
		b.begin();
		
		//draw the textures themselves
		if((check() && !toggled) || (toggled && !check())) {
			b.draw(selectedTexture, x, y);
			f.setColor(Color.WHITE);
			f.draw(b, buttonText, x+4, y+Button.basicButtonSize-8);
		} else {
			b.draw(texture, x, y);
			f.setColor(Color.BLACK);
			f.draw(b, buttonText, x+4, y+Button.basicButtonSize-8);
		}
		b.end();
		
		f.setColor(Color.BLACK);
		if(check() && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			for(Button button : Refs.buttons) {
				button.pressed = false;
			}
			this.pressed = true;
			if(this.toggleable) {
				this.toggled = !this.toggled;
				if(this instanceof AddMapSectionsButton && !toggled) ButtonFunctions.resetSectionEditor();
			} else {
				execute();
			}
			
		}
		
		if(this.toggled) {
			execute();
		}
	}

}
