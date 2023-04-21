package com.epicoweo.platformereditor.screens;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.epicoweo.platformereditor.PlatformerEditor;
import com.epicoweo.platformereditor.etc.Assets;
import com.epicoweo.platformereditor.etc.Drawable;
import com.epicoweo.platformereditor.etc.Refs;
import com.epicoweo.platformereditor.tile.Tile;
import com.epicoweo.platformereditor.tile.Tile.TileTexture;
import com.epicoweo.platformereditor.tile.Tile.TileType;
import com.epicoweo.platformereditor.ui.Button;
import com.epicoweo.platformereditor.ui.ButtonFunctions;
import com.epicoweo.platformereditor.ui.MenuTab;
import com.epicoweo.platformereditor.ui.SideBar;
import com.epicoweo.platformereditor.ui.TileChooser;

public class EditorScreen implements Screen {

	public static OrthographicCamera camera;
	Vector3 ogCamera = new Vector3();
	final PlatformerEditor game;
	
	Array<SideBar> editorSideBars;
	TileChooser tc;
	public static int editorWidth = 800;
	public static int editorHeight = 720;
	public static float editorX = (Refs.APP_WIDTH - editorWidth) / 2f;
	public static float editorY = 0;
	
	public static int mapWidth = 25;
	public static int mapHeight = 25;
	
	public static float gridSquareSize = (editorWidth) / (mapWidth+1);
	
	public static Array<Array<Tile>> tileGrid;
	public static Array<Array<Tile>> entityGrid;
	public static Array<Array<Tile>> etcGrid;
	
	public static Array<Array<Tile>> currentGrid;
	
	public static Array<Rectangle> mapSections = new Array<Rectangle>();
	
	public static float currentRotation = 0;
	
	public static Stage stage = new Stage();
	
	public static TextureRegion[] spriteSheet;
	public static TextureRegion[] etcSpriteSheet;
	public static TextureRegion[] entitySpriteSheet;
	public static TextureRegion[] backgroundSpriteSheet;
	
	boolean dragging = false;
	boolean creatingTiles = true;
	Array<int[]> draggedTiles;
	public static Array<Array<Tile>> bg0Grid;
	public static Array<Array<Tile>> bg1Grid;
	public static Array<Array<Tile>> bg2Grid;
	public static Array<Array<Tile>> fg0Grid;
	public static Array<Array<Tile>> fg1Grid;
	public static Array<Array<Tile>> fg2Grid;
	
	public EditorScreen(final PlatformerEditor game) {
		this.game = game;
		Refs.game = game;
		
		loadAssets();
		
		loadSpriteSheet();
		Button.setupButtons();
		
		TileTexture.EMPTY.setTextures(); //set textures
		
		this.editorSideBars = new Array<SideBar>();
		
		tileGrid = new Array<Array<Tile>>();
		entityGrid = new Array<Array<Tile>>();
		etcGrid = new Array<Array<Tile>>();
		
		bg0Grid = new Array<Array<Tile>>();
		bg1Grid = new Array<Array<Tile>>();
		bg2Grid = new Array<Array<Tile>>();
		fg0Grid = new Array<Array<Tile>>();
		fg1Grid = new Array<Array<Tile>>();
		fg2Grid = new Array<Array<Tile>>();
		
		
		draggedTiles = new Array<int[]>();
		
		currentGrid = tileGrid;
		
		//make empty grid
		for(int i = 0; i < mapHeight; i++) {
			tileGrid.add(new Array<Tile>());
			entityGrid.add(new Array<Tile>());
			etcGrid.add(new Array<Tile>());
			bg0Grid.add(new Array<Tile>());
			bg1Grid.add(new Array<Tile>());
			bg2Grid.add(new Array<Tile>());
			fg0Grid.add(new Array<Tile>());
			fg1Grid.add(new Array<Tile>());
			fg2Grid.add(new Array<Tile>());
			for(int j = 0; j < mapWidth; j++) {
				tileGrid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				entityGrid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				etcGrid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				bg0Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				bg1Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				bg2Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				fg0Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				fg1Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				fg2Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
			}
		}
		
		setupCamera();
		setupSideBars();
		
		Gdx.input.setInputProcessor(stage);
	}
	
	private void loadSpriteSheet() {
		Texture sheet = game.manager.get(Assets.SPRITESHEET);
		Texture etcSheet = game.manager.get(Assets.ETCSPRITESHEET);
		Texture entitySheet = game.manager.get(Assets.ENTITYSPRITESHEET);
		Texture bgSheet = game.manager.get(Assets.BGSPRITESHEET);
		TextureRegion[][] sprites = new TextureRegion(sheet).split(16, 16);
		TextureRegion[][] etcSprites = new TextureRegion(etcSheet).split(16, 16);
		TextureRegion[][] entitySprites = new TextureRegion(entitySheet).split(16, 16);
		TextureRegion[][] bgSprites = new TextureRegion(bgSheet).split(16, 16);
		spriteSheet = new TextureRegion[] {
			new TextureRegion(),
			sprites[3][0], // stone
			sprites[1][6], // off
			sprites[1][7], // on
			sprites[1][8], // gravityswap up
			sprites[0][7], // platform
			sprites[5][1], // grass
			sprites[2][6], // redblock
			sprites[2][7], // greenblock
			sprites[2][8], // gravityswap down
			sprites[3][3], // spikeup
			sprites[5][3], // spikedown
			sprites[4][2], // spikeleft
			sprites[4][4], // spikeright
		};
		etcSpriteSheet = new TextureRegion[] {
			new TextureRegion(),
			etcSprites[0][0] // player
		};
		entitySpriteSheet = new TextureRegion[] {
			new TextureRegion(),
			entitySprites[0][0] // mechasuitpickup
		};
		backgroundSpriteSheet = new TextureRegion[] {
			new TextureRegion(),
			bgSprites[0][1] // fence
		};
		
	}

	private void loadAssets() {
		game.manager.load(Assets.SPRITESHEET, Texture.class);
		game.manager.load(Assets.ETCSPRITESHEET, Texture.class);
		game.manager.load(Assets.ENTITYSPRITESHEET, Texture.class);
		game.manager.load(Assets.BGSPRITESHEET, Texture.class);
		game.manager.load(Assets.ICONS, Texture.class);
		game.manager.finishLoading();
	}

	void setCurrentGrid() {
		TileTexture tt = tc.selectedTile.tt;
		
		switch(tt.type) {
		case "tile":
			currentGrid = tileGrid;
			break;
		case "etc":
			currentGrid = etcGrid;
			break;
		case "entity":
			currentGrid = entityGrid;
			break;
		case "bg0":
			currentGrid = bg0Grid;
			break;
		case "bg1":
			currentGrid = bg1Grid;
			break;
		case "bg2":
			currentGrid = bg2Grid;
			break;
		case "fg0":
			currentGrid = fg0Grid;
			break;
		case "fg1":
			currentGrid = fg1Grid;
			break;
		case "fg2":
			currentGrid = fg2Grid;
			break;
		default:
			break;
		}
		
	}
	
	void setupSideBars() {
		tc = new TileChooser(0, 0, editorX, Refs.APP_HEIGHT-editorY);
		editorSideBars.add(tc);
		editorSideBars.add(new MenuTab(editorX + editorWidth, editorY, Refs.APP_WIDTH-editorX, Refs.APP_HEIGHT-editorY));
	}
	
	void setupCamera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Refs.APP_WIDTH, Refs.APP_HEIGHT);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
		ogCamera = new Vector3(camera.position.x, camera.position.y, 0);
	}
	
	public static void rotate() {
		EditorScreen.currentRotation += 90;
		EditorScreen.currentRotation = Math.floorMod((int)EditorScreen.currentRotation, 360);
	}
	
	public static void saveLevel() throws IOException {
		String folder = createMapFolder();
		saveAsPng(folder);
		saveSections(folder);
	}
	
	public static String createMapFolder() throws IOException {
		String folderName = "../output/map";
		int num = 0;
		while(new File(folderName + num).exists()) {
			num++;
		}
		File newFile = new File(folderName + num);
		newFile.mkdir();
		return folderName + num;
	}
	
	public static void saveAsPng(String folder) { //save the map as a png
		Pixmap mainPix = new Pixmap(mapWidth, mapHeight, Format.RGBA8888); //tiles
		for(int i = 0; i < tileGrid.size; i++) {
			for(int j = 0; j < tileGrid.get(i).size; j++) {
				//red channel: tiles; green: entities; blue: etc
				mainPix.setColor(new Color(tileGrid.get(i).get(j).tt.textureId/255f,
						entityGrid.get(i).get(j).tt.textureId/255f,
						etcGrid.get(i).get(j).tt.textureId/255f, 1));
				mainPix.drawPixel(j, i); //x, y
			}
		}
		PixmapIO.writePNG(new FileHandle(folder + "/mainpix.png"), mainPix);	
		mainPix.dispose();
		
		Pixmap bgPix = new Pixmap(mapWidth, mapHeight, Format.RGBA8888); //background
		for(int i = 0; i < bg0Grid.size; i++) {
			for(int j = 0; j < bg0Grid.get(i).size; j++) {

				//mainPix.setColor(new Color(bg0Grid.get(i).get(j).tt.textureId/255f,
				//		bg1Grid.get(i).get(j).tt.textureId/255f,
				//		bg2Grid.get(i).get(j).tt.textureId/255f, 1));
				mainPix.setColor(new Color(bg0Grid.get(i).get(j).tt.textureId/255f, 0f, 0f, 1f));
				mainPix.drawPixel(j, i); //x, y
			}
		}
		PixmapIO.writePNG(new FileHandle(folder + "/bgpix.png"), bgPix);	
		bgPix.dispose();
		
		//Pixmap fgPix = new Pixmap(mapWidth, mapHeight, Format.RGBA8888); //foreground
		//for(int i = 0; i < fg0Grid.size; i++) {
		//	for(int j = 0; j < fg0Grid.get(i).size; j++) {
		//		fgPix.setColor(new Color(fg0Grid.get(i).get(j).tt.textureId/255f,
		//				fg1Grid.get(i).get(j).tt.textureId/255f,
		//				fg2Grid.get(i).get(j).tt.textureId/255f, 1));
		//		fgPix.drawPixel(j, i); //x, y
		//	}
		//}
		//PixmapIO.writePNG(new FileHandle(folder + "/fgpix.png"), fgPix);	
		//fgPix.dispose();
	}
	
	@SuppressWarnings("unchecked")
	public static void saveSections(String folder) throws IOException {
		String savePath = folder + "/mapsections.json";
		FileWriter fw = new FileWriter(savePath);
		
		JSONArray jsonMapSections = new JSONArray();
		for(Rectangle section : mapSections) {
			JSONObject jsonSection = new JSONObject();
			JSONArray fromCoords = new JSONArray();
			JSONArray toCoords = new JSONArray();
			System.out.println(section);
			fromCoords.add(section.x);
			fromCoords.add(section.y);
			toCoords.add(section.x + section.width);
			toCoords.add(section.y + section.height);
			
			jsonSection.put("fromCoords", fromCoords);
			jsonSection.put("toCoords", toCoords);
			jsonMapSections.add(jsonSection);
		}
		
		fw.write(jsonMapSections.toJSONString());
		fw.close();
	}
	
	@Override
	public void show() {
		
	}

	public void replaceGridSquare(int x, int y) {
		if(!currentGrid.get(y).get(x).isEmpty) {
			currentGrid.get(y).set(x, new Tile(0, 0, 16, 16, TileTexture.EMPTY));
		} else {
			currentGrid.get(y).set(x, tc.selectedTile.duplicate());
			currentGrid.get(y).get(x).rotation = currentRotation;
		}
	}
	
	public void renderMapEditor() {
		
		ShapeRenderer r = new ShapeRenderer();
		
		//make background
		r.begin(ShapeType.Filled);
		r.setColor(0.2f, 0.2f, 0.2f, 1);
		r.rect(editorX, editorY, editorWidth, editorHeight);
		r.end();
		//render grid
		r.setProjectionMatrix(camera.combined);
		r.begin(ShapeType.Line);
		r.setColor(0f, 0f, 0f, 1);
		
		for(int i = 0; i < mapWidth; i++) {
			for(int j = 0; j < mapHeight; j++) {
				r.rect(editorX + (i)*gridSquareSize, editorY + (j)*gridSquareSize, gridSquareSize, gridSquareSize);
			}
		}
		r.end();
		
		Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		
		
		//allow user to insert tiles
		if(!ButtonFunctions.mapSectionFlag) {
			boolean flag = false;
			for(SideBar sb : editorSideBars) {
				if(new Rectangle(sb.x, sb.y, sb.width, sb.height).contains(mousePosition.x, mousePosition.y)) {
					flag = true;
				}
			}
			
			camera.unproject(mousePosition);
			
			doCreateTiles: {
				if(flag) break doCreateTiles;
				for(int i = 0; i < mapWidth; i++) {
					for(int j = 0; j < mapHeight; j++) {		
						Vector3 rectVector = new Vector3(editorX +(i)*gridSquareSize, editorY + (j)*gridSquareSize, 0);
						
						int[] coords = {i, j};
						if(new Rectangle(rectVector.x, rectVector.y, gridSquareSize, gridSquareSize)
								.contains(mousePosition.x, mousePosition.y)) {
							if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
								dragging = true;
								draggedTiles.add(coords);
								creatingTiles = currentGrid.get(j).get(i).isEmpty;
								
								replaceGridSquare(i, j);
							} else if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
								if(dragging && !draggedTiles.contains(coords, true)) {
									if(creatingTiles && currentGrid.get(j).get(i).isEmpty) {
										replaceGridSquare(i, j);
									}
									else if(!creatingTiles && !currentGrid.get(j).get(i).isEmpty) {
										replaceGridSquare(i, j);
									}
								}
							}
							else {
								dragging = false;
								draggedTiles.clear();
							}
						}
					}
				}
			}
			
		}
		
		
		
		
		//render squares 
		SpriteBatch b = new SpriteBatch();
		b.setProjectionMatrix(camera.combined);
		b.begin();
		for(int i = 0; i < mapHeight; i++) {
			for(int j = 0; j < mapWidth; j++) {
				drawTilesAt(b, i, j);
			}
		}
		b.end();
		
		ShapeRenderer ren = new ShapeRenderer(); // draw mapSections
		r.begin(ShapeType.Line);
		r.setProjectionMatrix(camera.combined);
		for(Rectangle rect : mapSections) {
			r.setColor(Color.GREEN);
			r.rect(EditorScreen.editorX+rect.x*EditorScreen.gridSquareSize, EditorScreen.editorY+rect.y*EditorScreen.gridSquareSize,
					rect.width*EditorScreen.gridSquareSize, rect.height*EditorScreen.gridSquareSize);
		}
		r.end();
		r.setColor(Color.BLACK);
		r.dispose();
		
		//render side bars
		for(SideBar sb : this.editorSideBars) {
			sb.render();
		}
	}
	
	
	//old function
	public static void drawScaledRegion(SpriteBatch b, TextureRegion t, int i, int j, float scale) {
		b.draw(t, editorX + (j)*gridSquareSize, editorY + (i)*gridSquareSize,
				t.getRegionWidth()/2, t.getRegionHeight()/2,
				2*t.getRegionWidth(), 2*t.getRegionHeight(),
				1f, 1f, tileGrid.get(i).get(j).rotation);
	}
	
	public static void drawTilesAt(SpriteBatch b, int i, int j) {
		Array<Array<Array<Tile>>> grids = new Array<Array<Array<Tile>>>();
		grids.add(tileGrid, entityGrid, etcGrid);
		grids.add(bg0Grid, bg1Grid, bg2Grid);
		grids.add(fg0Grid, fg1Grid, fg2Grid);
		
		int k = 0;
		for(Array<Array<Tile>> grid : grids) {
			if(grid.get(i).get(j).isEmpty) continue;
			
			TextureRegion drawTexture = grid.get(i).get(j).textureRegion;
			float scale = (int)grid.get(i).get(j).height / drawTexture.getRegionHeight();
			if(k == 0) {
				if(grid.get(i).get(j).type == TileType.PLATFORM) {
					b.draw(drawTexture, editorX + (j)*gridSquareSize, editorY + (int)((i+0.75f)*gridSquareSize),
						drawTexture.getRegionWidth()/2, drawTexture.getRegionHeight()/2,
						drawTexture.getRegionWidth(), drawTexture.getRegionHeight(),
						1f, 1f, tileGrid.get(i).get(j).rotation);
				} else {
					drawScaledRegion(b, drawTexture, i, j, scale);
				}
			} else {
				drawScaledRegion(b, drawTexture, i, j, scale);
			}
			k++; 
		}
	}
	
	public static void updateMapSize() {
		
		int oldWidth = tileGrid.get(0).size;
		int oldHeight = tileGrid.size;
		//update grid
		boolean flag = false;
		int oldOldHeight = oldHeight;
		while(oldHeight < mapHeight) {
			tileGrid.add(new Array<Tile>());
			entityGrid.add(new Array<Tile>());
			etcGrid.add(new Array<Tile>());
			bg0Grid.add(new Array<Tile>());
			bg1Grid.add(new Array<Tile>());
			bg2Grid.add(new Array<Tile>());
			fg0Grid.add(new Array<Tile>());
			fg1Grid.add(new Array<Tile>());
			fg2Grid.add(new Array<Tile>());
			
			oldHeight++;
			flag=true;
		}
		if(flag) {
			for(int i = oldOldHeight; i < mapHeight; i++) {
				for(int j = 0; j < mapWidth; j++) {
					tileGrid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
					entityGrid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
					etcGrid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
					bg0Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
					bg1Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
					bg2Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
					fg0Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
					fg1Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
					fg2Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				}
			}
		}
		
		while(oldHeight > mapHeight) {
			tileGrid.pop();
			entityGrid.pop();
			etcGrid.pop();
			bg0Grid.pop();
			bg1Grid.pop();
			bg2Grid.pop();
			fg0Grid.pop();
			fg1Grid.pop();
			fg2Grid.pop();
			oldHeight--;
		}
		
		while(oldWidth < mapWidth) {
			for(int i = 0; i < mapHeight; i++) {
				tileGrid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				entityGrid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				etcGrid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				bg0Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				bg1Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				bg2Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				fg0Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				fg1Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
				fg2Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
			}
			
			oldWidth++;
		}
		while(oldWidth > mapWidth) {
			for(int i = 0; i < mapHeight; i++) {
				tileGrid.get(i).pop();
				entityGrid.get(i).pop();
				etcGrid.get(i).pop();
				bg0Grid.get(i).pop();
				bg1Grid.get(i).pop();
				bg2Grid.get(i).pop();
				fg0Grid.get(i).pop();
				fg1Grid.get(i).pop();
				fg2Grid.get(i).pop();
			}
			oldWidth--;
		}
		
	}
	
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);
		camera.update();
		renderMapEditor();
		setCurrentGrid();
		
		getInput();
		
		for(int i = 0; i < Refs.buttons.size; i++) {
			Refs.buttons.get(i).render();
		}
		
		stage.draw();
		stage.act();
	}
	
	void getInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-5*camera.zoom, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(5*camera.zoom, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -5*camera.zoom, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 5*camera.zoom, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            camera.zoom = 1;
        }
	}

	@Override
	public void resize(int width, int height) {
		
		if(width > 0f && height > 0f) {
			
			float scale = (float)editorWidth/(float)Refs.APP_WIDTH;
			editorWidth = (int)(width*scale);
			editorHeight = height;
			
			Refs.APP_WIDTH = width;
			Refs.APP_HEIGHT = height;
			
			editorX = (Refs.APP_WIDTH - editorWidth) / 2f;
			editorY = 0;
			gridSquareSize = (int)((float)editorWidth) / ((float)mapWidth + 1);
		
			for(SideBar sb : this.editorSideBars) {
				//sb.resize(width, height);
			}
		}
		
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
