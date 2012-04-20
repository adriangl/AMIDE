package com.adriangl.amide.gameelements;

import java.util.Calendar;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class GameWindow {
	
	public static final String GAME_TITLE = "Asteroid Attack";
	public static int SCREEN_WIDTH = 800;
	public static int SCREEN_HEIGHT = 600;
	
	// Last timestamp when the loop was executed
	private long lastLoop;
	
	// Checks if the game must be closed or not
	public boolean finished = false;
	
	public static void main (String[] args){
		GameWindow gw = new GameWindow();
		gw.runGame();
	}
	
	public GameWindow(){
		try {				
			// Configure and create the LWJGL display			
			initDisplay();
			initGL();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Sys.alert("Error", "Failed: "+e.getMessage());
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
	
	private void runGame(){
		while (!finished){
			// Clear screen
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			// Get difference and current time in millis
			int delta = (int) (Calendar.getInstance().getTimeInMillis() - lastLoop);
			lastLoop = Calendar.getInstance().getTimeInMillis();
			
			// checks several window states to see what we do
			if (Display.isCloseRequested()){
				finished = true;
			}
			else if (Display.isActive()){
				// TODO Add game logic to update and render game elements
				updateElements(delta);
				renderElements();
			}
			else{
				updateElements(delta);
			}
			// Screen update when every operation is finished.
			Display.update();
		}
		
		// when the game is finished, we clean up alll the resources used.
		cleanupGame();
	}
	/**
	 * Updates all the game elements, based on the delta value
	 * @param delta Time increment
	 */
	private void updateElements(int delta) {
		// TODO Add update logic for all elements
		
	}
	
	/**
	 * Renders elements on screen
	 */
	private void renderElements() {
		// TODO Add render logic for all elements
		
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
