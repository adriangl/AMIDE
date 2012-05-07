package com.adriangl.amide.gameelements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.openal.SoundStore;

import testlwjgl.ObjModel;
import testlwjgl.Texture;


public class Asteroid extends GameElement {
	
	private Texture texture;
	private ObjModel model;

	private float angle;
	private float rotateSpeed = (float)(Math.random()+1)-0.5f;
	
	private float size;
	
	public Asteroid(Texture texture, ObjModel model, float x, float y, float size) {
		this(texture, model, x, y, size, (float) (-4 + (Math.random() * 8)), 
	(float) (-4 + (Math.random() * 8)));
	}
	
	public Asteroid(Texture texture, ObjModel model, float x, float y, float size, float vx, float vy) {
		this.texture = texture;
		this.model = model;
		
		this.speedX = vx;
		this.speedY = vy;
		this.x = x;
		this.y = y;

		this.size = size;
	}
	
	public Asteroid (Texture texture, ObjModel model, float x, float y, float size, 
			float angle, float rotateSpeed, float vx, float vy) {
		this(texture, model, x ,y, size, vx, vy);
		this.angle = angle;
		this.rotateSpeed = rotateSpeed;
	}
	
	public void update(int delta){
		super.update(delta);
		angle += (delta / 10.0f) * rotateSpeed;
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
		playSound();
	}

	private void playSound() {
		AssetsProvider.asteroidSound.playAsSoundEffect(1.0f, 1.0f, false);
		SoundStore.get().poll(0);
	}

	@Override
	public float getSize() {
		return size * 0.5f;
	}

	@Override
	public byte[] encode() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        
        try {
        	out.writeFloat(clientID);
			out.writeFloat(x);
			out.writeFloat(y);
	        out.writeFloat(size);
	        out.writeFloat(angle);
	        out.writeFloat(rotateSpeed);
	        out.writeFloat(speedX);
	        out.writeFloat(speedY);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return baos.toByteArray();
	}

	@Override
	public void decode(byte[] array) {
		// Conversion de los bytes a DatoUdp
        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        DataInputStream in = new DataInputStream(bais);
        try {
			while (in.available() > 0) {
			    String element = in.readUTF();
			    System.out.println(element);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		this.x = newX;
//		this.y = newY;
//		this.size = newSize;
//		this.angle = newAngle;
//		this.rotateSpeed = newRotateSpeed;
//		this.speedX = newSpeedX;
//		this.speedY = newSpeedY;
	}
	

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public ObjModel getModel() {
		return model;
	}

	public void setModel(ObjModel model) {
		this.model = model;
	}

}
