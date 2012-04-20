package com.adriangl.amide.gameelements;

/** 
 * Provides a common interface to create new game elements
 * such as asteroids, spaceships, bullets..
 * @author Adrián García
 */

public interface GameElementInterface {
	/**
	 * Update the element's position based on a delta value.
	 * @param delta Time interval.
	 */
	public void update(int delta);
	
	/**
	 * Renders the element.
	 */
	public void render();
	
	/**
	 * Returns the x coordinate of the selected element.
	 * @return Element's x coordinate.
	 */
	public long getX();
	
	/**
	 * Returns the y coordinate of the selected element.
	 * @return Element's y coordinate.
	 */
	public long getY();
	
	/**
	 * Checks if the current element has collided with another element.
	 * @return True if the elements have collided, false otherwise.
	 */
	public boolean collides(GameElement other);
	
	/**
	 * Actions to take if the element has collided with others.
	 */
	public void collide();
	
	/**
	 * Method which returns the radius of the element.
	 * @return Radius of the element.
	 */
	public float getSize();
}
