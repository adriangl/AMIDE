package com.adriangl.amide.gameelements;

import org.lwjgl.opengl.GL11;

import testlwjgl.ObjModel;
import testlwjgl.Texture;


public class Asteroid extends GameElement {
	
	private Texture texture;
	private ObjModel model;

	private float angle;
	
	private int size;
	
	public Asteroid(Texture texture, ObjModel model, float x, float y, int size) {
		this(texture, model, x, y, size, (float) (-4 + (Math.random() * 8)), 
	(float) (-4 + (Math.random() * 8)));
	}
	
	public Asteroid(Texture texture, ObjModel model, float x, float y, int size, float vx, float vy) {
		this.texture = texture;
		this.model = model;
		
		this.speedX = vx;
		this.speedY = vy;
		this.x = x;
		this.y = y;

		this.size = size;
	}
	
	public void update(int delta){
		super.update(delta);
		angle = (delta / 10.0f) * 1.5f;
	}
	
	@Override
	public void render() {
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPushMatrix();

		// position the model based on the players currently game
		// location
		GL11.glTranslatef(x,y,0);
		
		// rotate the ship round to our current orientation for shooting
		GL11.glRotatef(angle,0,0,1);
			
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
		// TODO Auto-generated method stub
		this.speedX = getX() - other.getX();
		this.speedY = getY() - other.getY();
	}

	@Override
	public float getSize() {
		return size * 0.5f;
	}

}
