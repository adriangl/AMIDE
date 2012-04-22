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
	
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	
	public void update (int delta){
		x += (speedX * delta) / 1000.0f;
		y += (speedY * delta) / 1000.0f;
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
