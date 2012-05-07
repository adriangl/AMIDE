package com.adriangl.amide.gameelements;

import java.io.IOException;
import java.util.Calendar;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.openal.SoundStore;

import testlwjgl.ObjLoader;
import testlwjgl.ObjModel;
import testlwjgl.Texture;
import testlwjgl.TextureLoader;

import com.adriangl.amide.constants.Constants;
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
	
	// Checks if server
	private boolean isServer;
	
	public GameWindow(boolean isServer){
		this.isServer = isServer;
		try {				
			// Configure and create the LWJGL display			
			initDisplay();
			initGL();
			initAssets();
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Sys.alert("Error", "Failed: "+e.getMessage());
		}
		
	}

	private void initAssets() {
		AssetsProvider.loadData();
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
		
		//AssetsProvider.bgmSound.playAsMusic(1.0f, 1.0f, true);
		//SoundStore.get().poll(0);
		
		while (!finished){
			// Clear screen
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			// Get difference and current time in millis
			int delta = (int) (System.currentTimeMillis() - lastLoop);
			lastLoop = Calendar.getInstance().getTimeInMillis();
			
			// checks several window states to see what we do
			if (Display.isCloseRequested()){
				finished = true;
			}
			else{
				synchronized(elementList){
					update(delta);
					render();
				}
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
		for (int i = 0; i<Constants.asteroidNumber; i++){
			Asteroid a = new Asteroid(AssetsProvider.asteroidTexture,
					AssetsProvider.asteroidModel, 
					(float) (-20 + (Math.random() * 40)), 
					(float) (-20 + (Math.random() * 40)), 3f);
			elementList.add(a);
		}
		// Add spaceship
		if (isServer){
			Spaceship sp = new Spaceship(AssetsProvider.spaceShipTexture, 
					AssetsProvider.spaceShipModel);
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
		renderBackground();
		renderElements();
	}

	private void renderBackground() {
		enterOrtho();
		GL11.glDisable(GL11.GL_LIGHTING);
		AssetsProvider.bgTexture.bind();
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0,0);
			GL11.glVertex2i(0,0);
			GL11.glTexCoord2f(0,1);
			GL11.glVertex2i(0,SCREEN_HEIGHT);
			GL11.glTexCoord2f(1,1);
			GL11.glVertex2i(SCREEN_WIDTH,SCREEN_HEIGHT);
			GL11.glTexCoord2f(1,0);
			GL11.glVertex2i(SCREEN_WIDTH,0);
		GL11.glEnd();
		leaveOrtho();
	}
	
	public void enterOrtho() {
		GL11.glPushAttrib(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_ENABLE_BIT);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_PROJECTION); 
		GL11.glPushMatrix();	
		
		GL11.glLoadIdentity();		
		GL11.glOrtho(0, 800, 600, 0, -1, 1);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);  
	}

	public void leaveOrtho() {
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glPopAttrib();
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
		AL.destroy();
	}

}
