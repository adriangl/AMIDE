package com.adriangl.amide.gameelements;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class GameWindow {
	
	public static final String GAME_TITLE = "Asteroid Attack";
	
	// Checks if the game must be closed or not
	public boolean finished = false;
	
	public static void main (String[] args){
		GameWindow gw = new GameWindow();
	}
	
	public GameWindow(){
		try {				
			// configure and create the LWJGL display			
			initDisplay();
			initGL();
			runGame();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Sys.alert("Error", "Failed: "+e.getMessage());
		} finally{
			cleanupGame();
		}
		
	}

	private void initGL() {
		// Check some system capabilities: 2d texturing, 
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glShadeModel(GL11.GL_SMOOTH);  		
	}

	/**
	 * Initialises the display
	 * @throws LWJGLException 
	 */

	private void initDisplay() throws LWJGLException{
		// Default display mode: 800x600
		DisplayMode mode = new DisplayMode(800, 600);
		
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
			Display.update();
			
			// checks several window states to see what we do
			if (Display.isCloseRequested()){
				finished = true;
			}
			else if (Display.isActive()){
				renderSquare();
			}
			else{
				try{
					Thread.sleep(100);
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	/** 
	 * Renders a basic square
	 */
	private void renderSquare() {
	    // center square according to screen size
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(0.5f, 0.3f, 1.0f);
			GL11.glVertex3f(-1, 1, 0); 
			GL11.glVertex3f(-1, -1, 0);
			GL11.glVertex3f(1, -1, 0);
			GL11.glVertex3f(1, 1, 0);
		GL11.glEnd();
	}
	
	/**
	 * Destroys current window 
	 */
	private void cleanupGame() {
		Display.destroy();		
	}

}
