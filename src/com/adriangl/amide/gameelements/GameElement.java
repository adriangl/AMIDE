package com.adriangl.amide.gameelements;

/**
 * Provides a basic class to create new game elements.
 * @author Adrián García
 *
 */

public abstract class GameElement implements GameElementInterface{
	
	protected float x = 0;
	protected float y = 0;
	
	protected float speedX = 0;
	protected float speedY = 0;
	
	protected int clientID = 0;
	
	/** The width of the play area - determined by trying things until it felt right */
	private static final int PLAY_AREA_WIDTH = 60;
	/** Half the width of the player area */
	private static final int HALF_WIDTH = PLAY_AREA_WIDTH / 2;
	/** The height of the play area - determined by trying things until it felt right */
	private static final int PLAY_AREA_HEIGHT = 50;
	/** Half the height of the player area */
	private static final int HALF_HEIGHT = PLAY_AREA_HEIGHT / 2;
	
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	
	public int getClientID(){
		return clientID;
	}
	
	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	
	public void update (int delta){
		x += (speedX * delta) / 1000.0f;
		y += (speedY * delta) / 1000.0f;
		
		// if we move off either side of the player area, then come back on
				// the other side. In asteroids all entities have this behaviour
				if (x < -HALF_WIDTH) {
					x = HALF_WIDTH - 1;
				}
				if (x > HALF_WIDTH) {
					x = -(HALF_WIDTH - 1);
				}
				// same again but for top and bottom this time
				if (y < -HALF_HEIGHT) {
					y = HALF_HEIGHT - 1;
				}
				if (y > HALF_HEIGHT) {
					y = -(HALF_HEIGHT - 1);
				}
	}
	
	public boolean collides(GameElementInterface other){
		// We check collisions between elements assuming that the elements are
		// spheres with a specific radius which we will retrieve using the 
		// getSize() method.
		float otherSize = other.getSize();
		float range = (otherSize + getSize());
		range *= range;

		// Get the distance on X and Y between the two entities, then
		// find the squared distance between the two.

		float dx = getX() - other.getX();
		float dy = getY() - other.getY();
		float distance = (dx*dx)+(dy*dy);
		
		// if the squared distance is less than the squared range
		// then we've had a collision!

		return (distance <= range);
	}

}
