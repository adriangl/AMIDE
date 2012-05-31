package com.adriangl.amide.gameelements;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.openal.SoundStore;

import com.adriangl.amide.constants.Constants;

import testlwjgl.ObjModel;
import testlwjgl.Texture;


public class Spaceship extends GameElement {
	
	private Texture texture;
	private ObjModel model;

	private float angle;

	private float forwardX = 0;
	private float forwardY = 1;
	
	private float size;
	
	private float bulletTimeout;
	
	public Spaceship(Texture texture, ObjModel model) {
		this(texture, model, 0f, 0f, 0.03f, 0f, 0f);
	}
	
	public Spaceship(Texture texture, ObjModel model, float x, float y, 
			float size, float vx, float vy) {
		this.texture = texture;
		this.model = model;
		
		this.speedX = vx;
		this.speedY = vy;
		this.x = x;
		this.y = y;

		this.size = size;
	}
	
	public void update(int delta, GameElementList list){
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			angle += (delta / 5.0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			angle -= (delta / 5.0f);
		}

		forwardX = (float) -Math.sin(Math.toRadians(angle));
		forwardY = (float) Math.cos(Math.toRadians(angle));

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			speedX += (forwardX * delta) / 50.0f;
			speedY += (forwardY * delta) / 50.0f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			// TODO Add logic to stop the spaceship
			speedX -= (forwardX * delta) / 50.0f;
			speedY -= (forwardY * delta) / 50.0f;
		}
		
		bulletTimeout -= delta;
		// Generate bullet
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			if (bulletTimeout <= 0){
				generateBullet(list);
				bulletTimeout = Constants.BULLET_TIMEOUT;
			}
		}

		super.update(delta);

	}
	
	private void generateBullet(GameElementList list) {
		// TODO Auto-generated method stub
		Bullet bullet = new Bullet(AssetsProvider.asteroidTexture, 
				AssetsProvider.asteroidModel,
				getX() + forwardX, 
				getY() + forwardY, 
				forwardX * 30, 
				forwardY * 30);
		list.add(bullet);
	}

	@Override
	public void render() {
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPushMatrix();

		GL11.glTranslatef(x,y,0);
		
		GL11.glRotatef(angle,0,0,1);
		
		GL11.glRotatef(-90, 1, 0, 0);
		
		GL11.glScalef(size, size, size);
			
		texture.bind();
		model.render();
		
		GL11.glPopMatrix();
	}

	@Override
	public void collide(GameElementInterface other, GameElementList list) {
		this.speedX = getX() - other.getX();
		this.speedY = getY() - other.getY();
		if (other instanceof Bullet){
			playSound();
		}
	}

	private void playSound() {
		AssetsProvider.spaceShipSound.playAsSoundEffect(1.0f, 0.5f, false);
		SoundStore.get().poll(0);
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
