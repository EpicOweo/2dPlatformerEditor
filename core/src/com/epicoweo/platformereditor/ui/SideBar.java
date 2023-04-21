package com.epicoweo.platformereditor.ui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.epicoweo.platformereditor.etc.Refs;

public abstract class SideBar {
	
	private ShapeRenderer renderer;
	
	public int x;
	public int y;
	public float width;
	public float height;
	
	public SideBar() {
		this.renderer = Refs.game.renderer;
	}
	
	public void render() {
		renderer.begin(ShapeType.Filled);
		renderer.setColor(0.5f, 0.5f, 0.5f, 1);
		renderer.rect(x, y, width, height);
		renderer.end();
	}
	
	public void resize(float width, float height) {
		this.width = width;
		this.height = height;
	}
}
