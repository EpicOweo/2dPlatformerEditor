package com.epicoweo.platformereditor.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;
import com.epicoweo.platformereditor.screens.EditorScreen;
import com.epicoweo.platformereditor.tile.Tile;
import com.epicoweo.platformereditor.tile.Tile.TileTexture;
import com.epicoweo.platformereditor.tile.Tile.TileType;

public class ButtonFunctions {
	
	public static void updateDimensions(TextField mapWidthTextField, TextField mapHeightTextField) {
		try {
			EditorScreen.mapWidth = Integer.parseInt(mapWidthTextField.getText());
			EditorScreen.mapHeight = Integer.parseInt(mapHeightTextField.getText());
			
			EditorScreen.updateMapSize();
			
			if (EditorScreen.stage.getKeyboardFocus() == mapWidthTextField) {
				EditorScreen.stage.unfocus(mapWidthTextField);
			}
			if (EditorScreen.stage.getKeyboardFocus() == mapHeightTextField) {
				EditorScreen.stage.unfocus(mapHeightTextField);
			}
			
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	//x, y squares for the first pos
	static float xs1 = -1;
	static float ys1 = -1;
	public static boolean mapSectionFlag = false;
	
	public static void editMapSections() {
		ShapeRenderer r = new ShapeRenderer();
		OrthographicCamera camera = EditorScreen.camera;
		
		Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Vector3 projMousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(mousePosition);
		
		//indices of tile grid
		float xSquares = Math.round(2f*(mousePosition.x-EditorScreen.editorX) / EditorScreen.gridSquareSize)/2f;
		float ySquares = Math.round(2f*mousePosition.y / EditorScreen.gridSquareSize)/2f;
		Vector3 squaresVector = new Vector3(xSquares*EditorScreen.gridSquareSize, ySquares*EditorScreen.gridSquareSize, 0);
		
		if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && mapSectionFlag
				&& new Rectangle(EditorScreen.editorX, EditorScreen.editorY, EditorScreen.editorWidth, EditorScreen.editorHeight)
				.contains(projMousePosition.x, projMousePosition.y)) {
			System.out.println("click");
			if(xs1 == -1) {
				xs1 = xSquares;
				ys1 = ySquares;
			} else {
				// xs1,ys1 is...
				if(xSquares > xs1 && ySquares > ys1) { // bottom left
					EditorScreen.mapSections.add(new Rectangle(xs1, ys1, xSquares-xs1, ySquares-ys1));
				} else if(xSquares < xs1 && ySquares > ys1) {//bottom right
					EditorScreen.mapSections.add(new Rectangle(xSquares, ys1, xs1-xSquares, ySquares-ys1));
				} else if(xSquares > xs1 && ySquares < ys1) {//top left
					EditorScreen.mapSections.add(new Rectangle(xs1, ySquares, xSquares-xs1, ys1-ySquares));
				} else if(xSquares < xs1 && ySquares < ys1) {//top right
					EditorScreen.mapSections.add(new Rectangle(xSquares, ySquares, xs1-xSquares, ys1-ySquares));
				}
				
				xs1 = -1;
				ys1 = -1;
			}
		} else if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && mapSectionFlag) {
			xs1 = -1;
			ys1 = -1;
		}
		
		
		r.setColor(Color.RED);
		r.setAutoShapeType(true);
		r.setProjectionMatrix(camera.combined);
		r.begin();
		r.set(ShapeType.Filled);
		
		r.circle(EditorScreen.editorX+squaresVector.x, squaresVector.y, 3);
		if(xs1 != -1) {
			r.circle(EditorScreen.editorX+xs1*EditorScreen.gridSquareSize, ys1*EditorScreen.gridSquareSize, 3);
			r.set(ShapeType.Line);
			r.rect(EditorScreen.editorX+xs1*EditorScreen.gridSquareSize, EditorScreen.editorY+ys1*EditorScreen.gridSquareSize,
					(xSquares-xs1)*EditorScreen.gridSquareSize, (ySquares-ys1)*EditorScreen.gridSquareSize);
		}
		
		r.end();
		
		r.dispose();
		
		mapSectionFlag = true;
	}

	public static void resetSectionEditor() {
		mapSectionFlag = false;
		xs1 = -1;
		ys1 = -1;
	}

	public static void removePreviousSection() {
		EditorScreen.mapSections.pop();
		xs1 = -1;
		ys1 = -1;
		
	}

	public static void importLevel() {           
        File file = new File("../import");
        if(!file.exists()) return;
        String filePath = file.getAbsolutePath();
        if(file.isDirectory()) {
        	
        	Pixmap mainPix = new Pixmap(new FileHandle(filePath + "/mainpix.png"));
        	Pixmap bgPix = new Pixmap(new FileHandle(filePath + "/bgpix.png"));
        	Pixmap fgPix = new Pixmap(new FileHandle(filePath + "/fgpix.png"));
        	
        	EditorScreen.mapWidth = mainPix.getWidth();
        	EditorScreen.mapHeight = mainPix.getHeight();
        	int width = EditorScreen.mapWidth;
        	int height = EditorScreen.mapHeight;
        	
        	//make empty grid
        	EditorScreen.tileGrid.clear();
			EditorScreen.entityGrid.clear();
			EditorScreen.etcGrid.clear();
			EditorScreen.bg0Grid.clear();
			EditorScreen.bg1Grid.clear();
			EditorScreen.bg2Grid.clear();
			EditorScreen.fg0Grid.clear();
			EditorScreen.fg1Grid.clear();
			EditorScreen.fg2Grid.clear();
    		for(int i = 0; i < EditorScreen.mapHeight; i++) {
    			EditorScreen.tileGrid.add(new Array<Tile>());
    			EditorScreen.entityGrid.add(new Array<Tile>());
    			EditorScreen.etcGrid.add(new Array<Tile>());
    			EditorScreen.bg0Grid.add(new Array<Tile>());
    			EditorScreen.bg1Grid.add(new Array<Tile>());
    			EditorScreen.bg2Grid.add(new Array<Tile>());
    			EditorScreen.fg0Grid.add(new Array<Tile>());
    			EditorScreen.fg1Grid.add(new Array<Tile>());
    			EditorScreen.fg2Grid.add(new Array<Tile>());
    			for(int j = 0; j < EditorScreen.mapWidth; j++) {
    				EditorScreen.tileGrid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
    				EditorScreen.entityGrid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
    				EditorScreen.etcGrid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
    				EditorScreen.bg0Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
    				EditorScreen.bg1Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
    				EditorScreen.bg2Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
    				EditorScreen.fg0Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
    				EditorScreen.fg1Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
    				EditorScreen.fg2Grid.get(i).add(new Tile(0, 0, EditorScreen.gridSquareSize, EditorScreen.gridSquareSize, TileTexture.EMPTY));
    			}
    		}
        	
        	
        	//import mainpix.png
    		for(int i = 0; i < height; i++) {
    			for(int j = 0; j < width; j++) {
    				//get coords of the block
    				int x = j, y = i;
    				
    				//get tiles
    				int tileId = (int)(new Color(mainPix.getPixel(x, y)).r*255);
    				
    				
    				TileTexture texture = TileTexture.EMPTY;
    				for(TileTexture t : TileTexture.values()) {
    					if(t.textureId == tileId && t.type.equals("tile")) texture = t;
    				}
    				
    				TileType tt = texture == TileTexture.PLATFORM ? TileType.PLATFORM : TileType.FULL;
    				
    				
    				//add texture ID to the array
    				EditorScreen.tileGrid.get(y).insert(x, new Tile(x, y, 16, 16, texture));
    				
    				
    				//get entities
    				int entityId = (int)(new Color(mainPix.getPixel(x, y)).g*255);
    				TileTexture etexture = TileTexture.EMPTY;
    				for(TileTexture t : TileTexture.values()) {
    					if(t.textureId == entityId && t.type.equals("entity")) etexture = t;
    				}
    				
    				
    				EditorScreen.entityGrid.get(i).add(new Tile(x, y, 16, 16, etexture));
    				
    				
    				// get etc data
    				int etcId = (int)(new Color(mainPix.getPixel(x, y)).b*255);
    				TileTexture etctexture = TileTexture.EMPTY;
    				for(TileTexture t : TileTexture.values()) {
    					if(t.textureId == etcId && t.type.equals("etc")) etctexture = t;
    				}
    				
    				EditorScreen.etcGrid.get(y).insert(x, new Tile(x, y, 16, 16, etctexture));
    				
    				
    				//bg0
    				int bg0Id = (int)(new Color(bgPix.getPixel(x, y)).r*255);
    				
    				
    				TileTexture bgtexture = TileTexture.EMPTY;
    				for(TileTexture t : TileTexture.values()) {
    					if(t.textureId == bg0Id && t.type.equals("bg0")) bgtexture = t;
    				}
    				
    				TileType bgtt = TileType.FULL;
    				
    				
    				//add texture ID to the array
    				EditorScreen.bg0Grid.get(y).insert(x, new Tile(x, y, 16, 16, bgtexture));
    				
    			}
    		}
    		mainPix.dispose();
    		bgPix.dispose();
    		fgPix.dispose();
        	
    		String jsonFileContents = "";
    		
        	//import mapsections.json
    		Scanner sc;
			try {
				sc = new Scanner(new File(filePath + "/mapsections.json"));
				while(sc.hasNextLine()) {
        			jsonFileContents += sc.nextLine();
        		}
        		sc.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		
    		
    		JSONParser parser = new JSONParser();
    		try {
    			JSONArray arr = (JSONArray) parser.parse(jsonFileContents);
    			for(int i = 0; i < arr.size(); i++) {
    				JSONObject obj = (JSONObject) arr.get(i);
    				System.out.println(obj.toJSONString());
    				JSONArray fr = (JSONArray) obj.get("fromCoords");
    				JSONArray t = (JSONArray) obj.get("toCoords");
    				double fX = (double) fr.get(0);
    				double fY = (double) fr.get(1);
    				double tX = (double) t.get(0);
    				double tY = (double) t.get(1);
    				float fromX = (float) fX;
    				float fromY = (float) fY;
    				float toX = (float) tX;
    				float toY = (float) tY;
    				
    				EditorScreen.mapSections.add(new Rectangle(fromX, fromY,
    						(toX-fromX), (toY-fromY)));
    			}
    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		
		
    		}
        }
	}
}
