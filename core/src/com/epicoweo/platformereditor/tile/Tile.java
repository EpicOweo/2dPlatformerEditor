package com.epicoweo.platformereditor.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.epicoweo.platformereditor.etc.Drawable;
import com.epicoweo.platformereditor.etc.Refs;
import com.epicoweo.platformereditor.screens.EditorScreen;

public class Tile extends Drawable {

	public String texturePath;
	public boolean isEmpty = false;
	public float rotation = 0;
	public TileTexture tt;
	public TileType type;
	public float width = EditorScreen.gridSquareSize;
	public float height = EditorScreen.gridSquareSize;
	
	public enum TileTexture {
		//maplayout
		EMPTY(0, "tile"), STONE(1, "tile"),
		OFF(2, "tile"), ON(3, "tile"),
		GRAVITYSWAP(4, "tile"), PLATFORM(5, "tile"),
		GRASS(6, "tile"), REDBLOCK(7, "tile"),
		GREENBLOCK(8, "tile"), GRAVITYSWAPDOWN(9, "tile"),
		SPIKEUP(10, "tile"), SPIKEDOWN(11, "tile"),
		SPIKELEFT(12, "tile"), SPIKERIGHT(13, "tile"), 
		
		//mapetc
		PLAYER(1, "etc"),
		
		//mapentities
		MECHASUITPICKUP(1, "entity"),
		
		//bg0
		FENCE(1, "bg0");
		
		public TextureRegion texture;
		public int textureId;
		public String type;
		
		TileTexture(int textureId, String type) {
			this.textureId = textureId;
			this.type = type;
		}
		
		public void setTextures() {
			for(TileTexture tt : TileTexture.values()) {
				switch(tt.type) {
				case("tile"):
					tt.texture = EditorScreen.spriteSheet[tt.textureId];
					break;
				case("bg0"):
				case("bg1"):
				case("bg2"):
					tt.texture = EditorScreen.backgroundSpriteSheet[tt.textureId];
					break;
				case("etc"):
					tt.texture = EditorScreen.etcSpriteSheet[tt.textureId];
					break;
				case("entity"):
					tt.texture = EditorScreen.entitySpriteSheet[tt.textureId];
					break;
				}
			}
		}
	}
	
	public enum TileType {
		
		FULL("full"), SLOPE_45("slope45"), PLATFORM("platform");
		
		String s;
		TileType(String s) {
			this.s = s;
		}
		
		@Override
		public String toString() {
			return s;
		}
	}
	
	public Tile(float x, float y, float width, float height, TileTexture texture, TileType type) {
		super(x, y, width, height);
		
		
		this.type = type;
		this.tt = texture;
		
		if(texture != TileTexture.EMPTY) {
			if(type != TileType.PLATFORM) {
				this.textureRegion = Refs.scaleTextureRegion(texture.texture, 64, 16);
			} else {
				this.height = EditorScreen.gridSquareSize / 4;
				this.textureRegion = Refs.scaleTextureRegion(texture.texture, 64, 16);
			}
		} else {
			this.textureRegion = texture.texture;
			isEmpty = true;
		}
		
	}
	
	public Tile(float x, float y, float width, float height, TileTexture texture) {
		this(x, y, width, height, texture, TileType.FULL);
		
	}
	public Tile(float x, float y, float width, float height) {
		this(x, y, width, height, TileTexture.STONE, TileType.FULL);
		
	}
	
	public Tile duplicate() {
		Tile newTile = new Tile(x, y, width, height, tt, type);
		return newTile;
	}
	
	
}
