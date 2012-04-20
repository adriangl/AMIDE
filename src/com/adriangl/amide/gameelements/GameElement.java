package com.adriangl.amide.gameelements;

/**
 * Provides a basic class to create new game elements.
 * @author Adrián García
 *
 */

public abstract class GameElement implements GameElementInterface{
	
	private long x;
	private long y;
	
	public long getX(){
		return x;
	}
	public long getY(){
		return y;
	}
	
	public boolean collides(GameElementInterface other){
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
