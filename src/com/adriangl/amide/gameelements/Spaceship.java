package com.adriangl.amide.gameelements;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import testlwjgl.ObjModel;
import testlwjgl.Texture;


public class Spaceship extends GameElement {
	
	private Texture texture;
	private ObjModel model;

	private float angle;

	private float forwardX = 0;
	private float forwardY = 1;
	
	private float size;
	
	public Spaceship(Texture texture, ObjModel model) {
		this(texture, model, 0f, 0f, 0.03f, 0f, 0f);
	}
	
	public Spaceship(Texture texture, ObjModel model, float x, float y, float size, float vx, float vy) {
		this.texture = texture;
		this.model = model;
		
		this.speedX = vx;
		this.speedY = vy;
		this.x = x;
		this.y = y;

		this.size = size;
	}
	
	public void update(int delta){
		// if the player is pushing left or right then rotate the
		// ship. Note that the amount rotated is scaled by delta, the
		// amount of time that has passed. This means that rotation
		// stays framerate independent
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			angle += (delta / 5.0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			angle -= (delta / 5.0f);
		}

		// recalculate the forward vector based on the current
		// ship rotation
		forwardX = (float) -Math.sin(Math.toRadians(angle));
		forwardY = (float) Math.cos(Math.toRadians(angle));

		// if the player is pushing the thrust key (up) then
		// increse the velocity in the direction we're currently
		// facing
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			// increase the velocity based on the current forward
			// vector (note again that this is scaled by delta to
			// keep us framerate independent)
			speedX += (forwardX * delta) / 50.0f;
			speedY += (forwardY * delta) / 50.0f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			// TODO Add logic to stop the spaceship
		}

		// call the update the abstract class to cause our generic
		// movement and anything else the abstract implementation
		// provides for us
		super.update(delta);

	}
	
	@Override
	public void render() {
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPushMatrix();

		// position the model based on the players currently game
		// location
		GL11.glTranslatef(x,y,0);
		
		GL11.glRotatef(angle,0,0,1);
		
		// rotate the ship round to our current orientation for shooting
		// GL11.glRotatef(angle,0,0,1);
		GL11.glRotatef(-90, 1, 0, 0);
			
		// scale the model down because its way to big by default
		GL11.glScalef(size, size, size);
			
		// bind to the texture for our model then render it. This 
		// actually draws the geometry to the screen
		texture.bind();
		model.render();
		
		GL11.glPopMatrix();
	}

	@Override
	public void collide(GameElementInterface other) {
		this.speedX = getX() - other.getX();
		this.speedY = getY() - other.getY();
	}

	@Override
	public float getSize() {
		return 2;
	}

	@Override
	public byte[] encode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void decode(byte[] array) {
		// TODO Auto-generated method stub
		
	}

}
