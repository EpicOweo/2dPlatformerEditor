package com.epicoweo.platformereditor.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.epicoweo.platformereditor.etc.Refs;
import com.epicoweo.platformereditor.tile.Tile;
import com.epicoweo.platformereditor.tile.Tile.TileTexture;
import com.epicoweo.platformereditor.tile.Tile.TileType;

public class TileChooser extends SideBar {
	
	Array<Tile> tilesList = new Array<Tile>();
	
	public Tile selectedTile;
	
	public TileChooser(int x, int y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		createTilesList();
		selectedTile = tilesList.get(0);
	}
	
	public TileChooser(float x, float y, float width, float height) {
		this((int)x, (int)y, width, height);
	}
	
	void createTilesList() {
		tilesList.add(new Tile(0, 0, 64, 64, TileTexture.STONE));
		tilesList.add(new Tile(0, 0, 64, 64, TileTexture.GRASS));
		tilesList.add(new Tile(0, 0, 64, 64, TileTexture.FENCE));
		tilesList.add(new Tile(0, 0, 64, 64, TileTexture.ON));
		tilesList.add(new Tile(0, 0, 64, 64, TileTexture.OFF));
		tilesList.add(new Tile(0, 0, 64, 64, TileTexture.GREENBLOCK));
		tilesList.add(new Tile(0, 0, 64, 64, TileTexture.REDBLOCK));
		tilesList.add(new Tile(0, 0, 64, 64, TileTexture.PLAYER));
		tilesList.add(new Tile(0, 0, 64, 64, TileTexture.GRAVITYSWAP));
		tilesList.add(new Tile(0, 0, 64, 64, TileTexture.GRAVITYSWAPDOWN));
		tilesList.add(new Tile(0, 0, 64, 64, TileTexture.PLATFORM, TileType.PLATFORM));
		tilesList.add(new Tile(0, 0, 64, 64, TileTexture.MECHASUITPICKUP));
		tilesList.add(new Tile(0, 0, 64, 64, TileTexture.SPIKEUP));
		tilesList.add(new Tile(0, 0, 64, 64, TileTexture.SPIKEDOWN));
		tilesList.add(new Tile(0, 0, 64, 64, TileTexture.SPIKELEFT));
		tilesList.add(new Tile(0, 0, 64, 64, TileTexture.SPIKERIGHT));
	}

	@Override
	public void render() {
		super.render();
		
		SpriteBatch b = Refs.game.batch;
		ShapeRenderer r = Refs.game.renderer;
		
		float xLeft = this.x + this.width / 2 - 96;
		float xRight = this.x + this.width / 2 + 32;
		
		Tile hoveredTile = null;
		
		int count = 0;
		boolean flag = true;
		
		Vector2 mousePosition = new Vector2(Gdx.input.getX(), Refs.APP_HEIGHT - Gdx.input.getY());
		
		for(Tile tile : tilesList) {
			
			int modifier = Math.floorDiv(count, 2);
			int yPos = Refs.APP_HEIGHT - (this.y + modifier*96 + 100);
			if(flag) {
				if(xLeft - 4 <= mousePosition.x && mousePosition.x <= xLeft - 4 + 72) {
					if(yPos <= mousePosition.y
						&& mousePosition.y <= yPos + 72) {
							hoveredTile = tile;
					}
				}
			} else {
				if(xRight - 4 <= mousePosition.x && mousePosition.x <= xRight - 4 + 72) {
					if(Refs.APP_HEIGHT - (this.y + modifier*96 + 100) <= mousePosition.y
						&& mousePosition.y <= Refs.APP_HEIGHT - (this.y + modifier*96 + 100) + 72) {
							hoveredTile = tile;
					}
				};
			}
			flag = !flag;
			count++;
		}
		
		count = 0;
		flag = true;
		
		r.begin(ShapeType.Filled);
		
		r.setColor(0.2f, 0.2f, 0.2f, 1);
		
		
		//put a gray rectangle behind each of the textures
		for(Tile tile : tilesList) {
			
			int modifier = Math.floorDiv(count, 2);
			
			if(tile == selectedTile) {
				r.setColor(46/255, 150/255, 255/255, 1);
			} else if(tile == hoveredTile) {
				r.setColor(255/255, 255/255, 255/255, 1);
			} else {
				r.setColor(0.2f, 0.2f, 0.2f, 1);
			}
			
			if(flag) {
				r.rect(xLeft - 4, Refs.APP_HEIGHT -(this.y + modifier*96 + 100), 72, 72);
			} else {
				r.rect(xRight - 4, Refs.APP_HEIGHT - (this.y + modifier*96 + 100), 72, 72);
			}
			flag = !flag;
			count++;
		}
		
		r.end();
		
		count = 0;
		flag = true;
		
		b.begin();
		//draw the textures themselves
		for(Tile tile : tilesList) {
			
			int modifier = Math.floorDiv(count, 2);
			
			if(flag) {
				b.draw(tile.textureRegion, xLeft, Refs.APP_HEIGHT - (this.y + modifier*96 + 96));
			} else {
				b.draw(tile.textureRegion, xRight, Refs.APP_HEIGHT - (this.y + modifier*96 + 96));
			}
			flag = !flag;
			count++;
		}
		b.end();
		
		if(hoveredTile != null) {
			if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				selectedTile = hoveredTile;
			}
		}
		
	}
}
