package com.epicoweo.platformereditor.etc;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Drawable {
	
	protected int x;
	protected int y;
	protected float width;
	protected float height;
	public TextureRegion textureRegion;
	
	public Drawable(float x, float y, float width, float height) {
		this.x = (int)x;
		this.y = (int)y;
		this.width = width;
		this.height = height;
	}
}
