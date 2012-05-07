package com.adriangl.amide.gameelements;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import testlwjgl.ObjLoader;
import testlwjgl.ObjModel;
import testlwjgl.Texture;
import testlwjgl.TextureLoader;

public class AssetsProvider {
	
	public static ObjModel asteroidModel;
	public static Texture asteroidTexture;
	
	public static ObjModel spaceShipModel;
	public static Texture spaceShipTexture;
	
	public static Audio spaceShipSound;
	public static Audio asteroidSound;
	
	public static Audio bgmSound;
	
	public static Texture bgTexture;
	
	private static TextureLoader loader = new TextureLoader();
	
	public static void loadData(){
		try {
			//Models
			asteroidModel = ObjLoader.loadObj("asteroid.obj");
			spaceShipModel = ObjLoader.loadObj("chocobo.obj");
			
			//Textures
			asteroidTexture = loader.getTexture("asteroid.jpg");
			spaceShipTexture = loader.getTexture("chocobo.jpg");
			
			bgTexture = loader.getTexture("bg.jpg");
			
			//Sounds
			spaceShipSound = AudioLoader.getAudio("OGG", 
					ResourceLoader.getResourceAsStream("spaceship_sfx.ogg"));
			asteroidSound = AudioLoader.getAudio("OGG", 
					ResourceLoader.getResourceAsStream("asteroid_sfx.ogg"));
					
			bgmSound = AudioLoader.getAudio("OGG", 
					ResourceLoader.getResourceAsStream("bgm.ogg"));
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
