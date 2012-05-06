package com.adriangl.amide.gameelements;

import java.io.IOException;
import java.util.Calendar;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import testlwjgl.ObjLoader;
import testlwjgl.ObjModel;
import testlwjgl.Texture;
import testlwjgl.TextureLoader;

import com.adriangl.amide.network.Client;
import com.adriangl.amide.network.Server;

public class GameWindow {
	
	public static final String GAME_TITLE = "Asteroid Attack";
	public static int SCREEN_WIDTH = 800;
	public static int SCREEN_HEIGHT = 600;
	
	// Last timestamp when the loop was executed
	private long lastLoop = 0;
	
	// Checks if the game must be closed or not
	public boolean finished = false;
	
	// Game elements
	private GameElementList elementList = new GameElementList();
	
	// Textures and models
	private TextureLoader loader = new TextureLoader();
	Texture asteroidTexture;
	ObjModel asteroidModel;
	Texture spaceShipTexture;
	ObjModel spaceShipModel;
	
	// Checks if server
	private boolean isServer;
	
	public GameWindow(boolean isServer){
		this.isServer = isServer;
		try {				
			// Configure and create the LWJGL display			
			initDisplay();
			initGL();
			initTextures();
			initModels();
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Sys.alert("Error", "Failed: "+e.getMessage());
		}
		
	}

	private void initModels() {
		try {
			asteroidModel = ObjLoader.loadObj("asteroid.obj");
			spaceShipModel = ObjLoader.loadObj("chocobo.obj");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initTextures() {
		
		try {
			asteroidTexture = loader.getTexture("asteroid.jpg");
			spaceShipTexture = loader.getTexture("chocobo.jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private void initGL() {
		// Enable some system capabilities: 2d texturing, depth testing
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		// Perspective settings
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();		
		GLU.gluPerspective(45.0f, ((float) SCREEN_WIDTH) / ((float) SCREEN_HEIGHT), 0.1f, 100.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	}

	/**
	 * Initialises the display
	 * @throws LWJGLException 
	 */

	private void initDisplay() throws LWJGLException{
		// Default display mode: 800x600
		DisplayMode mode = new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		Display.setTitle(GAME_TITLE);
		Display.setDisplayMode(mode);
		Display.setFullscreen(false);
			
		Display.create();
			
	}
	
	/** 
	 * Runs the game loop
	 */
	
	public void runGame(){
		addElements();
		
		lastLoop = System.currentTimeMillis();
		
		if(isServer){
			(new Thread(new Server(elementList))).start();
		}
		else{
			(new Thread(new Client(elementList))).start();
		}
		
		while (!finished){
			// Clear screen
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
			
			// Get difference and current time in millis
			int delta = (int) (System.currentTimeMillis() - lastLoop);
			lastLoop = Calendar.getInstance().getTimeInMillis();
			
			// checks several window states to see what we do
			if (Display.isCloseRequested()){
				finished = true;
			}
			else{
				update(delta);
				render();
			}
			// Screen update when every operation is finished.
			Display.update();
		}
		
		// when the game is finished, we clean up all the resources used.
		cleanupGame();
		
		// close the game if the current instance is a server
		if (isServer){
			System.exit(0);
		}
	}
	

	private void addElements() {
		// Add asteroids
		for (int i = 0; i<10; i++){
			Asteroid a = new Asteroid(asteroidTexture, asteroidModel, 
					(float) (-20 + (Math.random() * 40)), (float) (-20 + (Math.random() * 40)), 3f);
			elementList.add(a);
		}
		// Add spaceship
		if (isServer){
			Spaceship sp = new Spaceship(spaceShipTexture, spaceShipModel);
			elementList.add(sp);
		}
	}

	/**
	 * Updates all the game elements, based on the delta value
	 * @param delta Time increment
	 */
	private void update(int delta) {
		for (int i = 0; i<elementList.size();i++){
			GameElement element = elementList.get(i);
			for (int j = 0; j<elementList.size();j++){
				GameElement other = elementList.get(j);
				if (!element.equals(other) && element.collides(other)){
					element.collide(other);
				}
			}
			element.update(delta);
		}
	}
	
	/**
	 * Renders elements on screen
	 */
	private void render() {
		// TODO Add render logic for all elements
		 
		GL11.glLoadIdentity();
		GL11.glDisable(GL11.GL_LIGHTING);
		renderBackground();
		renderElements();
	}

	private void renderBackground() {
		// TODO Add background
		
	}

	private void renderElements() {
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glTranslatef(0,0,-50);
		for (int i=0; i<elementList.size();i++){
			GameElement element = elementList.get(i);
			element.render();
		}
	}

	/**
	 * Destroys current window and resources 
	 */
	private void cleanupGame() {
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}

}
