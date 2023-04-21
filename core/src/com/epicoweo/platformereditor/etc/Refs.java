package com.epicoweo.platformereditor.etc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.epicoweo.platformereditor.PlatformerEditor;
import com.epicoweo.platformereditor.ui.Button;

public class Refs {

	public static PlatformerEditor game;
	
	public static int APP_WIDTH = 1280;
	public static int APP_HEIGHT = 720;
	
	public static Array<Button> buttons = new Array<Button>();
	
	public static Texture scaleTexture(Texture t, int width, int height) {
		
		//TextureRegion region = new TextureRegion(t);
		if(!t.getTextureData().isPrepared()) {
			t.getTextureData().prepare();
		}
		
		Pixmap pixmapOld = t.getTextureData().consumePixmap();
		Pixmap pixmapNew = new Pixmap(width, height, pixmapOld.getFormat());
		pixmapNew.setFilter(Filter.NearestNeighbour);
		
		pixmapNew.drawPixmap(pixmapOld,
				0, 0, pixmapOld.getWidth(), pixmapOld.getHeight(),
				0, 0, pixmapNew.getWidth(), pixmapNew.getHeight()
		);
		
		t.draw(pixmapNew, 0, 0);
		
		if(!pixmapOld.isDisposed()) pixmapOld.dispose();
		if(!pixmapNew.isDisposed()) pixmapNew.dispose();
		
		return t;
	}
	
	public static TextureRegion scaleTextureRegion(TextureRegion t, int width, int height) {
		
		Texture texture = t.getTexture();
		if (!texture.getTextureData().isPrepared()) {
		    texture.getTextureData().prepare();
		
		}
		
		Pixmap pixmapOld = texture.getTextureData().consumePixmap();
		Pixmap pixmapNew = new Pixmap(width, height, pixmapOld.getFormat());
		pixmapNew.setFilter(Filter.NearestNeighbour);
		
		for (int x = 0; x < t.getRegionWidth(); x++) {
		    for (int y = 0; y < t.getRegionHeight(); y++) {
		        int colorInt = pixmapOld.getPixel(t.getRegionX() + x, t.getRegionY() + y);
		        // you could now draw that color at (x, y) of another pixmap of the size (regionWidth, regionHeight)
		        pixmapNew.drawPixel(x, y, colorInt);
		    }
		}
		
		texture.dispose();
		texture = new Texture(pixmapNew);
		
		//pixmapOld.dispose();
		//pixmapNew.dispose();
		
		TextureRegion tr = new TextureRegion(texture);
		return tr;
	}
	
	public static TextureRegion[] flattenSpriteSheet(TextureRegion[][] sprites) {
		TextureRegion[] flattened = new TextureRegion[sprites.length*sprites[0].length];
		int counter = 0;
		for(int i = 0; i < sprites.length; i++) {
			for(int j = 0; j < sprites[i].length; j++) {
				flattened[counter] = sprites[i][j];
				counter++;
			}
		}
		return flattened;
	}
	
}
