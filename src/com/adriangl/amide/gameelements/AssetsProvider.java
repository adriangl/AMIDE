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
	
	private static TextureLoader loader = new TextureLoader();
	
	public static void loadData(){
		try {
			//Models
			asteroidModel = ObjLoader.loadObj("asteroid.obj");
			spaceShipModel = ObjLoader.loadObj("chocobo.obj");
			
			//Textures
			asteroidTexture = loader.getTexture("asteroid.jpg");
			spaceShipTexture = loader.getTexture("chocobo.jpg");
			
			//Sounds
			spaceShipSound = AudioLoader.getAudio("WAV", 
					ResourceLoader.getResourceAsStream("chocobo_sound.wav"));
			asteroidSound = AudioLoader.getAudio("WAV", 
					ResourceLoader.getResourceAsStream("rock_sound.wav"));;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
