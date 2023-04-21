package com.epicoweo.platformereditor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.epicoweo.platformereditor.screens.EditorScreen;

public class PlatformerEditor extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public ShapeRenderer renderer;
	
	public AssetManager manager;
	
	EditorScreen editorScreen;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		renderer = new ShapeRenderer();
		manager = new AssetManager();
		
		this.editorScreen = new EditorScreen(this);
		this.setScreen(editorScreen);
	}

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		renderer.dispose();
		
		for(Object o : manager.getAll(Object.class, new Array<Object>())) {
			manager.unload(manager.getAssetFileName(o));
		}
		
		manager.dispose();
	}
}
