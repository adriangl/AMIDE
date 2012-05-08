package com.adriangl.amide.gameelements;

import org.lwjgl.opengl.GL11;

import testlwjgl.ObjModel;
import testlwjgl.Texture;

public class Bullet extends GameElement {
	
	private Texture texture;
	private ObjModel model;
	
	private float size = 0.65f;
	
	private int time = 1000;
	
	public Bullet(Texture texture, ObjModel model, float x, float y, 
			float vx, float vy){
		this.texture = texture;
		this.model = model;
		
		this.speedX = vx;
		this.speedY = vy;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void update (int delta, GameElementList list){
		super.update(delta);
		
		time -= delta;
		
		if (time <= 0){
			removeBullet(list);
		}
	}
	
	private void removeBullet(GameElementList list) {
		list.remove(this);		
	}

	@Override
	public void render() {
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPushMatrix();

		
		GL11.glTranslatef(x,y,0);
			
		GL11.glScalef(size, size, size);
			
		texture.bind();
		model.render();
		
		GL11.glPopMatrix();		
	}

	@Override
	public void collide(GameElementInterface other, GameElementList list) {
		// TODO Auto-generated method stub
		if (other instanceof Asteroid){
			removeBullet(list);
			((Asteroid)other).removeAsteroid(list);
		}
	}

	@Override
	public float getSize() {
		return size;
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
