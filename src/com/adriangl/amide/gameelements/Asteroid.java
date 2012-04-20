package com.adriangl.amide.gameelements;

import org.lwjgl.opengl.GL11;

public class Asteroid extends GameElement{

	@Override
	public void update(int delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// enable lighting over the rock model

				GL11.glEnable(GL11.GL_LIGHTING);
				
				// store the original matrix setup so we can modify it

				// without worrying about effecting things outside of this 

				// class

				GL11.glPushMatrix();

				// position the model based on the players currently game

				// location

				GL11.glTranslatef(positionX,positionY,0);

				// rotate the rock round to its current Z axis rotate

				GL11.glRotatef(rotationZ,0,0,1);
				
				// scale the model based on the size of rock we're representing

				GL11.glScalef(size, size, size);
				
				// bind the texture we want to apply to our rock and then

				// draw the model 

				texture.bind();
				model.render();
				
				// restore the model matrix so we leave this method

				// in the same state we entered

				GL11.glPopMatrix();
		
	}

	@Override
	public boolean collides(GameElement other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void collide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
